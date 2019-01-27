package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Tracker;
import org.HdrHistogram.Histogram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tracker to send all longValues to a histogram. Reset clears the histogram records.
 *
 * @author Eric Thill
 */
public class HistogramTracker implements Tracker {

  public static final List<Double> DEFAULT_PERCENTILES = Collections
          .unmodifiableList(Arrays.asList(0.0, 50.0, 90.0, 99.0, 99.9, 100.0));
  private final Histogram histogram;
  private final List<Double> percentiles;
  private final List<String> percentilesDisplay;

  /**
   * Instantiate the underlying histogram with numberOfSignificantValueDigits=3 and use percentiles=[0.0, 50.0, 90.0, 99.0, 99.9, 100.0]
   */
  public HistogramTracker() {
    this(3, DEFAULT_PERCENTILES);
  }

  /**
   * Instantiate the underlying histogram with numberOfSignificantValueDigits=3 and use the given percentiles
   *
   * @param percentiles The percentiles to log. 0=minValue and 100=maxValue
   */
  public HistogramTracker(List<Double> percentiles) {
    this(3, percentiles);
  }

  /**
   * Instantiate the underlying histogram with the given numberOfSignificantValueDigits and use the given percentiles
   *
   * @param numberOfSignificantValueDigits The numberOfSignificantValueDigits passed to the underlying histogram
   * @param percentiles                    The percentiles to log. 0=minValue and 100=maxValue
   */
  public HistogramTracker(int numberOfSignificantValueDigits, List<Double> percentiles) {
    histogram = new Histogram(numberOfSignificantValueDigits);
    this.percentiles = percentiles;
    this.percentilesDisplay = stringifyPercentiles(percentiles);
  }

  /**
   * Instantiate the underlying histogram with the given arguments and use the given percentiles
   *
   * @param highestTrackableValue          The highestTrackableValue passed to the underlying histogram
   * @param numberOfSignificantValueDigits The numberOfSignificantValueDigits passed to the underlying histogram
   * @param percentiles                    The percentiles to log. 0=minValue and 100=maxValue
   */
  public HistogramTracker(long highestTrackableValue, int numberOfSignificantValueDigits, List<Double> percentiles) {
    histogram = new Histogram(highestTrackableValue, numberOfSignificantValueDigits);
    this.percentiles = percentiles;
    this.percentilesDisplay = stringifyPercentiles(percentiles);
  }

  /**
   * Instantiate the underlying histogram with the given arguments and use the given percentiles
   *
   * @param lowestDiscernibleValue         The lowestDiscernibleValue passed to the underlying histogram
   * @param highestTrackableValue          The highestTrackableValue passed to the underlying histogram
   * @param numberOfSignificantValueDigits The numberOfSignificantValueDigits passed to the underlying histogram
   * @param percentiles                    The percentiles to log. 0=minValue and 100=maxValue
   */
  public HistogramTracker(long lowestDiscernibleValue, long highestTrackableValue, int numberOfSignificantValueDigits,
                          List<Double> percentiles) {
    histogram = new Histogram(lowestDiscernibleValue, highestTrackableValue, numberOfSignificantValueDigits);
    this.percentiles = percentiles;
    this.percentilesDisplay = stringifyPercentiles(percentiles);
  }

  private static List<String> stringifyPercentiles(List<Double> percentiles) {
    List<String> strings = new ArrayList<>();
    for(Double d : percentiles) {
      String s = String.valueOf(d);
      if(s.endsWith(".0")) {
        s = s.substring(0, s.length() - 2);
      }
      strings.add(s);
    }
    return strings;
  }

  @Override
  public void record(Record record) {
    histogram.recordValue(record.getValueLong());
  }

  @Override
  public void reset() {
    histogram.reset();
  }

  /**
   * Get the min value in the underlying histogram
   *
   * @return The min value
   */
  public long getMinValue() {
    return histogram.getMinValue();
  }

  /**
   * Get the max value in the underlying histogram
   *
   * @return The max value
   */
  public long getMaxValue() {
    return histogram.getMaxValue();
  }

  /**
   * Get the value at the given percentile in the underlying histogram
   *
   * @param percentile The percentile
   * @return The value at the given percentile
   */
  public long getValueAtPercentile(double percentile) {
    return histogram.getValueAtPercentile(percentile);
  }

  /**
   * Get the total count of records in the underlying histogram
   *
   * @return The total count
   */
  public long getTotalCount() {
    return histogram.getTotalCount();
  }

  @Override
  public String toString() {
    long count = histogram.getTotalCount();
    StringBuilder sb = new StringBuilder("[");
    if(count == 0) {
      for(int i = 0; i < percentiles.size(); i++) {
        String pctDisplay = percentilesDisplay.get(i);
        sb.append(" ").append(pctDisplay).append("=").append(0);
      }
    } else {
      for(int i = 0; i < percentiles.size(); i++) {
        double pct = percentiles.get(i);
        String pctDisplay = percentilesDisplay.get(i);
        long val;
        if(pct == 0.0) {
          val = histogram.getMinValue();
        } else if(pct == 100.0) {
          val = histogram.getMaxValue();
        } else {
          val = histogram.getValueAtPercentile(pct);
        }
        sb.append(" ").append(pctDisplay).append("=").append(val);
      }
    }
    sb.append(" ] count=").append(count);
    return sb.toString();
  }

}
