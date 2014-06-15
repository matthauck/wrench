package wrench.orm.model;

import java.util.*;

import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * @author mhauck
 */
public class Columns {

    private Map<String, Column<?>> columns = new LinkedCaseInsensitiveMap<>();
    private boolean done = false;

    public Columns() {}

    public <T> Columns addColumn(String name, Class<T> type, ColumnGetter<T> getter, ColumnSetter<T> setter) {
        if (columns.containsKey(name)) {
            throw new IllegalArgumentException("Already contains column '" + name + "'");
        }

        columns.put(name, new Column<>(name, type, getter, setter));
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

        return columns.get(field);
    }

}
