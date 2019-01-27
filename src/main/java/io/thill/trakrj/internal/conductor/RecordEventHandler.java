package io.thill.trakrj.internal.conductor;

import io.thill.trakrj.Tracker;
import io.thill.trakrj.internal.exception.Exceptions;
import io.thill.trakrj.logger.StatLogger;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Internal class. Public methods may change or be removed without warning.
 */

public class RecordEventHandler {

  private final AtomicInteger missedEvents = new AtomicInteger();
  private final MutableIntObjectMap<Tracker> trackers = IntObjectMaps.mutable.empty();
  private final RecordEventRingBuffer ringBuffer;
  private final StatLogger statLogger;
  private final LogScheduler scheduler;

  public RecordEventHandler(RecordEventRingBuffer ringBuffer, StatLogger statLogger) {
    this.ringBuffer = ringBuffer;
    this.statLogger = statLogger;
    this.scheduler = new LogScheduler(ringBuffer);
  }

  public void incrementMissedEvents() {
    missedEvents.incrementAndGet();
  }

  public void start() {
    Thread t = new Thread(this::runLoop, "TrakrJ-Conductor");
    t.setDaemon(true);
    t.start();

    scheduler.start();
  }

  private void runLoop() {
    try {
      while(true) {
        handle(ringBuffer.take());
      }
    } catch(Throwable t) {
      Exceptions.logError("TrakrJ conductor encountered an exception: \n" + Exceptions.throwableToString(t));
    }
  }

  private void handle(RecordEvent event) {
    try {
      switch(event.getType()) {
        case RECORD:
          handleRecord(event);
          break;
        case LOG:
          handleLog(event);
          break;
        case LOG_AND_RESET:
          handleLogAndReset(event);
          break;
        case RESET:
          handleReset(event);
          break;
        case ADD_TRACKER:
          handleAddTracker(event);
          break;
      }
    } catch(Throwable t) {
      Exceptions.logError("Error Handling " + event, t);
    } finally {
      event.reset();
    }
  }

  private void handleRecord(RecordEvent event) {
    Tracker tracker = trackers.get(event.getId().uid());
    if(tracker != null)
      tracker.record(event);
  }

  private void handleLog(RecordEvent event) {
    logMissedEvents();
    Tracker tracker = trackers.get(event.getId().uid());
    if(tracker != null) {
      statLogger.log(event.getId(), tracker);
    }
  }

  private void handleLogAndReset(RecordEvent event) {
    logMissedEvents();
    Tracker tracker = trackers.get(event.getId().uid());
    if(tracker != null) {
      statLogger.log(event.getId(), tracker);
      tracker.reset();
    }
  }

  private void handleReset(RecordEvent event) {
    Tracker tracker = trackers.get(event.getId().uid());
    if(tracker != null) {
      tracker.reset();
    }
  }

  private void logMissedEvents() {
    int missedEvents = this.missedEvents.getAndSet(0);
    if(missedEvents > 0) {
      Exceptions.logWarn("Missed " + missedEvents + " events due to back-pressure");
    }
  }

  private void handleAddTracker(RecordEvent event) {
    if(trackers.containsKey(event.getId().uid())) {
      Exceptions.logError("Multiple trackers registered with uid=" + event.getId().uid());
    }
    trackers.put(event.getId().uid(), event.getTracker());
    scheduler.add(event.getId(), event.getLogInterval(), event.getResetInterval());
  }

}
