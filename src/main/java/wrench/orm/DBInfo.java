package orm;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author mhauck
 */
public class DBInfo {

    public final boolean storesUpperCaseIdentifiers;
    public final boolean storesLowerCaseIdentifiers;
    public final boolean supportsGetGeneratedKeys;
    public final String identifierQuoteString;

    public DBInfo(DataSource dataSource) throws SQLException {

        Connection conn = dataSource.getConnection();
        try {
            DatabaseMetaData meta = conn.getMetaData();

            storesUpperCaseIdentifiers = meta.storesUpperCaseIdentifiers();
            storesLowerCaseIdentifiers = meta.storesLowerCaseIdentifiers();

            supportsGetGeneratedKeys = meta.supportsGetGeneratedKeys();

            identifierQuoteString = meta.getIdentifierQuoteString();

        } finally {
            conn.close();
        }

    }

}
