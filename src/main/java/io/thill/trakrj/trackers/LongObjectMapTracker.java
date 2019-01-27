package io.thill.trakrj.trackers;

import io.thill.trakrj.Record;
import io.thill.trakrj.Tracker;
import org.eclipse.collections.api.map.primitive.MutableLongObjectMap;
import org.eclipse.collections.impl.factory.primitive.LongObjectMaps;

/**
 * Tracker to keep long:Object values in a map. Reset clears the map.
 *
 * @author Eric Thill
 */
public class LongObjectMapTracker implements Tracker {

	private final MutableLongObjectMap<Object> map = LongObjectMaps.mutable.empty();

	@Override
	public void record(Record record) {
		map.put(record.getKeyLong(), record.getValueObject());
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
		map.forEachKeyValue((long k, Object v) -> c.accept(k, v));
	}

	@FunctionalInterface
	public interface Consumer {
		void accept(long key, Object value);
	}
}
