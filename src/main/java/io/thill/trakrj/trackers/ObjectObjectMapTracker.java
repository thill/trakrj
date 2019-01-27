package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Tracker;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tracker to keep Object:Object values in a map. Reset clears the map.
 *
 * @author Eric Thill
 */
public class ObjectObjectMapTracker implements Tracker {

  private final Map<Object, Object> map = new LinkedHashMap<>();

  @Override
  public void record(Record record) {
    map.put(record.getKeyObject(), record.getValueObject());
  }

  @Override
  public void reset() {
    map.clear();
  }

  @Override
  public String toString() {
    return map.toString();
  }

  /**
   * Iterate over all elements in the underlying map. The purpose of providing access to the underlying map as a function is to not expose the underlying data
   * structure types as part of the API.
   *
   * @param c The consumer to accept all values in the map
   */
  public void forEach(Consumer c) {
    map.forEach((Object key, Object value) -> c.accept(key, value));
  }

  @FunctionalInterface
  public interface Consumer {
    void accept(Object key, Object value);
  }
}
