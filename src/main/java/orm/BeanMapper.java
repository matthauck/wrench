package orm;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import orm.utils.ResultSetUtils;

/**
 * @author mhauck
 */

public class BeanMapper {

    private TableMapper tableMapper;
    private TypeMapper typeMapper;

    public BeanMapper(TableMapper tableMapper, TypeMapper typeMapper) {
        this.tableMapper = tableMapper;
        this.typeMapper = typeMapper;
    }

    public Map<String, Object> toMap(Object object) {

        Map<String, Object> values = new HashMap<>();

        for (PropertyDescriptor prop : BeanUtils.getPropertyDescriptors(object.getClass())) {

            final String propName = prop.getName();

            Object javaValue;
            try {
                javaValue = prop.getReadMethod().invoke(object);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Error reading bean property " + propName, e);
            }
            final Object sqlValue = typeMapper.fromJava(javaValue);

            values.put(tableMapper.columnName(propName), sqlValue);
        }

        return Collections.unmodifiableMap(values);
    }

    public <T> T fromResultSet(ResultSet resultSet, Class<T> beanType) throws SQLException {

        Map<String, Object> values = ResultSetUtils.readResultRow(resultSet);

        T newBean = BeanUtils.instantiate(beanType);

        for (String columnName : values.keySet()) {

            final String propName = tableMapper.fieldName(columnName);

            PropertyDescriptor prop;
            try {
                prop = BeanUtils.getPropertyDescriptor(beanType, propName);
            } catch (BeansException e) {
                continue;
            }
            if (prop == null) {
                continue;
            }

            Method writeMethod = prop.getWriteMethod();
            if (writeMethod == null) {
                continue;
            }

            Object javaValue = typeMapper.fromSql(values.get(columnName), prop.getPropertyType());

            try {
                writeMethod.invoke(newBean, javaValue);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new SQLException("Unable to set column " + columnName + " on bean " + beanType, e);
            }
        }

        return newBean;
    }

}
