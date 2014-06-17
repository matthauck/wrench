package wrench.orm.model;

/**
 * @author mhauck
 */
public interface ColumnGetter<T extends Table, V> {

    V get(T bean);

}
