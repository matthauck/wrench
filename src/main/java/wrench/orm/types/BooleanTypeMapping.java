package wrench.orm.types;

/**
 * @author mhauck
 */
public class BooleanTypeMapping implements TypeMapping<Boolean> {

    @Override
    public Class<Boolean> getTargetType() {
        return Boolean.class;
    }

    @Override
    public Object fromJava(Boolean value) {
        return value ? 1 : 0;
    }

    @Override
    public Boolean fromSql(Object value, Class<Boolean> javaType) {
        return ((Number) value).intValue() == 1;
    }
}
