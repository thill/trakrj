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
import io.thill.trakrj.Stat.StatType;
import io.thill.trakrj.Tracker;

import java.util.Arrays;
import java.util.List;

/**
 * @author Eric Thill
 */
public abstract class AbstractObjectTracker implements Tracker {

  private static final String STAT_NAME = "value";

  private final StatImpl stat;
  private final List<Stat> stats;

  public AbstractObjectTracker() {
    stat = new StatImpl(STAT_NAME, StatType.OBJECT);
    stats = Arrays.asList(stat);
  }

  @Override
  public final List<Stat> stats() {
    final Object value = getValue();
    stat.setObjectValue(value);
    return stats;
  }

  public abstract Object getValue();

  @Override
  public String toString() {
    final Object value = getValue();
    return value == null ? null : value.toString();
  }
}
