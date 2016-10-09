/*
 * 
 */
package com.myframework.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.myframework.annotation.SType;
import com.myframework.annotation.ServiceType;

/**
 * SpringBean管理器，获取和管理Spring的BeanFactory
 * 
 * @DateTime: 2011-3-4
 * @author author
 * @version 1.0
 */
public class SpringBeanManager
{
    /***/
    public static final Log log = LogFactory.getLog(SpringBeanManager.class);

    /** 获取service的名字集合 */
    private static List serviceNames;

    /**
     * 通过Spring的beanName或beanId,获取bean的实例.
     * 
     * @param beanName SpringContext 的xml中定义的id或name名字
     * @return object
     */
    public static Object getBean(String beanName)
    {
        return SpringContextUtil.getContext().getBean(beanName);
    }

    /**
     * 获取所有Bean.
     * 
     * @return the all beans
     */
    public static String[] getAllBeans()
    {
        return SpringContextUtil.getContext().getBeanDefinitionNames();
    }

    /**
     * 获取所有Service
     * 
     * @return list
     */
    public static List getAllServiceNames()
    {
        // 不用每次都查询说有Service一次记录
        if (serviceNames == null)
        {
            serviceNames = new ArrayList();
            for (String serviceName : getAllBeans())
            {
                if (serviceName.indexOf("Service") != -1)
                {
                    try
                    {
                        Object o = SpringContextUtil.getContext().getBean(serviceName);
                        if (o.getClass().getSuperclass().isAnnotationPresent(ServiceType.class))
                        {
                            ServiceType ss = o.getClass().getSuperclass().getAnnotation(ServiceType.class);
                            // 判定类型是外部访问型才能储存
                            if (SType.SANS == ss.value())
                            {
                                serviceNames.add(serviceName);
                            }
                        }
                    } catch (Exception e)
                    {
                        // 忽略所有错误。
                    }
                }
            }
        }
        return serviceNames;
    }
    
}
