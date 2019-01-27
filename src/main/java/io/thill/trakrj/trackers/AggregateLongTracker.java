package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Tracker;

/**
 * Tracker to aggregate long values, reset to 0.
 *
 * @author Eric Thill
 */
public class AggregateLongTracker implements Tracker {

	private long value;

	@Override
	public void record(Record record) {
		value += record.getValueLong();
	}

	@Override
	public void reset() {
		value = 0;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}

	/**
	 * Get the aggregate
	 *
	 * @return The aggregate
	 */
	public long getAggregate() {
		return value;
	}
}
