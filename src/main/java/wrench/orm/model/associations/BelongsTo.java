package wrench.orm.model.associations;

import wrench.orm.model.Table;

/**
 * @author mhauck
 */
public interface BelongsTo extends Table {

    ForeignKeys belongsTo();

}
