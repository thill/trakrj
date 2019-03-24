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
package io.thill.trakrj.conductor;

import io.thill.trakrj.Tracker;
import io.thill.trakrj.TrackerId;
import io.thill.trakrj.Interval;
import io.thill.trakrj.logger.StatLogger;

import java.util.Map;

/**
 * The {@link Conductor} is responsible for tracker management, log/reset scheduling, and record routing. Conductors are instantiated using reflection and must
 * have a public constructor that takes no arguments.
 *
 * @author Eric Thill
 */
public interface Conductor extends AutoCloseable {
  /**
   * Configure the tracker and start any threads. This is guaranteed to be called before any other method. Any threads that are started should be marked as a
   * daemon thread so they don't prevent the JVM from shutting down.
   *
   * @param config The configuration. All properties in the config starting with "conductor." will have that prefix removed and will be passed in as the
   *               conductor config.
   * @param logger The underlying logger implementation
   */
  void configure(Map<String, String> config, StatLogger logger);

  /**
   * Add a tracker to handle stat records. The underlying implementation is guaranteed to handle this event.
   *
   * @param id            The ID of the tracker. The underlying uid must be unique. The display name will be used for logging.
   * @param tracker       The tracker implementation to handle stat records
   * @param logInterval   The interval to log this tracker's state
   * @param resetInterval The interval to reset. This interval must be a multiple of logInterval.
   */
  void addTracker(TrackerId id, Tracker tracker, Interval logInterval, Interval resetInterval);

  /**
   * Dispatch a record to be handled by the tracker associated with the given {@link TrackerId}.  The associated tracker is responsible for knowing which values
   * are meaningful. Depending on the implementation of this class, some record events may be ignored due to back-pressure.
   *
   * @param id        The ID of the tracker that will handle this event
   * @param keyLong   The key, as a primitive long
   * @param keyDouble The key, as a primitive double
   * @param keyObject The key, as an Object. This object will be passed by reference and therefore should must be immutable.
   * @param valLong   The value, as a primitive long
   * @param valDouble The value, as a primitive double
   * @param valObject The value, as an Object. This object will be passed by reference and therefore should must be immutable.
   */
  void record(TrackerId id, long keyLong, double keyDouble, Object keyObject, long valLong, double valDouble, Object valObject);

  /**
   * Reset the tracker associated with the given {@link TrackerId} on demand. The underlying implementation is guaranteed to handle this event.
   *
   * @param id The ID of the tracker that will be reset
   */
  void reset(TrackerId id);

  /**
   * Gracefully close the conductor threads. This method should block until shutdown is successful.
   */
  void close();
}
