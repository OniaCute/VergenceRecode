package cc.vergence.utils.data;

public final class TimerUtil {
    private long startMs;

    public TimerUtil() {
        reset();
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public long getGapMs() {
        return System.currentTimeMillis() - startMs;
    }

    public double getGapS() {
        return getGapMs() / 1000.0;
    }

    public void reset() {
        startMs = System.currentTimeMillis();
    }
}