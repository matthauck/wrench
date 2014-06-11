package orm;

/**
 * @author mhauck
 */
public interface TableMapper {

    String tableName(Class<?> modelClass);

    String columnName(String fieldName);

    String fieldName(String columnName);
}
