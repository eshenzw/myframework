package com.myframework.extend.ViewResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author zw
 * @date 2017/11/6
 */
public class ViewNameExtensionViewResolver implements ViewResolver, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(ViewNameExtensionViewResolver.class);

    private int order = 0;

    Map<String, ViewResolver> viewResolvers = new HashMap<String, ViewResolver>();

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        String viewExtension = StringUtils.getFilenameExtension(viewName);
        if (StringUtils.isEmpty(viewExtension)) {
            return null;
        }

        ViewResolver viewResolver = viewResolvers.get(viewExtension);
        if (viewResolver != null) {
            View view = viewResolver.resolveViewName(viewName, locale);
            if (logger.isDebugEnabled()) {
                logger.debug("Returning [" + view + "] based on view extension '"
                        + viewExtension + "'");
            }
            return view;
        }
        if (logger.isWarnEnabled()) {
            logger.warn("No view found with view extension[" + viewExtension + "]; returning null");
        }
        return null;
    }

    public void setViewResolvers(Map<String, ViewResolver> viewResolvers) {
        this.viewResolvers.putAll(viewResolvers);
    }

    public Map<String, ViewResolver> getViewResolvers() {
        return viewResolvers;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
