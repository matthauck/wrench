package wrench.orm;

import wrench.orm.model.Table;

/**
 * @author mhauck
 */
public interface SQLWhereClause<T extends Table> {

    boolean where(T bean);

    default String column() {
        return null;
    }

}
