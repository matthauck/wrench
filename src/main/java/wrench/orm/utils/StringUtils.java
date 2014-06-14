package orm.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mhauck
 */
public class StringUtils {

    public static String join(List<String> strings) {
        return join(strings, ", ", null);
    }

    public static String joinAndQuote(List<String> strings, String quote) {
        return join(strings, ", ", quote);
    }

    public static String join(List<String> strings, String delim) {
        return join(strings, delim, null);
    }

    public static String join(List<String> strings, String delim, String quote) {
        StringBuilder buf = new StringBuilder();
        boolean first = true;
        for (String s : strings) {
            if (!first) {
                buf.append(delim);
            } else {
                first = false;
            }

            if (quote != null) {
                buf.append(quote).append(s).append(quote);
            } else {
                buf.append(s);
            }
        }

        return buf.toString();
    }


    public static String repeat(String string, int times) {
        return repeat(string, times, ", ");
    }

    public static String repeat(String string, int times, String delim) {
        List<String> list = new ArrayList<>(times);
        for (int i = 0; i < times; i++) {
            list.add(string);
        }

        return join(list, delim);
    }

}
