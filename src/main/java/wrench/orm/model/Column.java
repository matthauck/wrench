package wrench.orm.model;

/**
 * @author mhauck
 */
public class Column<T extends Table, V> {

    public final String name;
    public final ColumnGetter<T, V> getter;
    public final ColumnSetter<T, V> setter;
    public final Class<V> type;

    public Column(String name, Class<V> type, ColumnGetter<T, V> getter, ColumnSetter<T, V> setter) {
        this.name = name;
        this.getter = getter;
        this.setter = setter;
        this.type = type;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Column)) {
            return false;
        }
        Column that = (Column) obj;
        return name.equals(that.name) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
