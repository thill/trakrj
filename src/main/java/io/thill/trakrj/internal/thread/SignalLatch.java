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
package io.thill.trakrj.internal.thread;

import java.util.concurrent.CountDownLatch;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class SignalLatch {

  private final CountDownLatch countDownLatch = new CountDownLatch(1);

  public void signal() {
    countDownLatch.countDown();
  }

  public void await() {
    try {
      countDownLatch.await();
    } catch(InterruptedException e) {
      return;
    }
  }
}
