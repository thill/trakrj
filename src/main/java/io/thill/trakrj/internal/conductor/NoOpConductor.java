package io.thill.trakrj.internal.conductor;

import io.thill.trakrj.Interval;
import io.thill.trakrj.Tracker;
import io.thill.trakrj.TrackerId;
import io.thill.trakrj.conductor.Conductor;
import io.thill.trakrj.logger.StatLogger;

import java.util.Map;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class NoOpConductor implements Conductor {

  @Override
  public void configure(Map<String, String> config, StatLogger logger) {

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
  public void shutdown() {

  }
}
