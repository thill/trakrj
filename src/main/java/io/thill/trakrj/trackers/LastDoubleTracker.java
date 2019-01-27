package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Tracker;

/**
 * Tracker to track last double value, reset to given nullValue.
 *
 * @author Eric Thill
 */
public class LastDoubleTracker implements Tracker {

  private final double nullValue;
  private double value;

  /**
   * Use {@link Double#NaN} as the reset/null value
   */
  public LastDoubleTracker() {
    this(Double.NaN);
  }

  /**
   * Use the given nullValue as the reset/null value
   *
   * @param nullValue The null value
   */
  public LastDoubleTracker(double nullValue) {
    this.nullValue = nullValue;
  }

  @Override
  public void record(Record record) {
    value = record.getValueDouble();
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
  public double getValue() {
    return value;
  }

  /**
   * Value the last element is reset to, which represents no value being stored.
   *
   * @return The null value
   */
  public double getNullValue() {
    return nullValue;
  }
}
