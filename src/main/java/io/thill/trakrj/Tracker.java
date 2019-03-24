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

import java.util.List;

/**
 * A statistic tracker. {@link io.thill.trakrj.logger.StatLogger} implementations will call {@link Object#toString()} for purely text-based loggers or {@link
 * Tracker#stats()} for more fine-grained
 *
 * @author Eric Thill
 */
public interface Tracker {
  void record(Record record);

  void reset();

  List<? extends Stat> stats();
}
