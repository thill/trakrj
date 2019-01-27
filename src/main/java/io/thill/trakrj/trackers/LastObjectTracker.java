package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Tracker;

/**
 * Tracker to track last Object value, reset to 0.
 *
 * @author Eric Thill
 */
public class LastObjectTracker implements Tracker {

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
	public String toString() {
		return String.valueOf(value);
	}

	/**
	 * Get the last value
	 *
	 * @return The last value, or null if none exists
	 */
	public Object getValue() {
		return value;
	}
}
