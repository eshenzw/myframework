package com.myframework.core.repeat;

import com.myframework.bean.MyResponse;
import com.myframework.core.repeat.annotation.RepeatCheck;
import com.myframework.util.RespUtil;
import com.myframework.util.StringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author zw
 * @date 2017/11/20
 */
public class RepeatCheckInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(RepeatCheckInterceptor.class);

    private RepeatCheckHandler repeatCheckHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatCheck annotation = method.getAnnotation(RepeatCheck.class);
            if (annotation != null) {
                boolean isCheckToken = annotation.check();
                if (isCheckToken) {
                    String submitToken = StringUtil.valueOf(request.getHeader(RepeatCheckHandler.SUBMIT_TOKEN));
                    if (isRepeatSubmit(submitToken)) {
                        logger.warn("请不要重复提交数据,URL:" + request.getServletPath());
                        MyResponse ret = MyResponse.failed("请不要重复提交数据");
                        RespUtil.responseJson(JSONObject.fromObject(ret).toString());
                        return false;
                    }
                }
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }

    private boolean isRepeatSubmit(String submitToken) {
        if (!repeatCheckHandler.isRepeat(submitToken)) {
            repeatCheckHandler.saveToken(submitToken);
            return false;
        }
        return true;
    }

    public RepeatCheckHandler getRepeatCheckHandler() {
        return repeatCheckHandler;
    }

    public void setRepeatCheckHandler(RepeatCheckHandler repeatCheckHandler) {
        this.repeatCheckHandler = repeatCheckHandler;
    }
}
