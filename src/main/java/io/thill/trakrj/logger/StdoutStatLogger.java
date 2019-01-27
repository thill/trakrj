package io.thill.trakrj.logger;

import io.thill.trakrj.Tracker;
import io.thill.trakrj.TrackerId;

import java.util.Date;
import java.util.Map;

/**
 * @author Eric Thill
 */
public class StdoutStatLogger implements StatLogger {
  @Override
  public void configure(Map<String, String> config) {

  }

  @Override
  public void log(TrackerId id, Tracker tracker) {
    System.out.println(new Date() + " - TrakrJ - " + id.display() + " - " + tracker.toString());
  }

}
