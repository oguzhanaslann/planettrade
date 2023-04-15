package util;

public class NumberUtils {

    // random number between x and y
    public static int random(int lowerLimit, int upperLimit) {
        return (int) (Math.random() * (upperLimit - lowerLimit + 1) + lowerLimit);
    }


    public static long random(long lowerLimit, long upperLimit) {
        return (long) (Math.random() * (upperLimit - lowerLimit + 1) + lowerLimit);
    }

    public static double random(double lowerLimit, double upperLimit) {
        return (double) (Math.random() * (upperLimit - lowerLimit + 1) + lowerLimit);
    }
}
