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

import io.thill.trakrj.Stat;
import io.thill.trakrj.Stat.StatType;
import io.thill.trakrj.Tracker;

import java.util.Arrays;
import java.util.List;

/**
 * @author Eric Thill
 */
public abstract class AbstractLongTracker implements Tracker {

  private static final String STAT_NAME = "val";

  private final long nullValue;
  private final SettableStat stat;
  private final List<Stat> stats;

  public AbstractLongTracker(long nullValue) {
    this.nullValue = nullValue;
    stat = new SettableStat(STAT_NAME, StatType.DOUBLE);
    stats = Arrays.asList(stat);
  }

  @Override
  public final List<Stat> stats() {
    final long value = getValue();
    if(value == nullValue) {
      stat.setNull();
    } else {
      stat.setLongValue(value);
    }
    return stats;
  }

  public abstract long getValue();

  public long getNullValue() {
    return nullValue;
  }

  @Override
  public String toString() {
    final long value = getValue();
    return value == nullValue ? null : Long.toString(value);
  }
}
