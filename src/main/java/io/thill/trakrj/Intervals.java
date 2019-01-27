package io.thill.trakrj;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Provides common {@link Interval} implementations. These implementations will all start at midnight on the current day and increment appropriately to
 * infinity. They currently do not provide any support for adjusting to future midnights impacted by leap seconds.
 *
 * @author Eric Thill
 */
public class Intervals {

  private Intervals() {

  }

  /**
   * Interval represented by the given number of seconds.
   *
   * @param seconds Number of seconds
   * @return The interval
   */
  public static Interval seconds(int seconds) {
    return new MillisInterval(TimeUnit.SECONDS.toMillis(seconds));
  }

  /**
   * Interval represented by the given number of minutes.
   *
   * @param minutes Number of minutes
   * @return The interval
   */
  public static Interval minutes(int minutes) {
    return new MillisInterval(TimeUnit.MINUTES.toMillis(minutes));
  }

  /**
   * Interval represented by the given number of hours.
   *
   * @param hours Number of hours
   * @return The interval
   */
  public static Interval hours(int hours) {
    return new MillisInterval(TimeUnit.MINUTES.toMillis(hours));
  }

  /**
   * Interval represented by a single hour
   *
   * @return The interval
   */
  public static Interval hourly() {
    return new MillisInterval(TimeUnit.HOURS.toMillis(1));
  }

  /**
   * Interval represented by a single day
   *
   * @return The interval
   */
  public static Interval daily() {
    return new MillisInterval(TimeUnit.DAYS.toMillis(1));
  }

  /**
   * Interval that will never come to pass
   *
   * @return The interval
   */
  public static Interval never() {
    return new NeverInterval();
  }

  private static class NeverInterval implements Interval {
    @Override
    public long first(long timestamp) {
      return Interval.NEVER;
    }

    @Override
    public long next(long timestamp) {
      return Interval.NEVER;
    }

    @Override
    public String toString() {
      return "[never]";
    }
  }

  private static class MillisInterval implements Interval {
    private final long millis;

    public MillisInterval(long millis) {
      this.millis = millis;
    }

    @Override
    public long first(long timestamp) {
      long first = midnightOf(timestamp);
      while(first < timestamp) {
        first = next(first);
      }
      return first;
    }

    @Override
    public long next(long timestamp) {
      // TODO leap seconds?
      return timestamp + millis;
    }

    @Override
    public String toString() {
      return "[millis=" + millis + "]";
    }
  }

  private static long midnightOf(long timestamp) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(timestamp);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    final long midnight = cal.getTimeInMillis();
    return midnight;
  }
}
