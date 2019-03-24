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
import io.thill.trakrj.function.IntObjectConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tracker to keep Object values in an array. keyLong is used as the index, and valueObject is used as the value. Reset fills the array with null.
 *
 * @author Eric Thill
 */
public class ObjectArrayTracker implements Tracker {

  private final Object[] array;
  private final List<SettableStat> stats = new ArrayList<>();
  private final List<Stat> statsUnmodifiable = Collections.unmodifiableList(stats);

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
  public void forEach(IntObjectConsumer<Object> c) {
    for(int i = 0; i < array.length; i++) {
      c.accept(i, array[i]);
    }
  }

  @Override
  public List<Stat> stats() {
    if(stats.size() == 0) {
      for(int i = 0; i < array.length; i++) {
        stats.add(new SettableStat(Integer.toString(i), StatType.DOUBLE));
      }
    }
    for(int i = 0; i < array.length; i++) {
      stats.get(i).setObjectValue(array[i]);
    }
    return statsUnmodifiable;
  }
}
