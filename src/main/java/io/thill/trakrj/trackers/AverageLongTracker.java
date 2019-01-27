package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Tracker;

/**
 * Tracker to average long values, reset to 0.
 *
 * @author Eric Thill
 */
public class AverageLongTracker implements Tracker {

  private long aggregate;
  private long numRecords;

  @Override
  public void record(Record record) {
    aggregate += record.getValueLong();
    numRecords++;
  }

  @Override
  public void reset() {
    aggregate = 0;
    numRecords = 0;
  }

  @Override
  public String toString() {
    return String.valueOf(getAverage());
  }

  /**
   * Get the average
   *
   * @return The average
   */
  public long getAverage() {
    return aggregate / numRecords;
  }

}
