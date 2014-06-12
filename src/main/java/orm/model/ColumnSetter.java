package orm.model;

/**
 * @author mhauck
 */
public interface ColumnSetter<T> {

    void set(T value);

}
