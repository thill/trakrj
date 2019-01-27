package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Tracker;
import org.eclipse.collections.api.map.primitive.MutableLongDoubleMap;
import org.eclipse.collections.impl.factory.primitive.LongDoubleMaps;

/**
 * Tracker to keep long:double values in a map. Reset clears the map.
 *
 * @author Eric Thill
 */
public class LongDoubleMapTracker implements Tracker {

	private final MutableLongDoubleMap map = LongDoubleMaps.mutable.empty();

	@Override
	public void record(Record record) {
		map.put(record.getKeyLong(), record.getValueDouble());
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
		map.forEachKeyValue((long k, double v) -> c.accept(k, v));
	}

	@FunctionalInterface
	public interface Consumer {
		void accept(long key, double value);
	}
}
