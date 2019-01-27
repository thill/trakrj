package io.thill.trakrj.internal.tracker;

import io.thill.trakrj.TrackerId;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class SimpleTrackerId implements TrackerId {

  private final int uid;
  private final String display;

  public SimpleTrackerId(int uid, String display) {
    this.uid = uid;
    this.display = display;
  }

  @Override
  public int uid() {
    return uid;
  }

  @Override
  public String display() {
    return display;
  }
}