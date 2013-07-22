package org.xiscript.xi.compilation;

import java.util.HashMap;
import java.util.Map;

import org.xiscript.xi.datatypes.XiVar;

public class VariableSuite {

	private static class IndexMap {
		private final Map<XiVar, Integer> cache = new HashMap<XiVar, Integer>();
		private int index = 1;

		public int add(XiVar v) {
			Integer last = cache.put(v, index++);
			return (last == null) ? -1 : last;
		}

		public int get(XiVar v) {
			return cache.get(v);
		}

		public boolean has(XiVar v) {
			return cache.containsKey(v);
		}

		@Override
		public String toString() {
			return cache.toString();
		}
	}

	private static class TypeCache {
		private final Map<XiVar, Type> cache = new HashMap<XiVar, Type>();

		public Type put(XiVar v, Type t) {
			return cache.put(v, t);
		}

		public Type get(XiVar v) {
			return cache.get(v);
		}

		@Override
		public String toString() {
			return cache.toString();
		}
	}

	private TypeCache tc;
	private IndexMap im;

	public VariableSuite() {
		tc = new TypeCache();
		im = new IndexMap();
	}

	public int add(XiVar v, Type t) {
		tc.put(v, t);
		return im.add(v);
	}

	public Type typeOf(XiVar v) {
		return tc.get(v);
	}

	public int indexOf(XiVar v) {
		return im.get(v);
	}

	public boolean has(XiVar v) {
		return im.has(v);
	}

	@Override
	public String toString() {
		return tc + " " + im;
	}

}
