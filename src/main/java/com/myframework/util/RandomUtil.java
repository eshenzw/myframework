package com.myframework.util;

import java.util.Random;

/**
 * 取随机数的工具
 * @author zw<br />
 * 2013-2-20
 */
public class RandomUtil {

	/**
	 * 取随机数(0-1)
	 * 
	 * @return
	 */
	public static double getRandom() {
		Random rd = new Random();
		return rd.nextDouble();
	}

	/**
	 * 取min 到 max 之间的随机整数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static long getRandom(long min, long max) {
		if (min < 0 || max <= 0) {
			return 0;
		}
		if ((max - min) < 0) {
			return 0;
		}
		if (max == min) {
			return min;
		}
		max++;
		Random rd = new Random();
		long tmp = rd.nextInt((int) (max - min));
		return tmp + min;
	}

	/**
	 * 取指定长度的随机数
	 * 
	 * @param size
	 * @return
	 */
	public static String getRandom(int size) {
		if (size <= 0) {
			return null;
		}
		double tmp = getRandom();
		tmp = Math.pow(10, size) * tmp;
		String result = String.valueOf((int) tmp);
		while (result.length() < size) {
			result = "0" + result;
		}
		return result;
	}

	/**
	 * 根据准确率得出是否命中,还可以算出是否重击
	 * 
	 * @param d
	 * @return
	 */
	public static boolean isAttackEd(double d) {
		if (d <= 0) {
			return false;
		}
		double tmp = getRandom();
		if ((tmp + d) >= 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据幸运值算出攻击力
	 * 
	 * @param min
	 * @param max
	 * @param luck
	 * @return
	 */
	public static long getAttackByLuck(long min, long max, long luck) {
		if (luck == 0) {
			return getRandom(min, max);
		} else if (luck == 10) {
			return max;
		}
		long min1 = luck * (max - min) / 10 + min;
		return getRandom(min1, max);
	}
}
