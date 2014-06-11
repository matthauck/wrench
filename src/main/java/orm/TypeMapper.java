package orm;

/**
 * @author mhauck
 */
public interface TypeMapper {

    /**
     * maps value from java into a value the database can understand
     */
    Object fromJava(Object value);

    /**
     * maps value from database into a value java can understand
     */
    Object fromSql(Object value, Class<?> javaType);

}
