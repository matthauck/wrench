package wrench.orm.model.associations;

import java.util.HashMap;
import java.util.Map;

import wrench.orm.model.Table;

/**
 * @author mhauck
 */
public class ForeignKeys<MAIN extends Table> {

    private final Map<Class<? extends Table>, ForeignKey> foreignKeys;

    public ForeignKeys() {
        foreignKeys = new HashMap<>();
    }

    public <ASSOC extends Table> ForeignKeys<MAIN> add(ForeignKey<MAIN, ASSOC> fk) {
        foreignKeys.put(fk.assocClass, fk);
        return this;
    }


    @SuppressWarnings("unchecked")
    public <ASSOC extends Table> ForeignKey<MAIN, ASSOC> get(Class<ASSOC> assocClass) {
        return foreignKeys.get(assocClass);
    }

}
