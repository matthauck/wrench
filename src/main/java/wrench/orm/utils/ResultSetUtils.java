package wrench.orm.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mhauck
 */
public class ResultSetUtils {

    private ResultSetUtils() {}

    public static Map<String, Object> readResultRow(ResultSet results) throws SQLException {

        Map<String, Object> data = new HashMap<>();

        ResultSetMetaData meta = results.getMetaData();
        for (int i = 0; i < meta.getColumnCount(); i++) {
            String name = meta.getColumnName(i + 1);
            Object value = results.getObject(i + 1);
            data.put(name, value);
        }

        return Collections.unmodifiableMap(data);
    }

}
