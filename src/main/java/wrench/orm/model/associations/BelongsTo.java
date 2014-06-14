package orm.model.associations;

import orm.model.Table;

/**
 * @author mhauck
 */
public interface BelongsTo extends Table {

    ForeignKeys belongsTo();

}
