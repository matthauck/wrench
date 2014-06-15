package wrench.orm;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.BeforeClass;


/**
 * @author mhauck
 */
public class BaseDBTest {

    private static final String JDBC_URL = "jdbc:h2:test-db";

    protected DB db;
    protected Blueprint blueprint = new Blueprint();

    @BeforeClass
    public static void migrate() {
        // hack. delete database before starting up. couldn't get in-memory database to work
        new File("test-db.h2.db").delete();

        Flyway flyway = new Flyway();
        flyway.setDataSource(JDBC_URL, "sa", "");
        flyway.migrate();
    }

    @Before
    public void cleanup() throws SQLException {
        try {
            Class.forName("org.h2.Driver");

            db = new DB(JDBC_URL, "sa", "");

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        // simple manual cleanup...
        db.transaction((Connection conn) -> {
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM \"logos\"");
            stmt.execute("DELETE FROM \"books\"");
            stmt.execute("DELETE FROM \"users\"");

            return null;
        });
    }


}
