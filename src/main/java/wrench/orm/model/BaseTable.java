package orm.model;

import com.google.common.base.CaseFormat;
import orm.utils.PluralUtils;

/**
 * @author mhauck
 */
public abstract class BaseTable implements Table {

    @Override
    public String getTableName() {
        return PluralUtils.pluralize(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, getClass().getSimpleName()));
    }


}
