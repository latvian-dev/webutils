package dev.latvian.apps.webutils.math;

import java.util.Random;

@SuppressWarnings("ManualMinMaxCalculation")
public interface MathUtils {
	Random RANDOM = new Random();

	static int floor(float f) {
		int i = (int) f;
		return f < (float) i ? i - 1 : i;
	}

	static int floor(double d) {
		int i = (int) d;
		return d < (double) i ? i - 1 : i;
	}

	static long lfloor(double d) {
		long i = (long) d;
		return d < (double) i ? i - 1L : i;
	}

	static int ceil(float f) {
		int i = (int) f;
		return f > (float) i ? i + 1 : i;
	}

	static int ceil(double d) {
		int i = (int) d;
		return d > (double) i ? i + 1 : i;
	}

	static int clamp(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}

	static long clamp(long value, long min, long max) {
		return value < min ? min : value > max ? max : value;
	}

	static float clamp(float value, float min, float max) {
		return value < min ? min : value > max ? max : value;
	}

	static double clamp(double value, double min, double max) {
		return value < min ? min : value > max ? max : value;
	}

	static double smoothstep(double x) {
		return x * x * x * (x * (x * 6D - 15D) + 10D);
	}
}
