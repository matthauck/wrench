package orm;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.jdbc.core.metadata.TableMetaDataContext;
import org.springframework.jdbc.core.metadata.TableMetaDataProvider;
import org.springframework.jdbc.core.metadata.TableMetaDataProviderFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import orm.model.WithId;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mhauck
 */
public class DB {

    private final DataSource dataSource;

    private final TableMapper tableMapper;
    private final TypeMapper typeMapper;

    private final BeanMapper beanMapper;

    private final TableMetaDataContext metaDataContext;
    private final TableMetaDataProvider metaDataProvider;

    public DB(String jdbcUrl) throws SQLException {

        this.tableMapper = new LowerUnderscoreTableMapper();
        this.typeMapper = new DefaultTypeMapper();
        this.beanMapper = new BeanMapper(tableMapper, typeMapper);

        PoolableConnectionFactory connections = new PoolableConnectionFactory(new DriverManagerConnectionFactory(jdbcUrl, null), null);

        ObjectPool<PoolableConnection> pool = new GenericObjectPool<>(connections);
        connections.setPool(pool);

        dataSource = new PoolingDataSource<>(pool);

        metaDataContext = new TableMetaDataContext();
        metaDataProvider = TableMetaDataProviderFactory.createMetaDataProvider(dataSource, metaDataContext, null);
    }


    private String tableName(Class<?> beanType) {
        return metaDataProvider.tableNameToUse(tableMapper.tableName(beanType));
    }


    public <T> T transaction(SQLFunc<T> func) throws SQLException {

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            T result = func.apply(conn);

            conn.commit();
            return result;
        }

    }

    public long insert(Object bean) throws SQLException {

        SimpleJdbcInsert inserter = new SimpleJdbcInsert(dataSource);
        inserter.setGeneratedKeyName("id");

        Number key = inserter.withTableName(tableMapper.tableName(bean.getClass()))
            .executeAndReturnKey(beanMapper.toMap(bean));

        return key.longValue();
    }

    public <T> T find(final long id, final Class<T> beanType) throws SQLException {

        final String sql = "SELECT * FROM " + tableName(beanType) + " WHERE id = ?";

        return fetchOneAndMap(id, beanType, sql);
    }


    public <T> List<T> joinChildren(WithId bean, Class<T> childBeanType) throws SQLException{
        return joinChildren(bean.getId(), bean.getClass(), childBeanType);
    }

    public <T> List<T> joinChildren(long parentId, Class<?> parentBeanType, Class<T> childBeanType) throws SQLException {

        String fk = tableMapper.fkName(parentBeanType);

        final String sql = "SELECT * FROM " + tableName(childBeanType) + " WHERE " + fk + " = ?";

        return fetchAndMap(parentId, childBeanType, sql);
    }

    private <T> T fetchOneAndMap(long id, Class<T> beanType, String sql) throws SQLException {
        return fetchAndMap(id, beanType, sql, true).get(0);
    }
    private <T> List<T> fetchAndMap(long id, Class<T> beanType, String sql) throws SQLException {
        return fetchAndMap(id, beanType, sql, false);
    }

    private <T> List<T> fetchAndMap(long id, Class<T> beanType, String sql, boolean singleResult) throws SQLException {
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

                        T foundObj = beanMapper.fromResultSet(results, beanType);
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
