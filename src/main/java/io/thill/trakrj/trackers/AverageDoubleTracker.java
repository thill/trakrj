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
