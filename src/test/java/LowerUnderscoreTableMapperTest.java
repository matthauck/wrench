import static org.junit.Assert.*;

import org.junit.Test;
import orm.LowerUnderscoreTableMapper;

public class LowerUnderscoreTableMapperTest {

    @Test
    public void testTableName() {
        assertEquals("lower_underscore_table_mapper_test",
            new LowerUnderscoreTableMapper().tableName(LowerUnderscoreTableMapperTest.class));
    }

    @Test
    public void testColumnName() {
        assertEquals("some_property_name", new LowerUnderscoreTableMapper().columnName("somePropertyName"));
    }
}
