package org.xiscript.xi.util;

import java.util.HashMap;
import java.util.Map;

public class TimerManager {

	private static class Timer {
		private long start;
		private long stop;

		public void start() {
			start = System.nanoTime();
		}

		public void stop() {
			stop = System.nanoTime();
		}

		public long time() {
			return stop - start;
		}
	}

	private static final Map<Integer, Timer> timers = new HashMap<Integer, Timer>();

	public static void addTimer(int id) {
		Timer t = new Timer();
		timers.put(id, t);
		t.start();
	}

	public static long readTimer(int id) {
		Timer t = timers.get(id);
		t.stop();
		timers.remove(t);
		return t.time();
	}

}
