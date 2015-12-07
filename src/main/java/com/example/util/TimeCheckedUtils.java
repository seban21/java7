package com.example.util;

import java.util.concurrent.TimeUnit;

/**
 * @author gimbyeongsu
 * 
 */
public class TimeCheckedUtils {
	private long startNanoTime;

	public TimeCheckedUtils() {
	}

	public void checkedStart() {
		startNanoTime = System.nanoTime();
	}

	public String getCheckedEndStr(String name) {
		long runNanoTime = System.nanoTime() - startNanoTime;
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(" ");
		long sec = TimeUnit.NANOSECONDS.toSeconds(runNanoTime);
		if (sec == 0L) {
			long mis = TimeUnit.NANOSECONDS.toMillis(runNanoTime);
			long nanos = runNanoTime - (mis * 1000000L);
			sb.append("0sec ").append(mis).append("mis ").append(nanos).append("nanos");
		} else {
			long runNano = runNanoTime - (1000000000L * sec);
			long mis = TimeUnit.NANOSECONDS.toMillis(runNano);
			long nanos = runNanoTime - (sec * 1000000000L + mis * 1000000L);
			sb.append(sec).append("sec ").append(mis).append("mis ").append(nanos).append("nanos");
		}
		return sb.toString();
	}

	public long[] getCheckedEnd() {
		long runNanoTime = System.nanoTime() - startNanoTime;
		long[] time = new long[3];
		long sec = TimeUnit.NANOSECONDS.toSeconds(runNanoTime);
		if (sec == 0L) {
			time[0] = 0L;
			time[1] = TimeUnit.NANOSECONDS.toMillis(runNanoTime);
			time[2] = runNanoTime - (time[1] * 1000000L);
		} else {
			long n = runNanoTime - (1000000000L * sec);
			time[0] = sec;
			time[1] = TimeUnit.NANOSECONDS.toMillis(n);
			time[2] = runNanoTime - (time[0] * 1000000000L + time[1] * 1000000L);
		}
		return time;
	}

	public static String getTotalStr(long runTotalNanoTime) {
		StringBuilder sb = new StringBuilder();
		long second = TimeUnit.NANOSECONDS.toSeconds(runTotalNanoTime);
		if (second == 0L) {
			sb.append("0sec ").append(TimeUnit.NANOSECONDS.toMillis(runTotalNanoTime))
					.append("mis");
		} else {
			long n = runTotalNanoTime - (1000000000L * second);
			sb.append(second).append("sec ").append(TimeUnit.NANOSECONDS.toMillis(n)).append("mis");
		}
		return sb.toString();
	}

	public static long[] getTotal(long runTotalNanoTime) {
		long[] time = new long[2];
		long second = TimeUnit.NANOSECONDS.toSeconds(runTotalNanoTime);
		if (second == 0L) {
			time[0] = 0L;
			time[1] = TimeUnit.NANOSECONDS.toMillis(runTotalNanoTime);
		} else {
			long n = runTotalNanoTime - (1000000000L * second);
			time[0] = second;
			time[1] = TimeUnit.NANOSECONDS.toMillis(n);
		}
		return time;
	}

}
