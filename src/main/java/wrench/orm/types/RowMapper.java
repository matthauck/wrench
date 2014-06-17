package wrench.orm.types;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import wrench.orm.model.Column;
import wrench.orm.model.Table;
import wrench.orm.utils.MiscUtils;
import wrench.orm.utils.ResultSetUtils;

/**
 * @author mhauck
 */
public class RowMapper {

    private final TypeMapper typeMapper;

    public RowMapper(TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    public <T extends Table> SortedMap<String, Object> toMap(T object) {

        SortedMap<String, Object> values = new TreeMap<>();

        @SuppressWarnings("unchecked")
        final Collection<Column<T, ?>> columns = object.getColumns().columns();

        for (Column<T, ?> column : columns) {

            // exclude primary key
            if (column.name.equalsIgnoreCase("id")) {
                continue;
            }

            final Object javaValue = column.getter.get(object);

            final Object sqlValue;
            if (javaValue != null) {
                sqlValue = typeMapper.fromJava(javaValue);
            } else {
                sqlValue = null;
            }

            values.put(column.name, sqlValue);
        }

        return Collections.unmodifiableSortedMap(values);
    }

    public <T extends Table> T fromResultSet(ResultSet resultSet, Class<T> beanType) throws SQLException {

        Map<String, Object> values = ResultSetUtils.readResultRow(resultSet);

        T newBean = MiscUtils.instantiate(beanType);

        for (String columnName : values.keySet()) {

            final Object sqlValue = values.get(columnName);

            @SuppressWarnings("unchecked")
            final Column<T, ?> column = newBean.getColumns().column(columnName);

            setColumnValue(newBean, column, sqlValue);
        }

        return newBean;
    }

    // need a sub-method here for generics to kick in properly
    private <T extends Table, C> void setColumnValue(T bean, Column<T, C> column, Object sqlValue) {
        // this property is not defined for this table class. ignore
        if (column == null) {
            return;
        }

        final C javaValue = typeMapper.fromSql(sqlValue, column.type);
        column.setter.set(bean, javaValue);
    }

}
