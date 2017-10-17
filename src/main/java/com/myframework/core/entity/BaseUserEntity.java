package com.myframework.core.entity;

public class BaseUserEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
    public static final String USER_COOKIE_ID = "MyUId";
    public static final String USER_SESSION_ID = "MyUSId";
    public static final String USER_SESSION_BEAN = "MyUSBean";

    private Long userId;
    private String username;
    private String password;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
