package io.thill.trakrj.internal.tracker;

import io.thill.trakrj.TrackerId;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class AutoTrackerId implements TrackerId {

  private static final AtomicInteger NEXT_UID = new AtomicInteger();
  private final int uid;
  private final String display;

  public AutoTrackerId(String display) {
    this.uid = NEXT_UID.getAndIncrement();
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