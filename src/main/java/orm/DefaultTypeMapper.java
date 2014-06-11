package orm;

/**
 * @author mhauck
 */
public class DefaultTypeMapper implements TypeMapper {

    @Override
    public Object fromJava(Object value) {
        if (value == null) {
            return null;
        } else {

            if (value instanceof Boolean) {
                return ((Boolean) value) ? 1 : 0;

            } else {
                return value;
            }
        }
    }

    @Override
    public Object fromSql(Object value, Class<?> javaType) {
        if (value == null) {
            return null;
        } else {

            if (javaType.equals(Boolean.class)) {
                boolean isTrue = ((Number) value).intValue() == 1;
                return isTrue;

            } else if (javaType.equals(long.class)) {
                return ((Number) value).longValue();

            } else if (javaType.equals(int.class)) {
                return ((Number) value).intValue();

            } else {
                return javaType.cast(value);
            }

        }
    }
}
