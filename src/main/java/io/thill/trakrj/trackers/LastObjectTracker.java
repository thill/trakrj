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
package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Tracker;

/**
 * Tracker to track last Object value, reset to 0.
 *
 * @author Eric Thill
 */
public class LastObjectTracker extends AbstractObjectTracker {

	private Object value;

	@Override
	public void record(Record record) {
		value = record.getValueObject();
	}

	@Override
	public void reset() {
		value = null;
	}
	
	@Override
	public Object getValue() {
		return value;
	}
}
