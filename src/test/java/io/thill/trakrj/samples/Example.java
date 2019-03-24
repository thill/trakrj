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
package io.thill.trakrj.samples;

import io.thill.trakrj.Intervals;
import io.thill.trakrj.Stats;
import io.thill.trakrj.TrackerId;
import io.thill.trakrj.TrakrJ;
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
    System.setProperty("trakrj.enabled", "true");

    TrakrJ.stats().register(ID.SEC5, new HistogramTracker(), Intervals.seconds(5), Intervals.seconds(5));
    TrakrJ.stats().register(ID.MIN, new HistogramTracker(), Intervals.seconds(5), Intervals.minutes(1));
    TrakrJ.stats().register(ID.AGG, new AggregateLongTracker(), Intervals.seconds(5), Intervals.minutes(1));
    TrakrJ.stats().register(ID.LAST, new LastLongTracker(), Intervals.seconds(5), Intervals.never());
    Random r = new Random();

    while(true) {
      Thread.sleep(50);
      long val = r.nextInt(1000);
      TrakrJ.stats().record(ID.SEC5, val);
      TrakrJ.stats().record(ID.MIN, val);
      TrakrJ.stats().record(ID.AGG, val);
      TrakrJ.stats().record(ID.LAST, val);
    }
  }

  private enum ID implements TrackerId {
    SEC5(1, "Histogram_5s"),
    MIN(2, "Histogram_1m"),
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
