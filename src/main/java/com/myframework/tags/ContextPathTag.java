/*
 * 作者：zw
 * 
 */
package com.myframework.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

public class ContextPathTag extends BaseTagSupport {

	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(getClass());

	public int doStartTag() throws JspException {
		super.doStartTag();
		try {
			this.out.print(this.request.getContextPath());
		} catch (IOException e) {
			log.error("", e);
		}
		return EVAL_BODY_INCLUDE;
	}
}
