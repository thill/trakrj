package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Tracker;

import java.util.Arrays;

/**
 * Tracker to keep Object values in an array. keyLong is used as the index, and valueObject is used as the value. Reset fills the array with null.
 *
 * @author Eric Thill
 */
public class ObjectArrayTracker implements Tracker {

  private final Object[] array;

  public ObjectArrayTracker(int size) {
    array = new Object[size];
  }

  @Override
  public void record(Record record) {
    array[(int)record.getKeyLong()] = record.getValueObject();
  }

  @Override
  public void reset() {
    Arrays.fill(array, null);
  }

  @Override
  public String toString() {
    return Arrays.toString(array);
  }

  /**
   * Get the length of the underlying array
   *
   * @return The length of the underlying array
   */
  public int length() {
    return array.length;
  }

  /**
   * Iterate over all elements in the underlying array. The purpose of providing access to the underlying array as a function is to not expose the underlying
   * data structure types as part of the API.
   *
   * @param c The consumer to accept all values in the array
   */
  public void forEach(Consumer c) {
    for(int i = 0; i < array.length; i++) {
      c.accept(i, array[i]);
    }
  }

  @FunctionalInterface
  public interface Consumer {
    void accept(int index, Object element);
  }

}
