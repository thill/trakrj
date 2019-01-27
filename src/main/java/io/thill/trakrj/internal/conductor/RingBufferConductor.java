package io.thill.trakrj.internal.conductor;

import io.thill.trakrj.Interval;
import io.thill.trakrj.Tracker;
import io.thill.trakrj.TrackerId;
import io.thill.trakrj.conductor.Conductor;
import io.thill.trakrj.internal.conductor.RecordEvent.Type;
import io.thill.trakrj.logger.StatLogger;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class RingBufferConductor implements Conductor {

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

    new AtomicBoolean().set(true);
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
