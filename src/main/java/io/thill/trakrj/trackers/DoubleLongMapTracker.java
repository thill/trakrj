package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Tracker;
import org.eclipse.collections.api.map.primitive.MutableDoubleLongMap;
import org.eclipse.collections.impl.factory.primitive.DoubleLongMaps;

/**
 * Tracker to keep double:long values in a map. Reset clears the map.
 *
 * @author Eric Thill
 */
public class DoubleLongMapTracker implements Tracker {

	private final MutableDoubleLongMap map = DoubleLongMaps.mutable.empty();

	@Override
	public void record(Record record) {
		map.put(record.getKeyDouble(), record.getValueLong());
	}

	@Override
	public void reset() {
		map.clear();
	}

	@Override
	public String toString() {
		return map.toString();
	}

	/**
	 * Iterate over all elements in the underlying map. The purpose of providing access to the underlying map as a function is to not expose the underlying data
	 * structure types as part of the API.
	 *
	 * @param c The consumer to accept all values in the map
	 */
	public void forEach(Consumer c) {
		map.forEachKeyValue((double k, long v) -> c.accept(k, v));
	}

	@FunctionalInterface
	public interface Consumer {
		void accept(double key, long value);
	}
}
