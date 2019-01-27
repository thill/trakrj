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
  void configure(Map<String, String> config);

  void log(TrackerId id, Tracker tracker);
}
