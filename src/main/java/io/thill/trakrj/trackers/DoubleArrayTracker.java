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
package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Stat;
import io.thill.trakrj.Stat.StatType;
import io.thill.trakrj.Tracker;
import io.thill.trakrj.function.IntDoubleConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tracker to keep double values in an array. keyLong is used as the index, and valueDouble is used as the value. Reset fills the array with nullValue.
 *
 * @author Eric Thill
 */
public class DoubleArrayTracker implements Tracker {

  private final double[] array;
  private final List<SettableStat> stats = new ArrayList<>();
  private final List<Stat> statsUnmodifiable = Collections.unmodifiableList(stats);
  private final double nullValue;
  private final double resetValue;

  /**
   * Construct the tracker with the given array size. resetValue will default to NaN, nullValue will default to NaN.
   *
   * @param size The size/length of the underlying array
   */
  public DoubleArrayTracker(int size) {
    this(size, Double.NaN, Double.NaN);
  }

  /**
   * Construct the tracker with the given array size and nullValue.
   *
   * @param size       The size/length of the underlying array
   * @param nullValue  The value that will be treated as a null statistic
   * @param resetValue The value that will be filled to the underlying array on a reset.
   */
  public DoubleArrayTracker(int size, double nullValue, double resetValue) {
    array = new double[size];
    this.nullValue = nullValue;
    this.resetValue = resetValue;
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

  @Override
  public List<Stat> stats() {
    if(stats.size() == 0) {
      for(int i = 0; i < array.length; i++) {
        stats.add(new SettableStat(Integer.toString(i), StatType.DOUBLE));
      }
    }
    for(int i = 0; i < array.length; i++) {
      if(array[i] == nullValue) {
        stats.get(i).setNull();
      } else {
        stats.get(i).setDoubleValue(array[i]);
      }
    }
    return statsUnmodifiable;
  }
}
