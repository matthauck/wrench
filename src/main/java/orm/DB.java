package orm;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.metadata.TableMetaDataContext;
import org.springframework.jdbc.core.metadata.TableMetaDataProvider;
import org.springframework.jdbc.core.metadata.TableMetaDataProviderFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.*;
import java.util.Map;

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

        return transaction((Connection conn) -> {

            String table = metaDataProvider.tableNameToUse(tableMapper.tableName(beanType));

            final String insertSql = "SELECT * FROM " + table + " WHERE id = ?";

            try (PreparedStatement statement = conn.prepareStatement(insertSql)) {
                statement.setLong(1, id);

                boolean success = statement.execute();

                if (!success) {
                    return null;
                }


                T foundObj = null;

                try (ResultSet results = statement.getResultSet()) {

                    while (results.next()) {
                        if (foundObj != null) {
                            throw new SQLException("Non-unique result");
                        }

                        foundObj = beanMapper.fromResultSet(results, beanType);
                    }
                }

                return foundObj;
            }
        });
    }


}
