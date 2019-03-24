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

/**
 * Interface that provides functionality for an interval of time. The implementing class should take care of any special logic for leap seconds, etc.
 *
 * @author Eric Thill
 */
public interface Interval {
  long NEVER = -1;

  /**
   * Given a current timestamp, return the first timestamp that will result in an interval passing.
   *
   * @param timestamp The current timestamp in milliseconds
   * @return The first timestamp. {@link Interval#NEVER} indicates the interval will never come to pass.
   */
  long first(long timestamp);

  /**
   * Given a timestamp, return the next timestamp that would represent this interval passing. The typical implementation of this method would be `return
   * timestamp+milliseconds;`
   *
   * @param timestamp The start timestamp in milliseconds
   * @return The end timestamp. {@link Interval#NEVER} indicates the interval will never come to pass.
   */
  long next(long timestamp);
}
