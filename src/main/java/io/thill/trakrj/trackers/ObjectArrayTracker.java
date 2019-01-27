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
