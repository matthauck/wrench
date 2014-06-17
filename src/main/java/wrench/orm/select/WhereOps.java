package wrench.orm.select;

/**
* @author mhauck
*/
public enum WhereOps {
    EQL("="),
    LT("<"),
    GT(">"),
    LTE("<="),
    GTE(">="),
    LIKE("LIKE"),
    NULL("IS NULL"),
    NOT_NULL("IS NOT NULL");

    private final String op;

    WhereOps(String op) {
        this.op = op;
    }

    public String getOp() {
        return op;
    }

    public boolean takesArgument() {
        return this != NULL && this != NOT_NULL;
    }
}
