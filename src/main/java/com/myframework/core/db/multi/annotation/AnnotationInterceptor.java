package com.myframework.core.db.multi.annotation;

import com.myframework.core.db.multi.DataSourceHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zw
 */
@Aspect
@Component
public class AnnotationInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        ForceMaster forceMaster = methodInvocation.getMethod().getAnnotation(ForceMaster.class);
        ForceClose forceClose = methodInvocation.getMethod().getAnnotation(ForceClose.class);
        Transactional transactional = methodInvocation.getMethod().getAnnotation(Transactional.class);
        if (forceMaster != null && forceMaster.value()) {
            DataSourceHolder.setMaster(true);
        }
        if (forceClose != null && forceClose.value()) {
            DataSourceHolder.setForceClose(true);
        }
        if (transactional != null && (transactional.propagation() == Propagation.REQUIRED || transactional.propagation() == Propagation.REQUIRES_NEW)) {
            DataSourceHolder.setTransaction(true);
        }
        try {
            return methodInvocation.proceed();
        } finally {
            if (forceMaster != null) {
                DataSourceHolder.removeMaster();
            }
            if (forceClose != null) {
                DataSourceHolder.removeForceClose();
            }
            if (transactional != null && (transactional.propagation() == Propagation.REQUIRED || transactional.propagation() == Propagation.REQUIRES_NEW)) {
                DataSourceHolder.removeTransaction();
            }
        }
    }
