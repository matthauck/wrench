package orm.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * naive pluralization/singularization
 *
 * @author mhauck
 */
public class PluralUtils {

    private static final List<Rule> singularizeRules = Arrays.asList(
        // ends with 'ies' -> ends with 'y'
        new Rule(RuleType.SINGULAR, "ies$", "y"),
        // ends with 's' --> take off the s
        new Rule(RuleType.SINGULAR, "s$", "")
    );

    private static final List<Rule> pluralizeRules = Arrays.asList(
        new Rule(RuleType.PLURAL, "y$", "ies"),
        new Rule(RuleType.PLURAL, "(?<!s)$", "s")
    );


    public static String pluralize(String string) {
        return apply(string, pluralizeRules);
    }

    public static String singularize(String string) {
        return apply(string, singularizeRules);
    }

    private static String apply(String string, List<Rule> rules) {
        for (Rule rule : rules) {
            String candidate = rule.apply(string);
            if (candidate != null) {
                return candidate;
            }
        }

        return string;
    }

    private enum RuleType {
        PLURAL, SINGULAR
    }

    private static class Rule {

        final RuleType type;
        final Pattern rule;
        final String replacement;

        public Rule(RuleType type, String regex, String replacement) {
            this.rule = Pattern.compile(regex);
            this.type = type;
            this.replacement = replacement;
        }

        public String apply(String string) {
            Matcher matcher = rule.matcher(string);
            if (matcher.find()) {
                return matcher.replaceAll(replacement);

            } else {
                return null;
            }
        }
    }

}
