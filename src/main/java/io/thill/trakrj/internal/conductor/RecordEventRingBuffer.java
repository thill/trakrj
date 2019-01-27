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
