/**
 * MIT License
 *
 * Copyright (c) 2019 Eric Thill
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.thill.trakrj.samples;

import io.thill.trakrj.Intervals;
import io.thill.trakrj.Stats;
import io.thill.trakrj.TrackerId;
import io.thill.trakrj.trackers.AggregateLongTracker;
import io.thill.trakrj.trackers.HistogramTracker;
import io.thill.trakrj.trackers.LastLongTracker;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Eric Thill
 */
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
