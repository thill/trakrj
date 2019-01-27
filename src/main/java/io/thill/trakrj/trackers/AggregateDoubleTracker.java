package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Tracker;

/**
 * Tracker to aggregate double values, reset to 0.
 *
 * @author Eric Thill
 */
public class AggregateDoubleTracker implements Tracker {

	private double value;

	@Override
	public void record(Record record) {
		value += record.getValueDouble();
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
	public double getAggregate() {
		return value;
	}
}
