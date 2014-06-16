package wrench.orm.model;

import java.util.*;

/**
 * @author mhauck
 */
public class Columns {

    // meant to be case insensitive. solve simply for now by converting keys to lowercase
    private Map<String, Column<?>> columns = new HashMap<>();
    private boolean done = false;

    public Columns() {}

    public <T> Columns addColumn(String name, Class<T> type, ColumnGetter<T> getter, ColumnSetter<T> setter) {
        if (columns.containsKey(key(name))) {
            throw new IllegalArgumentException("Already contains column '" + name + "'");
        }

        columns.put(key(name), new Column<>(name, type, getter, setter));
        return this;
    }

    public Columns done() {
        done = true;
        columns = Collections.unmodifiableMap(columns);
        return this;
    }

    public Collection<Column<?>> columns() {
        if (!done) {
            throw new IllegalStateException("Must call `done` before accessing columns");
        }

        return columns.values();
    }

    public Column<?> column(String field) {
        if (!done) {
            throw new IllegalStateException("Must call `done` before accessing columns");
        }

        return columns.get(key(field));
    }

    private String key(String name) {
        return name.toLowerCase(Locale.ENGLISH);
    }

}
