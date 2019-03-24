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
package io.thill.trakrj;

import io.thill.trakrj.conductor.Conductor;
import io.thill.trakrj.conductor.DefaultConductor;
import io.thill.trakrj.logger.StatLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

/**
 * The main API for TrakrJ statistics. This class dispatches statistics to the underlying {@link Conductor}.
 *
 * @author Eric Thill
 */
public class Stats implements AutoCloseable {

  private final Conductor conductor;

  /**
   * Create a {@link Stats} instance with a default conductor using the given {@link StatLogger}
   *
   * @param statLogger The stat logger to be used by the default conductor
   * @return The created {@link Stats} instance
   */
  public static Stats create(StatLogger statLogger) {
    return create(new DefaultConductor(), Collections.emptyMap(), statLogger);
  }

  /**
   * Create a {@link Stats} instance using the given conductor. The given conductor will be configured with the given configuration and {@link StatLogger}.
   *
   * @param conductor The conductor instance to use
   * @param conductorConfig The configuration for the given conductor instance
   * @param statLogger The stat logger to be used by the given conductor instance
   * @return The created {@link Stats} instance
   */
  public static Stats create(Conductor conductor, Map<String, String> conductorConfig, StatLogger statLogger) {
    conductor.configure(conductorConfig, statLogger);
    return new Stats(conductor);
  }

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

  @Override
  public void close() {
    conductor.close();
  }
}
