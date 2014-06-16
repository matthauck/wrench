package wrench.orm.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mhauck
 */
public class StringUtils {

    private static final Pattern titleCaseRegex = Pattern.compile("([A-Z][a-z0-9]*)");

    /**
     * maps class names to lower underscore table_names
     */
    public static String toLowerUnderscore(Class<?> type) {
        final String className = type.getSimpleName();
        Matcher matcher = titleCaseRegex.matcher(className);

        StringBuilder lowerUnderscore = new StringBuilder();
        boolean isFirst = true;
        while (matcher.find()) {
            if (!isFirst) {
                lowerUnderscore.append("_");
            } else {
                isFirst = false;
            }

            lowerUnderscore.append(matcher.group(1).toLowerCase(Locale.ENGLISH));
        }

        return lowerUnderscore.toString();
    }

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
