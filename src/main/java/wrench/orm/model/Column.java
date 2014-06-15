package wrench.orm.model;

/**
 * @author mhauck
 */
public class Column<T> {

    public final String name;
    public final ColumnGetter<T> getter;
    public final ColumnSetter<T> setter;
    public final Class<T> type;

    public Column(String name, Class<T> type, ColumnGetter<T> getter, ColumnSetter<T> setter) {
        this.name = name;
        this.getter = getter;
        this.setter = setter;
        this.type = type;
    }


}
