package orm.utils;

import static org.junit.Assert.*;
import static orm.utils.PluralUtils.*;

import org.junit.Test;

public class PluralUtilsTest {

    @Test
    public void testPluralize() {
        assertEquals("users", pluralize("user"));
        assertEquals("users", pluralize("users"));
        assertEquals("scaries", pluralize("scary"));
        assertEquals("scaries", pluralize("scaries"));
    }

    @Test
    public void testSingularize() {
        assertEquals("user", singularize("users"));
        assertEquals("user", singularize("user"));
        assertEquals("scary", singularize("scaries"));
        assertEquals("scary", singularize("scary"));
    }

}
