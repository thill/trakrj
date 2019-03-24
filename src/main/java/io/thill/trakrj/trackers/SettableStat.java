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

/**
 * @author Eric Thill
 */
class SettableStat implements Stat {

  private final String name;
  private final StatType type;
  private boolean nil;
  private long longValue;
  private double doubleValue;
  private Object objectValue;

  public SettableStat(String name, StatType type) {
    this.name = name;
    this.type = type;
    this.nil = true;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public StatType type() {
    return type;
  }

  @Override
  public long longValue() {
    return longValue;
  }

  @Override
  public double doubleValue() {
    return doubleValue;
  }

  @Override
  public Object objectValue() {
    return objectValue;
  }

  public SettableStat setLongValue(long longValue) {
    this.nil = false;
    this.longValue = longValue;
    return this;
  }

  public SettableStat setDoubleValue(double doubleValue) {
    this.nil = false;
    this.doubleValue = doubleValue;
    return this;
  }

  public SettableStat setObjectValue(Object objectValue) {
    this.nil = objectValue == null;
    this.objectValue = objectValue;
    return this;
  }

  public SettableStat setNull() {
    this.nil = true;
    this.objectValue = null;
    return this;
  }

  @Override
  public boolean isNull() {
    return nil;
  }

  @Override
  public String toString() {
    return name + "=" + valueToString();
  }

  private String valueToString() {
    if(isNull()) {
      return null;
    }
    switch(type) {
      case LONG:
        return Long.toString(longValue);
      case DOUBLE:
        return Double.toString(doubleValue);
      case OBJECT:
        return objectValue.toString();
      default:
        return null;
    }
  }
}
