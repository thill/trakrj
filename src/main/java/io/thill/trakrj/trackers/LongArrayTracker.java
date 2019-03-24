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
import io.thill.trakrj.Stat;
import io.thill.trakrj.Stat.StatType;
import io.thill.trakrj.Tracker;
import io.thill.trakrj.function.IntLongConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tracker to keep long values in an array. keyLong is used as the index, and valueLong is used as the value. Reset fills the array with nullValue.
 *
 * @author Eric Thill
 */
public class LongArrayTracker implements Tracker {

  private final long[] array;
  private final List<StatImpl> stats = new ArrayList<>();
  private final List<Stat> statsUnmodifiable = Collections.unmodifiableList(stats);
  private final long nullValue;
  private final long resetValue;

  /**
   * Construct the tracker with the given array size. nullValue will default to Long.MAX_VALUE, resetValue will default to Long.MAX_VALUE.
   *
   * @param size The size/length of the underlying array
   */
  public LongArrayTracker(int size) {
    this(size, Long.MAX_VALUE, Long.MAX_VALUE);
  }

  /**
   * Construct the tracker with the given array size and nullValue.
   *
   * @param size       The size/length of the underlying array
   * @param nullValue  The value that will be treated as a null statistic
   * @param resetValue The value that will be filled to the underlying array on a reset.
   */
  public LongArrayTracker(int size, long nullValue, long resetValue) {
    array = new long[size];
    this.nullValue = nullValue;
    this.resetValue = resetValue;
    reset();
  }

  @Override
  public void record(Record record) {
    array[(int)record.getKeyLong()] = record.getValueLong();
  }

  @Override
  public void reset() {
    Arrays.fill(array, resetValue);
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
  public void forEach(IntLongConsumer c) {
    for(int i = 0; i < array.length; i++) {
      c.accept(i, array[i]);
    }
  }

  /**
   * Get the primitive type associated with null used by the underlying array
   *
   * @return The value associated with null in the underlying array
   */
  public long getNullValue() {
    return nullValue;
  }

  @Override
  public List<Stat> stats() {
    if(stats.size() == 0) {
      for(int i = 0; i < array.length; i++) {
        stats.add(new StatImpl(Integer.toString(i), StatType.DOUBLE));
      }
    }
    for(int i = 0; i < array.length; i++) {
      if(array[i] == nullValue) {
        stats.get(i).setNull();
      } else {
        stats.get(i).setLongValue(array[i]);
      }
    }
    return statsUnmodifiable;
  }
}
