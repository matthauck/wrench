package orm.model;

/**
 * @author mhauck
 */
public interface Table {

    String getTableName();

    Columns getColumns();

}
