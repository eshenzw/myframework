package com.myframework.core.token;

import org.springframework.mobile.device.Device;

import java.util.Collection;
import java.util.Date;

/**
 * Created by zw on 2017/9/11.
 */
public class JwtSubjectInfo {
    private String uid;
    private String username;
    private Device device;
    private Date lastPasswordReset;
    private Collection<String> auths;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Date getLastPasswordReset() {
        return lastPasswordReset;
    }

    public void setLastPasswordReset(Date lastPasswordReset) {
        this.lastPasswordReset = lastPasswordReset;
    }

    public Collection<String> getAuths() {
        return auths;
    }

    public void setAuths(Collection<String> auths) {
        this.auths = auths;
    }
}
