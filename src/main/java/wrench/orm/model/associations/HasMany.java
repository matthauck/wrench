package orm.model.associations;

import orm.model.Table;

/**
 * @author mhauck
 */
public interface HasMany extends Table {

    ForeignKeys hasMany();

}
