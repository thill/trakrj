/**
 * Copyright (c) 2019 Eric Thill
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
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
