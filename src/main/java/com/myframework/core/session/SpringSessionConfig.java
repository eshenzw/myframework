package com.myframework.core.session;

import com.myframework.util.StringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.myframework.constant.Constants;
import com.myframework.util.PropertiesUtil;

/**
 * Created by zw on 2015/9/9.
 */
@EnableRedisHttpSession
public class SpringSessionConfig {
    public final static String SESSION_REDIS_ENABLE = "session.redis.enable";
    public final static String SESSION_REDIS_HOST = "session.redis.host";
    public final static String SESSION_REDIS_PORT = "session.redis.port";

    @Bean
    public JedisConnectionFactory connectionFactory() {
        if (StringUtil.toBoolean(PropertiesUtil.getInstance(Constants.SYSTEM_FILE_PATH).getValue(SpringSessionConfig.SESSION_REDIS_ENABLE))) {
            JedisConnectionFactory connection = new JedisConnectionFactory();
            connection.setPort(Integer.valueOf(PropertiesUtil.getInstance(Constants.SYSTEM_FILE_PATH).getValue(SESSION_REDIS_PORT)));
            connection.setHostName(PropertiesUtil.getInstance(Constants.SYSTEM_FILE_PATH).getValue(SESSION_REDIS_HOST));
            return connection;
        } else {
            return null;
        }
    }
}
