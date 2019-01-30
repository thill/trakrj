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
package io.thill.trakrj.internal.conductor;

import io.thill.trakrj.Tracker;
import io.thill.trakrj.internal.exception.Exceptions;
import io.thill.trakrj.internal.thread.SignalLatch;
import io.thill.trakrj.logger.StatLogger;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class RecordEventHandler implements AutoCloseable {

  private final AtomicInteger missedEvents = new AtomicInteger();
  private final MutableIntObjectMap<Tracker> trackers = IntObjectMaps.mutable.empty();
  private final AtomicBoolean keepRunning = new AtomicBoolean(true);
  private final SignalLatch shutdownCompleteLatch = new SignalLatch();
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

  @Override
  public void close() {
    keepRunning.set(false);
    scheduler.close();
    shutdownCompleteLatch.await();
  }

  private void runLoop() {
    try {
      while(keepRunning.get()) {
        handle(ringBuffer.take());
      }
    } catch(InterruptedException e) {

    } catch(Throwable t) {
      Exceptions.logError("TrakrJ conductor encountered an exception: \n" + Exceptions.throwableToString(t));
    } finally {
      shutdownCompleteLatch.signal();
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
