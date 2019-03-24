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

import io.thill.trakrj.Stat;

/**
 * @author Eric Thill
 */
class StatImpl implements Stat {

  private final String name;
  private final StatType type;
  private boolean nil;
  private long longValue;
  private double doubleValue;
  private Object objectValue;

  public StatImpl(String name, StatType type) {
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

  public StatImpl setLongValue(long longValue) {
    this.nil = false;
    this.longValue = longValue;
    return this;
  }

  public StatImpl setDoubleValue(double doubleValue) {
    this.nil = false;
    this.doubleValue = doubleValue;
    return this;
  }

  public StatImpl setObjectValue(Object objectValue) {
    this.nil = objectValue == null;
    this.objectValue = objectValue;
    return this;
  }

  public StatImpl setNull() {
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
