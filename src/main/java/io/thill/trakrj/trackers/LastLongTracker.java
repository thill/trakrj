package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Tracker;

/**
 * Tracker to track last long value, reset to given nullValue.
 *
 * @author Eric Thill
 */
public class LastLongTracker implements Tracker {

  private final long nullValue;
  private long value;

  /**
   * Use {@link Long#MAX_VALUE} as the reset/null value
   */
  public LastLongTracker() {
    this(Long.MAX_VALUE);
  }

  /**
   * Use the given nullValue as the reset/null value
   *
   * @param nullValue The null value
   */
  public LastLongTracker(long nullValue) {
    this.nullValue = nullValue;
  }

  @Override
  public void record(Record record) {
    value = record.getValueLong();
  }

  @Override
  public void reset() {
    value = nullValue;
  }

  @Override
  public String toString() {
    return value == nullValue ? "null" : String.valueOf(value);
  }

  /**
   * Get the last value
   *
   * @return The last value
   */
  public long getValue() {
    return value;
  }

  /**
   * Value the last element is reset to, which represents no value being stored.
   *
   * @return The null value
   */
  public long getNullValue() {
    return nullValue;
  }
}
