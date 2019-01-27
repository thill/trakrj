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
 * A single stat record. The context of which values should be used depends on the underlying {@link io.thill.trakrj.Tracker} implementation. Note that this
 * means that long/double/Object values do not type-cast automatically and represent independent values.
 *
 * @author Eric Thill
 */
public interface Record {
  /**
   * Get the long key
   *
   * @return The keyLong
   */
  long getKeyLong();

  /**
   * Get the double key
   *
   * @return The keyDouble
   */
  double getKeyDouble();

  /**
   * Get the object key
   *
   * @return The keyObject
   */
  Object getKeyObject();

  /**
   * Get the long value
   *
   * @return The valueLong
   */
  long getValueLong();

  /**
   * Get the double value
   *
   * @return The valueDouble
   */
  double getValueDouble();

  /**
   * Get the object value
   *
   * @return The valueObject
   */
  Object getValueObject();
}
