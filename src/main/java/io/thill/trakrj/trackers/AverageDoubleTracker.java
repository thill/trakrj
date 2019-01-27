package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Tracker;

/**
 * Tracker to average double values, reset to 0.
 *
 * @author Eric Thill
 */
public class AverageDoubleTracker implements Tracker {

	private double aggregate;
	private long numRecords;

	@Override
	public void record(Record record) {
		aggregate += record.getValueDouble();
		numRecords++;
	}

	@Override
	public void reset() {
		aggregate = 0;
		numRecords = 0;
	}

	@Override
	public String toString() {
		return String.valueOf(getAverage());
	}

	/**
	 * Get the average
	 *
	 * @return The average
	 */
	public double getAverage() {
		return aggregate / numRecords;
	}

}
