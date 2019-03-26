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

import io.thill.trakrj.Interval;
import io.thill.trakrj.Tracker;
import io.thill.trakrj.TrackerId;
import io.thill.trakrj.logger.StatLogger;

import java.util.Map;

/**
 * A disabled {@link Conductor} implementation. All method implementations are empty.
 *
 * @author Eric Thill
 */
public class DisabledConductor implements Conductor {

  private StatLogger logger;

  @Override
  public void configure(Map<String, String> config, StatLogger logger) {
    this.logger = logger;
  }

  @Override
  public void addTracker(TrackerId id, Tracker tracker, Interval logInterval, Interval resetInterval) {

  }

  @Override
  public void record(TrackerId id, long keyLong, double keyDouble, Object keyObject, long valLong, double valDouble, Object valObject) {

  }

  @Override
  public void reset(TrackerId id) {

  }

  @Override
  public void close() {
    logger.close();
  }

}
