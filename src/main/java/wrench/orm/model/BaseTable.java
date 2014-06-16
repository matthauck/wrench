package wrench.orm.model;

import wrench.orm.utils.PluralUtils;
import wrench.orm.utils.StringUtils;

/**
 * @author mhauck
 */
public abstract class BaseTable implements Table {

    @Override
    public String getTableName() {
        return PluralUtils.pluralize(StringUtils.toLowerUnderscore(getClass()));
    }

    @Override
    public boolean isNew() {
        return getId() <= 0;
    }
}
