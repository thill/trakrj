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
package io.thill.trakrj;

import io.thill.trakrj.conductor.Conductor;
import io.thill.trakrj.internal.load.Config;
import io.thill.trakrj.internal.exception.Exceptions;
import io.thill.trakrj.internal.load.Instantiate;
import io.thill.trakrj.internal.conductor.NoOpConductor;
import io.thill.trakrj.logger.StatLogger;

import java.util.Map;

/**
 * The main API for TrakrJ. This class takes care of all static initialization from the trakrj.properties and will dispatch accordingly to the underlying {@link
 * Conductor}. If trakrj.properties is not found, all calls to the underlying conductor will return immediately without executing any logic.
 *
 * @author Eric Thill
 */
public class Stats {

  private static final Conductor CONDUCTOR;

  static {
    Conductor conductor;
    try {
      Map<String, String> config = Config.loadConfig();
      Config.tryPrintConfig(config);
      StatLogger logger = Instantiate.instantiateLogger(config);
      conductor = Instantiate.instantiateConductor(config, logger);
    } catch(Throwable t) {
      Exceptions.logError("Could not instantiate trackrj. All calls to " + Stats.class.getSimpleName() + " will no-op.\n", t);
      conductor = new NoOpConductor();
    }
    CONDUCTOR = conductor;
  }

  public static void register(TrackerId id, Tracker tracker, Interval logInterval, Interval resetInterval) {
    CONDUCTOR.addTracker(id, tracker, logInterval, resetInterval);
  }

  public static void record(TrackerId id, long value) {
    CONDUCTOR.record(id, 0, Double.NaN, null, value, Double.NaN, null);
  }

  public static void record(TrackerId id, double value) {
    CONDUCTOR.record(id, 0, Double.NaN, null, 0, value, null);
  }

  public static void record(TrackerId id, Object value) {
    CONDUCTOR.record(id, 0, Double.NaN, null, 0, Double.NaN, value);
  }

  public static void record(TrackerId id, long key, long value) {
    CONDUCTOR.record(id, key, Double.NaN, null, value, Double.NaN, null);
  }

  public static void record(TrackerId id, long key, double value) {
    CONDUCTOR.record(id, key, Double.NaN, null, 0, value, null);
  }

  public static void record(TrackerId id, long key, Object value) {
    CONDUCTOR.record(id, key, Double.NaN, null, 0, Double.NaN, value);
  }

  public static void record(TrackerId id, double key, long value) {
    CONDUCTOR.record(id, 0, key, null, value, Double.NaN, null);
  }

  public static void record(TrackerId id, double key, double value) {
    CONDUCTOR.record(id, 0, key, null, 0, value, null);
  }

  public static void record(TrackerId id, double key, Object value) {
    CONDUCTOR.record(id, 0, key, null, 0, Double.NaN, value);
  }

  public static void record(TrackerId id, Object key, long value) {
    CONDUCTOR.record(id, 0, Double.NaN, key, value, Double.NaN, null);
  }

  public static void record(TrackerId id, Object key, double value) {
    CONDUCTOR.record(id, 0, Double.NaN, key, 0, value, null);
  }

  public static void record(TrackerId id, Object key, Object value) {
    CONDUCTOR.record(id, 0, Double.NaN, key, 0, Double.NaN, value);
  }

  public static void reset(TrackerId id) {
    CONDUCTOR.reset(id);
  }

}
