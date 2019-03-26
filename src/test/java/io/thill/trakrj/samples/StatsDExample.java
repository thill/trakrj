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
import io.thill.trakrj.logger.StatsDStatLogger;
import io.thill.trakrj.trackers.AggregateLongTracker;
import io.thill.trakrj.trackers.HistogramTracker;
import io.thill.trakrj.trackers.LastLongTracker;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;

/**
 * @author Eric Thill
 */
public class StatsDExample {
  public static void main(String[] args) throws Exception {
    Stats stats = Stats.create(new StatsDStatLogger());

    // Receive and print StatsD data in separate thread
//    new Thread(() -> {
//      try {
//        final byte[] data = new byte[1024];
//        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
//        DatagramSocket serverSocket = new DatagramSocket(8125);
//        while(true) {
//          serverSocket.receive(receivePacket);
//          System.out.println(new String(receivePacket.getData(), 0, receivePacket.getLength()));
//          System.out.println();
//        }
//      } catch(IOException e) {
//        e.printStackTrace();
//      }
//    }).start();

    // Register Stat Trackers
    stats.register(ID.SEC5, new HistogramTracker(), Intervals.seconds(5), Intervals.seconds(5));
    stats.register(ID.MIN, new HistogramTracker(), Intervals.seconds(5), Intervals.minutes(1));
    stats.register(ID.AGG, new AggregateLongTracker(), Intervals.seconds(5), Intervals.minutes(1));
    stats.register(ID.LAST, new LastLongTracker(), Intervals.seconds(5), Intervals.never());
    Random r = new Random();

    // Send random values to Stat Trackers
    while(true) {
      Thread.sleep(50);
      long val = r.nextInt(1000);
      stats.record(ID.SEC5, val);
      stats.record(ID.MIN, val);
      stats.record(ID.AGG, val);
      stats.record(ID.LAST, val);
    }
  }

  private enum ID implements TrackerId {
    SEC5(1, "Histogram_5s"),
    MIN(2, "Histogram_1m"),
    LAST(3, "Last"),
    AGG(4, "Aggregated");

    private final int uid;
    private final String display;

    ID(int uid, String display) {
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
