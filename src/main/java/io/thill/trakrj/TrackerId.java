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
package io.thill.trakrj;

import io.thill.trakrj.internal.tracker.AutoTrackerId;
import io.thill.trakrj.internal.tracker.ImmutableTrackerId;

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
   * The Unique Display Name for the {@link Tracker}. This will be used by the underlying {@link io.thill.trakrj.logger.StatLogger} to provide human readable
   * statistics. Valid characters are 0-9, A-Z, a-z, _
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
    return new ImmutableTrackerId(uid, display);
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
