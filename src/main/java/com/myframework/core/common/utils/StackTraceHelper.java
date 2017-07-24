package com.myframework.core.common.utils;

public class StackTraceHelper {

	private static final int MAX_STACK_LEN = 20;

	public static String getStackTrace() {

		Throwable ex = new Throwable();
		StackTraceElement[] stackElements = ex.getStackTrace();
		StringBuilder stack = new StringBuilder();

		if (stackElements != null) {
			for (int i = 1; (i < stackElements.length) && (i < MAX_STACK_LEN); i++) {
				stack.append(stackElements[i].getClassName()).append(stackElements[i].getMethodName()).append(" file:")
						.append(stackElements[i].getFileName()).append(":").append(stackElements[i].getLineNumber())
						.append("\n");
			}
		}

		return stack.toString();
	}

	public static String getStackMsg(Exception e) {

		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stackArray = e.getStackTrace();
		int n = 0;
		for (int i = 0; i < stackArray.length; i++) {
			StackTraceElement element = stackArray[i];
			sb.append(element.toString() + "\n");
		}
		return sb.toString();
	}

	public static String getSimpleStackMsg(Exception e) {

		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stackArray = e.getStackTrace();
		int no = 0;
		for (int i = 0; i < stackArray.length; i++) {
			StackTraceElement element = stackArray[i];
			sb.append(element.toString() + "\n");

			no++;
			if (no >= 3) {
				break;
			}
		}
		return sb.toString();
	}

	public static String getStackMsg(Throwable e) {

		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stackArray = e.getStackTrace();
		for (int i = 0; i < stackArray.length; i++) {
			StackTraceElement element = stackArray[i];
			sb.append(element.toString() + "\n");
		}
		return sb.toString();
	}

	public static String getArguments(Object[] arguments) {

		if (arguments == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arguments.length; i++) {
			sb.append(" index:").append(i);
			if (arguments[i] == null) {
				sb.append(" value:null");
			} else {
				sb.append(" type:").append(arguments[i].getClass().getName());
				sb.append(" value:").append(arguments[i].toString());
			}

		}

		return sb.toString();
	}

}
