/*
 * 作者：zw
 * 
 */
package com.myframework.tags;

import javax.servlet.jsp.JspException;

/**
 * 循环标签
 * 
 * @author Administrator
 * 
 */
public class LoopTag extends BaseTagSupport {

	private static final long serialVersionUID = 1L;
	private int times = 0;

	// Set方法设值
	public void setTimes(int times) {
		this.times = times;
	}

	public int doStartTag() throws JspException {
		super.doStartTag();
		if (this.times > 0) {
			this.times--;
			return EVAL_BODY_INCLUDE;
		} else {
			return SKIP_BODY;
		}
	}

	public int doAfterBody() throws JspException {
		if (this.times > 0) {
			this.times--;
			// 表示双从标签开始输入
			return EVAL_BODY_AGAIN;
		}
		// 表示结束，忽略标签内部的内容
		return SKIP_BODY;
	}

}