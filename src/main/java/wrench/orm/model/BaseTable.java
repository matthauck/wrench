package wrench.orm.model;

import com.google.common.base.CaseFormat;
import wrench.orm.utils.PluralUtils;

/**
 * @author mhauck
 */
public abstract class BaseTable implements Table {

    @Override
    public String getTableName() {
        return PluralUtils.pluralize(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, getClass().getSimpleName()));
    }

    @Override
    public boolean isNew() {
        return getId() <= 0;
    }
}
