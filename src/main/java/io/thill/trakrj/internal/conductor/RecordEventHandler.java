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
  private final Thread thread;

  public RecordEventHandler(RecordEventRingBuffer ringBuffer, StatLogger statLogger) {
    this.ringBuffer = ringBuffer;
    this.statLogger = statLogger;
    this.scheduler = new LogScheduler(ringBuffer);
    this.thread = new Thread(this::runLoop, "TrakrJ-Conductor");
  }

  public void incrementMissedEvents() {
    missedEvents.incrementAndGet();
  }

  public void start() {
    thread.setDaemon(true);
    thread.start();
    scheduler.start();
  }

  @Override
  public void close() {
    keepRunning.set(false);
    scheduler.close();
    thread.interrupt();
    shutdownCompleteLatch.await();
    statLogger.close();
  }

  private void runLoop() {
    try {
      while(keepRunning.get()) {
        handle(ringBuffer.take());
      }
    } catch(InterruptedException e) {
      return;
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
      statLogger.log(event.getId(), tracker, event.getTimestamp());
    }
  }

  private void handleLogAndReset(RecordEvent event) {
    logMissedEvents();
    Tracker tracker = trackers.get(event.getId().uid());
    if(tracker != null) {
      statLogger.log(event.getId(), tracker, event.getTimestamp());
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
