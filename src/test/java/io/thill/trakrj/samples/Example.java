package io.thill.trakrj.samples;

import io.thill.trakrj.Intervals;
import io.thill.trakrj.Stats;
import io.thill.trakrj.TrackerId;
import io.thill.trakrj.trackers.AggregateLongTracker;
import io.thill.trakrj.trackers.HistogramTracker;
import io.thill.trakrj.trackers.LastLongTracker;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Example {
  public static void main(String[] args) throws Exception {
    Stats.register(ID.SEC5, new HistogramTracker(), Intervals.seconds(5), Intervals.seconds(5));
    Stats.register(ID.MIN, new HistogramTracker(), Intervals.seconds(5), Intervals.minutes(1));
    Stats.register(ID.AGG, new AggregateLongTracker(), Intervals.seconds(5), Intervals.minutes(1));
    Stats.register(ID.LAST, new LastLongTracker(), Intervals.seconds(5), Intervals.never());
    Random r = new Random();

    while(true) {
      Thread.sleep(50);
      long val = r.nextInt(1000);
      Stats.record(ID.SEC5, val);
      Stats.record(ID.MIN, val);
      Stats.record(ID.AGG, val);
      Stats.record(ID.LAST, val);
    }
  }

  private enum ID implements TrackerId {
    SEC5(1, "5s Histogram"),
    MIN(2, "1m Histogram"),
    LAST(3, "Last"),
    AGG(4, "Aggregated");

    private final int uid;
    private final String display;

    private ID(int uid, String display) {
      this.uid = uid;
      this.display = display;
    }
    public int uid() {
      return uid;
    }
    public String display() {
      return display;
    }
  }
}
