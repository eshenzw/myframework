/*
 * 作者：zw
 * 
 */
package com.myframework.tags;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.myframework.constant.Constants;
import com.myframework.util.MyToolsUtil;
import com.myframework.util.SpringContextUtil;
import com.myframework.util.StringUtil;

/**
 * 页面输出
 * 
 * @author Administrator
 * 
 */
public class WriteTag extends BaseTagSupport {

	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(this.getClass());

	private String converter;
	private int add;

	public int doStartTag() throws JspException {
		super.doStartTag();
		Object tmp = null;
		Object result = null;
		String objName = this.name;
		if (StringUtil.isEmpty(objName)) {
			objName = Constants.DEFAULT_ATTRIBUTE_NAME;
		}
		try {
			if ("contextPath".equalsIgnoreCase(objName)) {
				result = this.request.getContextPath();
			} else {
				if (!StringUtil.isEmpty(this.property)) {
					objName = objName + "." + this.property;
				}
				tmp = MyToolsUtil.getValueFromObject(this.request, objName);
				if (StringUtil.isEmpty(tmp)) {
					result = "";
				} else {
					result = tmp.toString();
				}
				if (!StringUtil.isEmpty(this.converter)) {
					String[] arr = this.converter.split("[.]");
					if (arr.length != 2) {
						log.error("converter转换的格式不正确！");
					} else {
						result = MyToolsUtil.executeJavaMethod(SpringContextUtil.getBean(arr[0]), arr[1], new Class[] { String.class }, new String[] { result.toString() });
					}
				}
				if (add != 0 && ((String) result).matches("\\d+")) {
					int temp = Integer.parseInt((String) result);
					temp = temp + add;
					result = temp;
				}
				if (StringUtil.isEmpty(result) && !StringUtil.isEmpty(this.defaultValue)) {
					result = this.defaultValue;
				}
			}
			this.out.print(result);
		} catch (Exception e) {
			this.log.error("页面输出出错！", e);
		}
		return EVAL_BODY_INCLUDE;
	}

	public void setConverter(String converter) {
		this.converter = converter;
	}

	public void setAdd(int add) {
		this.add = add;
	}

}
