package wrench.orm.select;

/**
 * @author mhauck
 */
public class WhereClause<V> {

    public final String fieldName;
    public final V value;
    public final WhereOps op;

    public WhereClause(String fieldName, V value, WhereOps op) {
        this.fieldName = fieldName;
        this.value = value;
        this.op = op;
    }

}
