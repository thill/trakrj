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
