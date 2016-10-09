package com.myframework.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回码的枚举类型
 * 
 * @author zw <br />
 * @date 2013-08-15
 */
public enum ReturnCodeEnum {

	/**
	 * 操作成功
	 */
	CODE_OK("操作成功"),

	/**
	 * 操作失败
	 */
	CODE_ERROR("操作失败"),

	/**
	 * 未知错误
	 */
	UNKNOWN_ERROR("未知错误"),

	/**
	 * 验证码错误
	 */
	CHECK_CODE_ERROR("验证码错误"), LOGIN_ID_IS_EMPTY("用户ID为空"), LOGIN_NAME_IS_EMPTY(
			"登录名为空"), LOGIN_PWD_IS_EMPTY("密码为空"),

	/**
	 * 登录失败，用户名或密码错误
	 */
	LOGIN_ERROR("登录失败，用户名或密码错误"), LOGOUT_ERROR("注销失败"),
	
	/**
	 * 用户被锁定，不能登录
	 */
	USER_LOCK("用户被锁定"),
	
	/**
	 * 用户不存在
	 */
	USER_IS_NOT_EXIST("用户不存在"),

	/**
	 * 登录名重复
	 */
	LOGIN_NAME_EXIST("登录名重复"),

	/**
	 * 用户名称重复
	 */
	USER_NAME_EXIST("用户名称重复"),

	/**
	 * 该用户创建过其他用户，请先删除子用户，再删除该用户
	 */
	USER_HAVE_CHILD_USER("该用户创建过其他用户，请先删除子用户，再删除该用户"),

	/**
	 * 该用户创建过角色，请先删除该用户创建过的角色，再删除该用户
	 */
	USER_HAVE_CHILD_ROLE("该用户创建过角色，请先删除该用户创建过的角色，再删除该用户"),

	/**
	 * 原始密码错误
	 */
	OLD_PASSWORD_ERROR("原始密码错误"),

	/**
	 * 菜单名称重复
	 */
	MENU_NAME_EXISTS("菜单名称重复"),

	/**
	 * 菜单有子菜单，请先删除子菜单，再删除该菜单
	 */
	MENU_HAVE_CHILD_MENU("菜单有子菜单，请先删除子菜单，再删除该菜单"),

	/**
	 * 该菜单已经有角色绑定，请先解除该菜单与角色绑定，再删除该菜单
	 */
	MENU_HAVE_ROLE_BIND("该菜单已经有角色绑定，请先解除该菜单与角色绑定，再删除该菜单"),

	/**
	 * 角色名称重复
	 */
	ROLE_NAME_EXISTS("角色名称重复"),

	/**
	 * 角色【{0}】已经被其他用户使用，请先解除用户与角色的绑定，再删除角色
	 */
	ROLE_HAVE_USER_BIND("角色【{0}】已经被其他用户使用，请先解除用户与角色的绑定，再删除角色"),

	/**
	 * 你的账号密码已经过期,请更改密码！
	 */
	PASSWORD_TIME_OUT("你的账号密码已经过期,请更改密码！"),

	/**
	 * 你所设置的密码使用过于频繁，请重新设置！
	 */
	PASSWORD_USED_FREQUENTLY("你所设置的密码使用过于频繁，请重新设置！"),

	/**
	 * 您的密码过于简单，请重新设置！
	 */
	PASSWORD_IS_SIMPLE("您的密码过于简单，请重新设置！"),

	/**
	 * 用户身份证号重复
	 */
	PERSONAL_CARD_NO_EXISTS("用户身份证号重复"),

	/**
	 * 该地区包含子地区
	 */
	REGION_HAVE_CHILD("该地区包含子地区"),

	RANK_HAVE_USER_BIND("头衔有用户正在使用"),

	/**
	 * 上传图片是否重复check
	 */
	PHOTO_SEARCH_ERROR("参数错误"), PHOTO_EXISTS("图片已存在"), PHOTO_NOT_EXISTS("图片未存在"),

	/**
	 * 上传图片是否重复check
	 */
	PHOTO_UPLOAD_ERROR("参数错误"), PHOTO_UPLOAD_OK("上传成功"), PHOTO_UPLOAD_NG("上传失败"),

	/**
	 * 原图提醒消息获取
	 */
	MMSG_SEARCH_ERROR("参数错误"), MMSG_SEARCH_OK("检索有记录"), MMSG_SEARCH_NORECORD(
			"检索无记录"),
			
	/**
	 * 需要拷贝到电信服务器的图片列表获取
	 */
	PHOTO_COPY_SEARCH_ERROR("参数错误"), PHOTO_COPY_SEARCH_OK("检索有记录"), PHOTO_COPY_SEARCH_NORECORD(
			"检索无记录"),

	/**
	 * 原图提醒消息状态更新
	 */
	MMSG_UPDATE_ERROR("参数错误"), MMSG_UPDATE_OK("更新成功"), MMSG_UPDATE_NG("更新失败"),
	SECTION_NAME_IS_EXISTS("版块名称重复"),


	/**
	 * 图片上传状态更新
	 */
	PHOTO_UPDATE_ERROR("参数错误"), PHOTO_UPDATE_OK("更新成功"), PHOTO_UPDATE_NG("更新失败");
	;
	
	private ReturnCodeEnum(String text) {
		this.text = text;
	}

	private String text;

	public String getText() {
		return this.text;
	}

	/**
	 * 返回该枚举的map格式
	 * 
	 * @return 返回该枚举的map格式
	 */
	public static Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < values().length; i++) {
			map.put(values()[i].toString(), values()[i].getText());
		}
		return map;
	}
}
