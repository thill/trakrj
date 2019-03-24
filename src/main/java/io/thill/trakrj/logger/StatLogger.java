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
package io.thill.trakrj.logger;

import io.thill.trakrj.Tracker;
import io.thill.trakrj.TrackerId;

import java.util.Map;

/**
 * The {@link StatLogger} is responsible for stringifying trackers and logging them. Typically trackers are stringified using {@link Object#toString()}, but
 * custom implementations for a specific application may decide to use other means.
 *
 * @author Eric Thill
 */
public interface StatLogger {
  /**
   * Configure the logger
   *
   * @param config The configuration
   */
  void configure(Map<String, String> config);

  /**
   * Log the given tracker
   *
   * @param id        The id of the tracker
   * @param tracker   The tracker to log
   * @param timestamp The scheduled log timestamp
   */
  void log(TrackerId id, Tracker tracker, long timestamp);

}
