package utils;

public class RandSleep {
    public static void randomSleep() {
        try {
            Thread.sleep(1000 + (long)(Math.random() * 3000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
