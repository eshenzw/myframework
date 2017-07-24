package com.myframework.core.common.thread;

import java.util.Stack;

/**
 * 
 * created by zw
 *
 * @param <T>
 */
public class ExtendAnnotationThreadLocal<T> {
	
	private ThreadLocal<Stack<T>> extendThreadLocal = new ThreadLocal<Stack<T>>();
	
	
	public T get() {
		
		Stack<T> stack = extendThreadLocal.get();
		
		if(stack == null || stack.size() ==0) {
			return null;
		}
		
		
		return stack.peek();
	}
	
	
	/**
	 * 
	 * @param value
	 */
	public void set(T value) {
		
		Stack<T> stack = extendThreadLocal.get();
		
		if(stack == null) {
			stack = new Stack<T>();
		}
		
		stack.push(value);
		
		extendThreadLocal.set(stack);
	}
	
	/**
	 * 全部清除
	 */
	public void remove() {
		extendThreadLocal.remove();
	}
	
	/**
	 * 只清理一个
	 */
	public void removeOne() {
		
		Stack<T> stack = extendThreadLocal.get();
		
		if(stack == null || stack.size() == 0) {
			return;
		}
		
		stack.pop();

		extendThreadLocal.set(stack);
	}
}