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
import io.thill.trakrj.Record;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class RecordEvent implements Record {

  private final BlockingCommitBarrier commitBarrier = new BlockingCommitBarrier();

  private Type type;
  private TrackerId id;
  private Tracker tracker;
  private Interval logInterval;
  private Interval resetInterval;

  private long keyLong;
  private double keyDouble;
  private Object keyObject;
  private long valueLong;
  private double valueDouble;
  private Object valueObject;

  public RecordEvent() {
    reset();
  }

  public void reset() {
    commitBarrier.reset();
    type = null;
    id = null;
    tracker = null;
    keyLong = 0;
    keyDouble = Double.NaN;
    keyObject = null;
    valueLong = 0;
    valueDouble = Double.NaN;
    valueObject = null;
  }

  public void awaitCommit() throws InterruptedException {
    commitBarrier.await();
  }

  public void commit() {
    commitBarrier.commit();
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public TrackerId getId() {
    return id;
  }

  public void setId(TrackerId id) {
    this.id = id;
  }

  public Tracker getTracker() {
    return tracker;
  }

  public void setTracker(Tracker tracker) {
    this.tracker = tracker;
  }

  public Interval getLogInterval() {
    return logInterval;
  }

  public void setLogInterval(Interval logInterval) {
    this.logInterval = logInterval;
  }

  public Interval getResetInterval() {
    return resetInterval;
  }

  public void setResetInterval(Interval resetInterval) {
    this.resetInterval = resetInterval;
  }

  @Override
  public long getKeyLong() {
    return keyLong;
  }

  public void setKeyLong(long keyLong) {
    this.keyLong = keyLong;
  }

  @Override
  public double getKeyDouble() {
    return keyDouble;
  }

  public void setKeyDouble(double keyDouble) {
    this.keyDouble = keyDouble;
  }

  @Override
  public Object getKeyObject() {
    return keyObject;
  }

  public void setKeyObject(Object keyObject) {
    this.keyObject = keyObject;
  }

  @Override
  public long getValueLong() {
    return valueLong;
  }

  public void setValueLong(long valueLong) {
    this.valueLong = valueLong;
  }

  @Override
  public double getValueDouble() {
    return valueDouble;
  }

  public void setValueDouble(double valueDouble) {
    this.valueDouble = valueDouble;
  }

  @Override
  public Object getValueObject() {
    return valueObject;
  }

  public void setValueObject(Object valueObject) {
    this.valueObject = valueObject;
  }

  public enum Type {
    ADD_TRACKER, LOG, LOG_AND_RESET, RECORD, RESET;
  }

  @Override
  public String toString() {
    return "RecordEvent{" +
            "type=" + type +
            ", id=" + id +
            ", tracker=" + tracker +
            ", logInterval=" + logInterval +
            ", resetInterval=" + resetInterval +
            ", keyLong=" + keyLong +
            ", keyDouble=" + keyDouble +
            ", keyObject=" + keyObject +
            ", valueLong=" + valueLong +
            ", valueDouble=" + valueDouble +
            ", valueObject=" + valueObject +
            '}';
  }
}
