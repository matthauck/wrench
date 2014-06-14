package orm.types;

/**
 * @author mhauck
 */
public interface TypeMapping<T> {

    Class<T> getTargetType();

    /**
     * maps value from java into a value the database can understand.
     * a null value will never be passed in
     */
    Object fromJava(T value);

    /**
     * maps value from database into a value java can understand
     * a null value will never be passed in
     */
    T fromSql(Object value, Class<T> javaType);

}
