package wrench.orm.select;

import java.sql.SQLException;
import java.util.*;

import wrench.orm.DB;
import wrench.orm.model.Column;
import wrench.orm.model.ColumnGetter;
import wrench.orm.model.Table;
import wrench.orm.utils.MiscUtils;

/**
 * @author mhauck
 */
public class WhereBuilder<T extends Table> {

    private final DB db;
    private final Class<T> type;
    private final T testBean;

    private static final byte[] testByteArray = new byte[]{ 1 };

    final List<WhereClause<?>> clauses = new LinkedList<>();

    public WhereBuilder(Class<T> type, DB db) {
        this.db = db;
        this.type = type;
        this.testBean = MiscUtils.instantiate(type);
    }

    public <V> WhereBuilder<T> where(ColumnGetter<T, V> ref, V value) {
        return where(ref, value, WhereOps.EQL);
    }

    public <V> WhereBuilder<T> where(ColumnGetter<T, V> ref, V value, WhereOps op) {
        @SuppressWarnings("unchecked")
        final Class<V> valueType = (Class<V>) value.getClass();

        final String fieldName = findFieldName(ref, valueType);

        if (fieldName == null) {
            throw new IllegalArgumentException("Could not find field name for given column reference");
        }

        clauses.add(new WhereClause<>(fieldName, value, op));
        return this;
    }

    public List<T> find() throws SQLException {
        return db.select(type, clauses);
    }

    public T findOne() throws SQLException {
        return db.selectOne(type, clauses);
    }

    <V> String findFieldName(ColumnGetter<T, V> ref, Class<V> valueType) {

        @SuppressWarnings("unchecked")
        final Collection<Column<T, V>> columns = testBean.getColumns().columns(valueType);

        for (Column<T, V> c : columns) {
            if (testColumnRef(ref, c)) {
                return c.name;
            }
        }


        return null;
    }


    private <V> boolean testColumnRef(ColumnGetter<T, V> ref, Column<T, V> column) {

        try {
            V testValue = setTestBean(column);

            return testValue.equals(ref.get(testBean));

        } finally {
            clearTestBean(column);
        }
    }

    private <V> void clearTestBean(Column<T, V> column) {
        V value;
        if (Integer.class.isAssignableFrom(column.type)) {
            value = column.type.cast(0);

        } else if (Long.class.isAssignableFrom(column.type)) {
            value = column.type.cast(0l);

        } else if (Short.class.isAssignableFrom(column.type)) {
            value = column.type.cast((short) 0);

        } else if (Float.class.isAssignableFrom(column.type)) {
            value = column.type.cast(0f);

        } else if (Double.class.isAssignableFrom(column.type)) {
            value = column.type.cast(0d);

        } else if (Boolean.class.isAssignableFrom(column.type)) {
            value = column.type.cast(true);

        } else {
            value = column.type.cast(null);
            column.setter.set(testBean, value);
        }

        column.setter.set(testBean, value);
    }

    private <V> V setTestBean(Column<T, V> column) {
        V value;

        if (Integer.class.isAssignableFrom(column.type)) {
            value = column.type.cast(1);

        } else if (Long.class.isAssignableFrom(column.type)) {
            value = column.type.cast(1l);

        } else if (Short.class.isAssignableFrom(column.type)) {
            value = column.type.cast((short) 1);

        } else if (Float.class.isAssignableFrom(column.type)) {
            value = column.type.cast(1f);

        } else if (Double.class.isAssignableFrom(column.type)) {
            value = column.type.cast(1d);

        } else if (String.class.isAssignableFrom(column.type)) {
            value = column.type.cast("test");

        } else if (Boolean.class.isAssignableFrom(column.type)) {
            value = column.type.cast(true);

        } else if (byte[].class.isAssignableFrom(column.type)) {
            value = column.type.cast(testByteArray);

        } else {
            value = MiscUtils.instantiate(column.type);

        }

        column.setter.set(testBean, value);
        return value;
    }

}
