package wrench.orm.model;

/**
 * @author mhauck
 */
public interface ColumnSetter<T, V> {

    void set(T bean, V value);

}
