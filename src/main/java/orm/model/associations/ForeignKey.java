package orm.model.associations;

import orm.model.Table;

/**
 * @author mhauck
 */
public class ForeignKey<MAIN extends Table, ASSOC extends Table> {

    public interface ForeignId {
        int get();
    }

    public final Class<MAIN> mainClass;
    public final Class<ASSOC> assocClass;
    public final String foreignKeyColumn;
    public final ForeignId id;

    public ForeignKey(String foreignKeyColumn, Class<MAIN> mainClass, Class<ASSOC> associateClass, ForeignId id) {
        this.mainClass = mainClass;
        this.assocClass = associateClass;
        this.foreignKeyColumn = foreignKeyColumn;
        this.id = id;
    }

}
