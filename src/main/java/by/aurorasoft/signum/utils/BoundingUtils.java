package by.aurorasoft.signum.utils;

public class BoundingUtils {

    public static <T extends Comparable<T>> T bound(T value, T min, T max) {
        if (value.compareTo(min) < 0) {
            return min;
        } else if (value.compareTo(max) > 0) {
            return max;
        }
        return value;
    }
}
