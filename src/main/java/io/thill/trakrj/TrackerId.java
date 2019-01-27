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
