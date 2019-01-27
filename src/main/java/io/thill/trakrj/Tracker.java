package io.thill.trakrj;

/**
 * @author Eric Thill
 */
public interface Tracker {
	void record(Record record);
	void reset();
}
