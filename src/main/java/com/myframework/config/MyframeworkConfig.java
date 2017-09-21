package com.myframework.config;

import com.myframework.constant.Constants;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Created by zw on 2017/9/21.
 */
public class MyframeworkConfig {
    /**
     * 配置
     */
    private static Configuration configuration;

    /**
     * 获取配置信息
     */
    private static Configuration getConfig() {
        try {
            if (configuration == null) {
                configuration = new PropertiesConfiguration(Constants.MY_FRAMEWORK_FILE_PATH);
            }
            return configuration;
        } catch (ConfigurationException e) {
            return null;
        }
    }

    public static String getValue(String key, String defaultVal) {
        return getConfig() != null ? getConfig().getString(key, defaultVal) : defaultVal;
    }
}
