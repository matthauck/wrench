package orm;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.metadata.TableMetaDataContext;
import org.springframework.jdbc.core.metadata.TableMetaDataProvider;
import org.springframework.jdbc.core.metadata.TableMetaDataProviderFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.JdbcUtils;
import orm.model.Table;
import orm.model.associations.BelongsTo;
import orm.model.associations.ForeignKey;
import orm.model.associations.HasMany;
import orm.types.BooleanTypeMapping;
import orm.types.RowMapper;
import orm.types.TypeMapper;
import orm.types.TypeMapping;
import orm.utils.StringUtils;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mhauck
 */
public class DB {

    private final DataSource dataSource;

    private final RowMapper rowMapper;
    private final TypeMapper typeMapper;

    private final DBInfo dbInfo;

    private final Map<Class<?>, String> tableNames = new ConcurrentHashMap<>();


    public DB(String jdbcUrl, String user, String pass) throws SQLException {

        this.typeMapper = new TypeMapper();
        this.rowMapper = new RowMapper(typeMapper);

        register(new BooleanTypeMapping());

        PoolableConnectionFactory connections = new PoolableConnectionFactory(new DriverManagerConnectionFactory(jdbcUrl, user, pass), null);

        ObjectPool<PoolableConnection> pool = new GenericObjectPool<>(connections);
        connections.setPool(pool);

        dataSource = new PoolingDataSource<>(pool);

        dbInfo = new DBInfo(dataSource);
    }

    public void register(TypeMapping<?> mapping) {
        typeMapper.register(mapping);
    }

    private <T extends Table> String tableName(Class<T> beanType) {
        if (!tableNames.containsKey(beanType)) {
            T newBean = BeanUtils.instantiate(beanType);
            String tableName = newBean.getTableName();

//            if (dbInfo.storesUpperCaseIdentifiers) {
//                tableName = tableName.toUpperCase(Locale.ENGLISH);
//
//            } else if (dbInfo.storesLowerCaseIdentifiers) {
//                tableName = tableName.toLowerCase(Locale.ENGLISH);
//            }

            tableNames.put(beanType, tableName);
        }

        return quote(tableNames.get(beanType));
    }

    private String quote(String identifier) {
        return dbInfo.identifierQuoteString + identifier + dbInfo.identifierQuoteString;
    }


    public <T> T transaction(SQLFunc<T> func) throws SQLException {
        Connection conn = dataSource.getConnection();
        boolean autoCommitBefore = conn.getAutoCommit();

        try {
            conn.setAutoCommit(false);

            T result = func.apply(conn);

            conn.commit();
            return result;

        } catch (SQLException e) {
            conn.rollback();
            throw e;

        } finally {
            conn.setAutoCommit(autoCommitBefore);
            JdbcUtils.closeConnection(conn);
        }
    }

    public <T extends Table> T insert(T bean) throws SQLException {

        if (!dbInfo.supportsGetGeneratedKeys) {
            throw new IllegalStateException("Database does not support getting generated keys.");
        }

        SortedMap<String, Object> values = rowMapper.toMap(bean);
        List<String> orderedKeys = new ArrayList<>(values.keySet());

        final String sql = "INSERT INTO " + tableName(bean.getClass())
            + " (" + StringUtils.joinAndQuote(orderedKeys, dbInfo.identifierQuoteString) + ")"
            + " VALUES "
            + " (" + StringUtils.repeat("?", orderedKeys.size()) + ")";

        int id = transaction((Connection conn) -> {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            int i = 0;
            for (String key : orderedKeys) {
                i++;
                setStatement(i, stmt, values.get(key));
            }


            // do insert
            stmt.execute();

            // get key!
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);

            } else {
                throw new IllegalStateException("Did not receive a generated key on insert");
            }
        });

        bean.setId(id);
        return bean;
    }

    private void setStatement(final int i, final PreparedStatement stmt, final Object sqlValue) throws SQLException {
        if (sqlValue == null) {
            // set null!
        } else {

            if (sqlValue instanceof Integer) {
                stmt.setInt(i, (Integer) sqlValue);

            } else if (sqlValue instanceof Long) {
                stmt.setLong(i, (Long) sqlValue);

            } else if (sqlValue instanceof String) {
                stmt.setString(i, (String) sqlValue);

            } else {
                throw new UnsupportedOperationException("whoops. not supported yet");
            }

        }

    }

    public <T extends Table> T find(final int id, final Class<T> beanType) throws SQLException {

        final String sql = "SELECT * FROM " + tableName(beanType) + " WHERE " + quote("id") + " = ?";

        return fetchOneAndMap(id, beanType, sql);
    }

    public <T extends Table> List<T> join(HasMany parent, Class<T> childBeanType) throws SQLException {

        ForeignKey fk = parent.hasMany().get(childBeanType);
        if (fk == null) {
            throw new IllegalStateException("No HasMany relationship defined on " + parent.getClass() + " for " + childBeanType);
        }

        final String sql = "SELECT * FROM " + tableName(childBeanType) + " WHERE " + quote(fk.foreignKeyColumn) + " = ?";

        return fetchAndMap(parent.getId(), childBeanType, sql);
    }

    public <T extends Table> T join(BelongsTo child, Class<T> parentBeanType) throws SQLException {

        ForeignKey fk = child.belongsTo().get(parentBeanType);
        if (fk == null) {
            throw new IllegalStateException("No BelongsTo relationship defined on " + child.getClass() + " for " + parentBeanType);
        }

        final String sql = "SELECT * FROM " + tableName(parentBeanType) + " WHERE " + quote(fk.foreignKeyColumn) + " = ?";

        return fetchOneAndMap(fk.id.get(), parentBeanType, sql);
    }

    private <T extends Table> T fetchOneAndMap(int id, Class<T> beanType, String sql) throws SQLException {
        return fetchAndMap(id, beanType, sql, true).get(0);
    }
    private <T extends Table> List<T> fetchAndMap(int id, Class<T> beanType, String sql) throws SQLException {
        return fetchAndMap(id, beanType, sql, false);
    }

    private <T extends Table> List<T> fetchAndMap(int id, Class<T> beanType, String sql, boolean singleResult) throws SQLException {
        return transaction((Connection conn) -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, id);

                boolean success = statement.execute();

                if (!success) {
                    return null;
                }

                try (ResultSet results = statement.getResultSet()) {

                    List<T> found = new LinkedList<>();

                    while (results.next()) {

                        if (singleResult && found.size() > 0) {
                            throw new SQLException("Non-unique result");
                        }

                        T foundObj = rowMapper.fromResultSet(results, beanType);
                        found.add(foundObj);
                    }

                    if (singleResult && found.size() == 0) {
                        throw new SQLException("No element found");
                    }

                    return found;
                }
            }
        });
    }


}
