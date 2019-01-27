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
package io.thill.trakrj;

import io.thill.trakrj.internal.tracker.AutoTrackerId;
import io.thill.trakrj.internal.tracker.SimpleTrackerId;

/**
 * An ID to be associated with a single {@link Tracker}
 *
 * @author Eric Thill
 */
public interface TrackerId {
  /**
   * The Unique ID for the {@link Tracker}.
   *
   * @return The Unique ID
   */
  int uid();

  /**
   * The Display Name for the {@link Tracker}. This will be used by the underlying {@link io.thill.trakrj.logger.StatLogger} to provide human readable
   * statistics.
   *
   * @return The Display Name
   */
  String display();

  /**
   * Create a {@link TrackerId} using the given uid and display name
   *
   * @param uid     The Unique ID
   * @param display The Display Name
   * @return The {@link TrackerId}
   */
  static TrackerId create(int uid, String display) {
    return new SimpleTrackerId(uid, display);
  }

  /**
   * Create a {@link TrackerId} using the given display name. The uid will be determined automatically, starting with 0 and incrementing by one for each call to
   * this method. This method only guarantees UID uniqueness for {@link TrackerId}s returned by this method.
   *
   * @param display The Display Name
   * @return The {@link TrackerId}
   */
  static TrackerId generate(String display) {
    return new AutoTrackerId(display);
  }

}
