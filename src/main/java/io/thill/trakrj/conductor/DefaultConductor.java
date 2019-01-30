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
package io.thill.trakrj.conductor;

import io.thill.trakrj.Interval;
import io.thill.trakrj.Tracker;
import io.thill.trakrj.TrackerId;
import io.thill.trakrj.internal.conductor.RecordEvent;
import io.thill.trakrj.internal.conductor.RecordEvent.Type;
import io.thill.trakrj.internal.conductor.RecordEventHandler;
import io.thill.trakrj.internal.conductor.RecordEventRingBuffer;
import io.thill.trakrj.logger.StatLogger;

import java.util.Map;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class DefaultConductor implements Conductor {

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
  public void shutdown() {

  }
}
