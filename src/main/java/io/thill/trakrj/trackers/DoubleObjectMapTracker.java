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
import io.thill.trakrj.function.DoubleObjectConsumer;
import org.eclipse.collections.api.map.primitive.MutableDoubleObjectMap;
import org.eclipse.collections.impl.factory.primitive.DoubleObjectMaps;

/**
 * Tracker to keep double:Object values in a map. Reset clears the map.
 *
 * @author Eric Thill
 */
public class DoubleObjectMapTracker implements Tracker {

	private final MutableDoubleObjectMap<Object> map = DoubleObjectMaps.mutable.empty();

	@Override
	public void record(Record record) {
		map.put(record.getKeyDouble(), record.getValueObject());
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
	public void forEach(DoubleObjectConsumer<Object> c) {
		map.forEachKeyValue((double k, Object v) -> c.accept(k, v));
	}

}
