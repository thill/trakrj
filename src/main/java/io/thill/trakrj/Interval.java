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
