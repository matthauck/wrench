package wrench.orm.model;

/**
 * @author mhauck
 */
public interface Table {

    String getTableName();

    Columns getColumns();

    // convention for simplicity: all tables must implement a `getId` method
    int getId();
    void setId(int id);

    boolean isNew();
}
