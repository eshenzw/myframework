/*
 * 作者：zw
 * 
 */
package com.myframework.tags;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

public class SetAttributeTag extends BaseTagSupport {

	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(this.getClass());

	public int doStartTag() throws JspException {
		super.doStartTag();
		try {
			this.request.setAttribute(this.beanName, this.getValuesFromParam());
		} catch (Exception e) {
			log.error("", e);
		}
		return EVAL_BODY_INCLUDE;
	}
}
