package com.myframework.core.token;

import com.myframework.core.filter.RequestFilter;
import com.myframework.util.DeviceUtil;

import java.util.Collection;
import java.util.Date;

/**
 * Created by zw on 2017/9/11.
 */
public class JwtSubjectInfo {
    private String uid;
    private String username;
    private TokenDevice device;
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

    public TokenDevice getDevice() {
        if (device == null && RequestFilter.getRequest() != null) {
            String platform = TokenDevice.UNKNOWN;
            if (DeviceUtil.isMobileDevice(RequestFilter.getRequest())) {
                if (DeviceUtil.isIOSDevice(RequestFilter.getRequest())) {
                    platform = TokenDevice.IOS;
                } else if (DeviceUtil.isAndroidDevice(RequestFilter.getRequest())) {
                    platform = TokenDevice.ANDROID;
                } else if (DeviceUtil.isWeChat(RequestFilter.getRequest())) {
                    platform = TokenDevice.WEIXIN;
                }
            } else {
                platform = TokenDevice.WEB;
            }
            device = new TokenDevice(platform);
        }
        return device;
    }

    public void setDevice(TokenDevice device) {
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
