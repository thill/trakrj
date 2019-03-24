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
package io.thill.trakrj.internal.conductor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class BlockingCommitBarrier {

  private static final int NOT_COMMITED = 0;
  private static final int WAITING = 1;
  private static final int COMMITTED = 2;

  private final Object lock = new Object();
  private final AtomicInteger flag = new AtomicInteger(NOT_COMMITED);

  public void await() throws InterruptedException {
    // only need to wait if we were not already committed
    if(flag.compareAndSet(NOT_COMMITED, WAITING)) {
      synchronized(lock) {
        while(flag.get() != COMMITTED) {
          lock.wait();
        }
      }
    }
  }

  public void commit() {
    // only need to notify if the single consumer was waiting
    if(flag.getAndSet(COMMITTED) == WAITING) {
      synchronized(lock) {
        lock.notifyAll();
      }
    }
  }

  public void reset() {
    flag.set(NOT_COMMITED);
  }

}
