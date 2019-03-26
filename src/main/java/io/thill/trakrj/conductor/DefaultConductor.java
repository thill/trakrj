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
import io.thill.trakrj.internal.conductor.RecordEvent;
import io.thill.trakrj.internal.conductor.RecordEvent.Type;
import io.thill.trakrj.internal.conductor.RecordEventHandler;
import io.thill.trakrj.internal.conductor.RecordEventRingBuffer;
import io.thill.trakrj.logger.StatLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * The default {@link Conductor} implementation. This implementation uses an internal ring buffer. When the internal ring buffer is full, record events will be
 * missed. Should any events be missed, the number of missed events will be logged during the next {@link Tracker} log event.
 *
 * @author Eric Thill
 */
public class DefaultConductor implements Conductor {

  private static final String DISPLAY_NAME_REGEX = "[0-9A-Za-z_]+";
  private static final String CFGKEY_RINGBUFFER_SIZE = "ringbuffer.size";
  private static final String DEFAULT_RINGBUFFER_SIZE = "4096";

  private RecordEventHandler eventHandler;
  private RecordEventRingBuffer ringBuffer;

  @Override
  public void configure(Map<String, String> config, StatLogger logger) {
    int ringBufferSize = Integer.parseInt(config.getOrDefault(CFGKEY_RINGBUFFER_SIZE, DEFAULT_RINGBUFFER_SIZE));
    ringBuffer = new RecordEventRingBuffer(ringBufferSize);

    eventHandler = new RecordEventHandler(ringBuffer, logger);
    eventHandler.start();
  }

  @Override
  public void addTracker(TrackerId id, Tracker tracker, Interval logInterval, Interval resetInterval) {
    if(!id.display().matches(DISPLAY_NAME_REGEX)) {
      throw new IllegalArgumentException("Illegal display name '" + id.display() + "' does not match " + DISPLAY_NAME_REGEX);
    }

    RecordEvent event = ringBuffer.claim();
    event.setType(Type.ADD_TRACKER);
    event.setId(id);
    event.setTracker(tracker);
    event.setLogInterval(logInterval);
    event.setResetInterval(resetInterval);
    ringBuffer.commit(event);
  }

  @Override
  public void record(TrackerId id, long keyLong, double keyDouble, Object keyObject, long valLong, double valDouble, Object valObject) {
    RecordEvent event = ringBuffer.tryClaim();
    if(event == null) {
      eventHandler.incrementMissedEvents();
      return;
    }
    event.setType(Type.RECORD);
    event.setId(id);
    event.setKeyLong(keyLong);
    event.setKeyDouble(keyDouble);
    event.setKeyObject(keyObject);
    event.setValueLong(valLong);
    event.setValueDouble(valDouble);
    event.setValueObject(valObject);
    ringBuffer.commit(event);
  }

  @Override
  public void reset(TrackerId id) {
    RecordEvent event = ringBuffer.claim();
    event.setType(Type.RESET);
    event.setId(id);
    ringBuffer.commit(event);
  }

  @Override
  public void close() {
    eventHandler.close();
  }
}
