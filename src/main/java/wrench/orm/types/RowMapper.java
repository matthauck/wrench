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

    public SortedMap<String, Object> toMap(Table object) {

        SortedMap<String, Object> values = new TreeMap<>();

        for (Column<?> column : object.getColumns().columns()) {

            // exclude primary key
            if (column.name.equalsIgnoreCase("id")) {
                continue;
            }

            final Object javaValue = column.getter.get();

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
            final Column<?> column = newBean.getColumns().column(columnName);

            setColumnValue(column, sqlValue);
        }

        return newBean;
    }

    // need a sub-method here for generics to kick in properly
    private <C> void setColumnValue(Column<C> column, Object sqlValue) {
        // this property is not defined for this table class. ignore
        if (column == null) {
            return;
        }

        final C javaValue = typeMapper.fromSql(sqlValue, column.type);
        column.setter.set(javaValue);
    }

}
