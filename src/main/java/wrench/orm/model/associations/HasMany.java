package wrench.orm.model.associations;

import wrench.orm.model.Table;

/**
 * @author mhauck
 */
public interface HasMany extends Table {

    ForeignKeys hasMany();

}
