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

/**
 * The main API for TrakrJ statistics. This class dispatches statistics to the underlying {@link Conductor}.
 *
 * @author Eric Thill
 */
public class Stats {

  private final Conductor conductor;

  /**
   * Create a stats instance using the given conductor.
   *
   * @param conductor The conductor
   */
  public Stats(Conductor conductor) {
    this.conductor = conductor;
  }

  /**
   * Register a tracker with the underlying conductor.
   *
   * @param id            The unique ID of this tracker
   * @param tracker       The tracker to register
   * @param logInterval   The interval to log the tracker using the underlying {@link io.thill.trakrj.logger.StatLogger}
   * @param resetInterval The interval to reset the tracker
   */
  public void register(TrackerId id, Tracker tracker, Interval logInterval, Interval resetInterval) {
    conductor.addTracker(id, tracker, logInterval, resetInterval);
  }

  /**
   * Record a single long value.
   *
   * @param id    The id correlated with the tracker to record this value.
   * @param value The value to record.
   */
  public void record(TrackerId id, long value) {
    conductor.record(id, 0, Double.NaN, null, value, Double.NaN, null);
  }

  /**
   * Record a single double value.
   *
   * @param id    The id correlated with the tracker to record this value.
   * @param value The value to record.
   */
  public void record(TrackerId id, double value) {
    conductor.record(id, 0, Double.NaN, null, 0, value, null);
  }

  /**
   * Record a single Object value.
   *
   * @param id    The id correlated with the tracker to record this value.
   * @param value The value to record.
   */
  public void record(TrackerId id, Object value) {
    conductor.record(id, 0, Double.NaN, null, 0, Double.NaN, value);
  }

  /**
   * Record a single long:long key:value pair.
   *
   * @param id    The id correlated with the tracker to record this value.
   * @param value The value to record.
   */
  public void record(TrackerId id, long key, long value) {
    conductor.record(id, key, Double.NaN, null, value, Double.NaN, null);
  }

  /**
   * Record a single long:double key:value pair.
   *
   * @param id    The id correlated with the tracker to record this value.
   * @param value The value to record.
   */
  public void record(TrackerId id, long key, double value) {
    conductor.record(id, key, Double.NaN, null, 0, value, null);
  }

  /**
   * Record a single long:Object key:value pair.
   *
   * @param id    The id correlated with the tracker to record this value.
   * @param value The value to record.
   */
  public void record(TrackerId id, long key, Object value) {
    conductor.record(id, key, Double.NaN, null, 0, Double.NaN, value);
  }

  /**
   * Record a single double:long key:value pair.
   *
   * @param id    The id correlated with the tracker to record this value.
   * @param value The value to record.
   */
  public void record(TrackerId id, double key, long value) {
    conductor.record(id, 0, key, null, value, Double.NaN, null);
  }

  /**
   * Record a single double:double key:value pair.
   *
   * @param id    The id correlated with the tracker to record this value.
   * @param value The value to record.
   */
  public void record(TrackerId id, double key, double value) {
    conductor.record(id, 0, key, null, 0, value, null);
  }

  /**
   * Record a single double:Object key:value pair.
   *
   * @param id    The id correlated with the tracker to record this value.
   * @param value The value to record.
   */
  public void record(TrackerId id, double key, Object value) {
    conductor.record(id, 0, key, null, 0, Double.NaN, value);
  }

  /**
   * Record a single Object:long key:value pair.
   *
   * @param id    The id correlated with the tracker to record this value.
   * @param value The value to record.
   */
  public void record(TrackerId id, Object key, long value) {
    conductor.record(id, 0, Double.NaN, key, value, Double.NaN, null);
  }

  /**
   * Record a single Object:double key:value pair.
   *
   * @param id    The id correlated with the tracker to record this value.
   * @param value The value to record.
   */
  public void record(TrackerId id, Object key, double value) {
    conductor.record(id, 0, Double.NaN, key, 0, value, null);
  }

  /**
   * Record a single Object:Object key:value pair.
   *
   * @param id    The id correlated with the tracker to record this value.
   * @param value The value to record.
   */
  public void record(TrackerId id, Object key, Object value) {
    conductor.record(id, 0, Double.NaN, key, 0, Double.NaN, value);
  }

  /**
   * Reset the tracker correlated with the given ID
   *
   * @param id The ID
   */
  public void reset(TrackerId id) {
    conductor.reset(id);
  }

}
