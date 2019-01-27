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
package io.thill.trakrj.internal.conductor;

import io.thill.trakrj.Interval;
import io.thill.trakrj.Tracker;
import io.thill.trakrj.TrackerId;
import io.thill.trakrj.conductor.Conductor;
import io.thill.trakrj.logger.StatLogger;

import java.util.Map;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class NoOpConductor implements Conductor {

  @Override
  public void configure(Map<String, String> config, StatLogger logger) {

  }

  @Override
  public void addTracker(TrackerId id, Tracker tracker, Interval logInterval, Interval resetInterval) {

  }

  @Override
  public void record(TrackerId id, long keyLong, double keyDouble, Object keyObject, long valLong, double valDouble, Object valObject) {

  }

  @Override
  public void reset(TrackerId id) {

  }

  @Override
  public void shutdown() {

  }
}
