package com.myframework.core.token;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;

/**
 * Created by zw on 2017/7/20.
 */
public class TokenDevice implements Device {

    public static final String WEB = "web";
    public static final String ANDROID = "android";
    public static final String IOS = "ios";
    public static final String WEIXIN = "weixin";
    public static final String TABLET = "tablet";
    public static final String UNKNOWN = "unknown";

    private String platform;

    public TokenDevice() {
    }

    public TokenDevice(String platform) {
        this.platform = platform;
    }

    @Override
    public boolean isNormal() {
        return WEB.equals(platform);
    }

    @Override
    public boolean isMobile() {
        return ANDROID.equals(platform) || IOS.equals(platform);
    }

    @Override
    public boolean isTablet() {
        return TABLET.equals(platform);
    }

    @Override
    public DevicePlatform getDevicePlatform() {
        if (ANDROID.equals(platform)) {
            return DevicePlatform.ANDROID;
        } else if (IOS.equals(platform)) {
            return DevicePlatform.IOS;
        } else {
            return DevicePlatform.UNKNOWN;
        }
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
