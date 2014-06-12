package orm.types;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.springframework.beans.BeanUtils;
import orm.model.Column;
import orm.model.Table;
import orm.utils.ResultSetUtils;

/**
 * @author mhauck
 */

public class RowMapper {

    private final TypeMapper typeMapper;

    public RowMapper(TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    public Map<String, Object> toMap(Table object) {

        Map<String, Object> values = new HashMap<>();

        for (Column<?> column : object.getColumns().columns()) {
            final Object javaValue = column.getter.get();

            final Object sqlValue;
            if (javaValue != null) {
                sqlValue = typeMapper.fromJava(javaValue);
            } else {
                sqlValue = null;
            }

            values.put(column.name, sqlValue);
        }

        return Collections.unmodifiableMap(values);
    }

    public <T extends Table> T fromResultSet(ResultSet resultSet, Class<T> beanType) throws SQLException {

        Map<String, Object> values = ResultSetUtils.readResultRow(resultSet);

        T newBean = BeanUtils.instantiate(beanType);

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
