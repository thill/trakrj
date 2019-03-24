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

/**
 * Tracker to track last long value, reset to given nullValue.
 *
 * @author Eric Thill
 */
public class LastLongTracker extends AbstractLongTracker {

  private final long nullValue;
  private long value;

  /**
   * Use {@link Long#MAX_VALUE} as the reset/null value
   */
  public LastLongTracker() {
    this(Long.MAX_VALUE);
  }

  /**
   * Use the given nullValue as the reset/null value
   *
   * @param nullValue The null value
   */
  public LastLongTracker(long nullValue) {
    super(nullValue);
    this.nullValue = nullValue;
  }

  @Override
  public void record(Record record) {
    value = record.getValueLong();
  }

  @Override
  public void reset() {
    value = nullValue;
  }

  @Override
  public long getValue() {
    return value;
  }
}
