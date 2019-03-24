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

/**
 * Tracker to aggregate long values, reset to 0.
 *
 * @author Eric Thill
 */
public class AggregateLongTracker extends AbstractLongTracker {

	private long value;

	public AggregateLongTracker() {
		super(-1);
	}

	@Override
	public void record(Record record) {
		value += record.getValueLong();
	}

	@Override
	public void reset() {
		value = 0;
	}

	@Override
	public long getValue() {
		return value;
	}
}
