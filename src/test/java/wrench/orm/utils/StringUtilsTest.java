package wrench.orm.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import wrench.orm.test.model.User;

public class StringUtilsTest {

    @Test
    public void testToLowerUnderscore() {

        assertEquals("string_utils_test", StringUtils.toLowerUnderscore(StringUtilsTest.class));
        assertEquals("string_utils", StringUtils.toLowerUnderscore(StringUtils.class));
        assertEquals("user", StringUtils.toLowerUnderscore(User.class));

    }

}
