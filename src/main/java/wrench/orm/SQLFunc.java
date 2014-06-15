package wrench.orm;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author mhauck
 */
public interface SQLFunc<T> {

    T apply(Connection conn) throws SQLException;

}
