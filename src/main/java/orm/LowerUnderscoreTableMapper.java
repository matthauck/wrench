package orm;

import com.google.common.base.CaseFormat;
import orm.utils.PluralUtils;

/**
 * @author mhauck
 */
public class LowerUnderscoreTableMapper implements TableMapper {

    @Override
    public String tableName(Class<?> modelClass) {
        return PluralUtils.pluralize(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelClass.getSimpleName()));
    }

    @Override
    public String fkName(Class<?> modelClass) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelClass.getSimpleName()) + "_id";
    }

    @Override
    public String columnName(String fieldName) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
    }

    @Override
    public String fieldName(String columnName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
    }
}
