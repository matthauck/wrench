package wrench.orm.types;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author mhauck
 */
public class TypeMapper {

    private Set<TypeMapping<?>> mappings = new LinkedHashSet<>();

    public void register(TypeMapping<?> mapping) {
        mappings.add(mapping);
    }

    public <T> Object fromJava(Object value) {
        if (value == null) {
            return null;
        } else {

            for (TypeMapping<?> map : mappings) {
                if (map.getTargetType().isInstance(value)) {

                    @SuppressWarnings("unchecked")
                    TypeMapping<T> genMap = (TypeMapping<T>) map;
                    @SuppressWarnings("unchecked")
                    T genValue = (T) value;

                    return genMap.fromJava(genValue);
                }
            }

            // no mappings applied. just return the value!
            return value;
        }
    }

    public <T> T fromSql(Object value, Class<T> javaType) {
        if (value == null) {
            return null;
        } else {

            for (TypeMapping<?> map : mappings) {
                if (javaType.equals(map.getTargetType())) {
                    @SuppressWarnings("unchecked")
                    TypeMapping<T> realMap = (TypeMapping<T>) map;

                    return realMap.fromSql(value, javaType);
                }
            }

            return javaType.cast(value);
        }
    }
}
