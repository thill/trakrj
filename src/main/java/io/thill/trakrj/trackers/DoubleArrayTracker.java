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
import io.thill.trakrj.function.IntDoubleConsumer;

import java.util.Arrays;

/**
 * Tracker to keep double values in an array. keyLong is used as the index, and valueDouble is used as the value. Reset fills the array with nullValue.
 *
 * @author Eric Thill
 */
public class DoubleArrayTracker implements Tracker {

  private final double[] array;
  private final double nullValue;

  /**
   * Construct the tracker with the given array size. nullValue will default to 0.
   *
   * @param size The size/length of the underlying array
   */
  public DoubleArrayTracker(int size) {
    this(size, 0);
  }

  /**
   * Construct the tracker with the given array size and nullValue.
   *
   * @param size      The size/length of the underlying array
   * @param nullValue The value that will be filled to the underlying array on a reset.
   */
  public DoubleArrayTracker(int size, double nullValue) {
    array = new double[size];
    this.nullValue = nullValue;
    reset();
  }

  @Override
  public void record(Record record) {
    array[(int)record.getKeyLong()] = record.getValueDouble();
  }

  @Override
  public void reset() {
    Arrays.fill(array, nullValue);
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
  public void forEach(IntDoubleConsumer c) {
    for(int i = 0; i < array.length; i++) {
      c.accept(i, array[i]);
    }
  }

  /**
   * Get the primitive type associated with null used by the underlying array
   *
   * @return The value associated with null in the underlying array
   */
  public double getNullValue() {
    return nullValue;
  }
}
