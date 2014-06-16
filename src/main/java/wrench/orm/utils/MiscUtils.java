package wrench.orm.utils;

/**
 * @author mhauck
 */
public class MiscUtils {

    private MiscUtils() {}

    public static void close(AutoCloseable ... closeables) {

        for (AutoCloseable closeable : closeables) {
            try {
                closeable.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    public static <T> T instantiate(Class<T> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Unable to instantiate type " + type + "."
                + " Maybe you need to add a default constructor?");
        }
    }

}
