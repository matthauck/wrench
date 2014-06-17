package wrench.orm.model;

import java.util.*;

/**
 * @author mhauck
 */
public class Columns<T extends Table> {

    // meant to be case insensitive. solve simply for now by converting keys to lowercase
    private Map<String, Column<T, ?>> columns = new HashMap<>();
    private boolean done = false;

    public Columns(Class<T> type) {
        // type is for generics to kick in
    }

    public <V> Columns<T> addColumn(String name, Class<V> type, ColumnGetter<T, V> getter, ColumnSetter<T, V> setter) {
        if (columns.containsKey(key(name))) {
            throw new IllegalArgumentException("Already contains column '" + name + "'");
        }

        columns.put(key(name), new Column<>(name, type, getter, setter));
        return this;
    }

    public Columns<T> done() {
        done = true;
        columns = Collections.unmodifiableMap(columns);
        return this;
    }

    public Collection<Column<T, ?>> columns() {
        if (!done) {
            throw new IllegalStateException("Must call `done` before accessing columns");
        }

        return columns.values();
    }

    public Column<T, ?> column(String field) {
        if (!done) {
            throw new IllegalStateException("Must call `done` before accessing columns");
        }

        return columns.get(key(field));
    }

    @SuppressWarnings("unchecked")
    public <V> List<Column<T, V>> columns(Class<V> type) {
        if (!done) {
            throw new IllegalStateException("Must call `done` before accessing columns");
        }

        List<Column<T, V>> list = new LinkedList<>();

        for (Column<T, ?> c : columns.values()) {
            if (c.type.equals(type)) {
                list.add((Column<T, V>) c);
            }
        }

        return list;
    }

    private String key(String name) {
        return name.toLowerCase(Locale.ENGLISH);
    }

}
