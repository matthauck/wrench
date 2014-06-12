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

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mhauck
 */
public class DB {

    private final DataSource dataSource;

    private final RowMapper rowMapper;
    private final TypeMapper typeMapper;

    private final TableMetaDataProvider metaDataProvider;

    private final Map<Class<?>, String> tableNames = new ConcurrentHashMap<>();

    public DB(String jdbcUrl) throws SQLException {

        this.typeMapper = new TypeMapper();
        this.rowMapper = new RowMapper(typeMapper);

        register(new BooleanTypeMapping());

        PoolableConnectionFactory connections = new PoolableConnectionFactory(new DriverManagerConnectionFactory(jdbcUrl, null), null);

        ObjectPool<PoolableConnection> pool = new GenericObjectPool<>(connections);
        connections.setPool(pool);

        dataSource = new PoolingDataSource<>(pool);

        metaDataProvider = TableMetaDataProviderFactory.createMetaDataProvider(dataSource, new TableMetaDataContext(), null);
    }

    public void register(TypeMapping<?> mapping) {
        typeMapper.register(mapping);
    }

    private <T extends Table> String tableName(Class<T> beanType) {
        if (!tableNames.containsKey(beanType)) {
            T newBean = BeanUtils.instantiate(beanType);
            String tableName = metaDataProvider.tableNameToUse(newBean.getTableName());
            tableNames.put(beanType, tableName);
        }

        return tableNames.get(beanType);
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

    public long insert(Table bean) throws SQLException {

        SimpleJdbcInsert inserter = new SimpleJdbcInsert(dataSource);
        inserter.setGeneratedKeyName("id");

        Number key = inserter.withTableName(bean.getTableName())
            .executeAndReturnKey(rowMapper.toMap(bean));

        return key.longValue();
    }

    public <T extends Table> T find(final int id, final Class<T> beanType) throws SQLException {

        final String sql = "SELECT * FROM " + tableName(beanType) + " WHERE id = ?";

        return fetchOneAndMap(id, beanType, sql);
    }

    public <T extends Table> List<T> join(HasMany parent, Class<T> childBeanType) throws SQLException {

        ForeignKey fk = parent.hasMany().get(childBeanType);
        if (fk == null) {
            throw new IllegalStateException("No HasMany relationship defined on " + parent.getClass() + " for " + childBeanType);
        }

        final String sql = "SELECT * FROM " + tableName(childBeanType) + " WHERE " + fk.foreignKeyColumn + " = ?";

        return fetchAndMap(parent.getId(), childBeanType, sql);
    }

    public <T extends Table> T join(BelongsTo child, Class<T> parentBeanType) throws SQLException {

        ForeignKey fk = child.belongsTo().get(parentBeanType);
        if (fk == null) {
            throw new IllegalStateException("No BelongsTo relationship defined on " + child.getClass() + " for " + parentBeanType);
        }

        final String sql = "SELECT * FROM " + tableName(parentBeanType) + " WHERE " + fk.foreignKeyColumn + " = ?";

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
