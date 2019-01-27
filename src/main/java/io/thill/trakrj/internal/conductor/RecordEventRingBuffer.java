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

import java.util.concurrent.atomic.AtomicLong;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class RecordEventRingBuffer {
  private final AtomicLong claimSequence = new AtomicLong(0);
  private final AtomicLong readSequence = new AtomicLong(0);

  private final RecordEvent[] records;
  private final int indexMask;

  public RecordEventRingBuffer(int size) {
    if(!isPowerOfTwo(size)) {
      throw new IllegalArgumentException("size=" + size + " is not a power of two");
    }
    records = new RecordEvent[size];
    for(int i = 0; i < records.length; i++) {
      records[i] = new RecordEvent();
    }
    indexMask = size - 1;
  }

  private static boolean isPowerOfTwo(int val) {
    for(int pow = 1; pow < 31; pow++) {
      int check = 1;
      for(int i = 0; i < pow; i++) {
        check *= 2;
      }
      if(check == val) {
        return true;
      }
    }
    return false;
  }

  public RecordEvent take() throws InterruptedException {
    long sequence = readSequence.getAndIncrement();
    int offset = indexOf(sequence);
    RecordEvent event = records[offset];
    event.awaitCommit();
    return event;
  }

  private int indexOf(long sequence) {
    return (int)(sequence & indexMask);
  }

  public RecordEvent claim() {
    long claimed = claimSequence.getAndIncrement();

    // wait for this event to be writable
    while(isClaimable(claimed)) {
      Thread.yield();
    }

    return records[indexOf(claimed)];
  }

  public RecordEvent tryClaim() {
    long claimed;

    do {
      claimed = claimSequence.get();

      // ensure space is available to claim. if not, return null.
      if(isClaimable(claimed))
        return null;
    }
    while(!claimSequence.compareAndSet(claimed, claimed+1));

    return records[indexOf(claimed)];
  }

  private boolean isClaimable(long sequence) {
    return sequence - readSequence.get() >= records.length - 1;
  }

  public void commit(RecordEvent event) {
    event.commit();
  }

}
