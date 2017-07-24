package com.myframework.core.alarm.monitor.interceptor;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.myframework.core.alarm.EventTypeEnum;
import com.myframework.core.alarm.event.ServiceAccessEvent;
import com.myframework.core.alarm.event.exclude.AlarmExcludeHandler;
import com.myframework.core.alarm.event.extend.ServiceAccessBizExtend;
import com.myframework.core.alarm.monitor.RequestStatistic;
import com.myframework.core.alarm.monitor.ThreadProfile;

/**
 * 
 * service access 拦截器抽象类，因为要适配 struts2 和 spring，所以吧具体的逻辑抽象出来
 * 
 * created by zw
 *
 */
public abstract class AbstractThreadProfileInterceptor
		implements ApplicationEventPublisherAware, ApplicationContextAware {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	// 多少毫秒会打印异常日志，超过2s
	private int threshold = 2000;

	// public final static String HTTP_HEADER_SERVICE_NAME =
	// "X-365-SERVICE-NAME";

	// 服务的方法名
	private final static ThreadLocal<String> serviceNameThreadLocal = new ThreadLocal<>();

	private final static ThreadLocal<Boolean> excludeThreadLocal = new ThreadLocal<>();

	// 本服务内的方法名称
	private final static ThreadLocal<String> localServiceNameThreadLocal = new ThreadLocal<>();

	// publisher
	private ApplicationEventPublisher publisher;

	private static int theadProfile = 2000;

	private ServiceAccessBizExtend serviceAccessBizExtend;

	private AlarmExcludeHandler alarmExcludeHandler;

	public AlarmExcludeHandler getAlarmExcludeHandler() {
		return alarmExcludeHandler;
	}

	public void setAlarmExcludeHandler(AlarmExcludeHandler alarmExcludeHandler) {
		this.alarmExcludeHandler = alarmExcludeHandler;
	}

	public ServiceAccessBizExtend getServiceAccessBizExtend() {
		return serviceAccessBizExtend;
	}

	public void setServiceAccessBizExtend(ServiceAccessBizExtend serviceAccessBizExtend) {
		this.serviceAccessBizExtend = serviceAccessBizExtend;
	}

	/**
	 * 获取服务名，请求的连接地址
	 * 
	 * @return
	 */
	private String getRequestName(HttpServletRequest request) {

		return request.getRequestURI();
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
		theadProfile = threshold;
	}

	private void setupServiceName(final HttpServletRequest request) {

		// String serviceName = request.getHeader(HTTP_HEADER_SERVICE_NAME);
		//
		// // 头部没有服务名称，则认为是来自gateway的请求
		// if (StringUtils.isEmpty(serviceName)) {
		// serviceName = getRequestName(request);
		// }

		String serviceName = getRequestName(request);

		serviceNameThreadLocal.set(serviceName);

		// local
		localServiceNameThreadLocal.set(request.getRequestURI());
	}

	/**
	 * 获取服务名称
	 * 
	 * @return 服务名称
	 */
	public static String getServiceName() {
		String name = serviceNameThreadLocal.get();
		return StringUtils.isEmpty(name) ? "unknown" : name;
	}

	/**
	 * 获取本地服务名称
	 * 
	 * @return 服务名称
	 */
	public static String getLocalServiceName() {
		String name = localServiceNameThreadLocal.get();
		return StringUtils.isEmpty(name) ? "unknown" : name;
	}

	/**
	 * 请求执行前之后执行的操作
	 * 
	 * @param request
	 */
	protected void doPreHandle(HttpServletRequest request) {

		try {
			
			// 是否在排除列表中，比如导入导出这种的，堆栈长，执行慢，统计浪费时间，忽略之
			boolean isInExclude = isInServiceAccessExclude(request);

			excludeThreadLocal.set(isInExclude);
			
			// 设置服务名称
			this.setupServiceName(request);

			// 需要记录请求处理时长以及堆栈
			ThreadProfile.start(request.getRequestURI(), theadProfile);

			ThreadProfile.enter(this.getClass().getName(), "doPreHandle");

		} catch (Exception e) {
			logger.error("AbstractThreadProfileInterceptor execute doPreHandle failed!", e);
		}

		// //数据库操作,默认从master,slave分摊查询操作, 与database.yml中读readOnlyInSlave一起判断
		// DatabaseRouting.masterOrSlave();
	}

	/**
	 * 请求执行完之后执行的操作
	 * 
	 * @param request
	 * @param response
	 */
	protected void doAfterCompletion(HttpServletRequest request, HttpServletResponse response) {

		try {

			/** remove all threadlocal */
			String srcServiceName = this.getServiceName();
			
			ThreadProfile.exit();

			/** do stat */
			Pair<String, Long> duration = ThreadProfile.stop();
			if (duration != null) {

//				// 统计响应时长分布
//				RequestStatistic.put(request.getRequestURI(), duration.getRight().intValue());

				// 服务调用的统计
				ServiceAccessEvent event = new ServiceAccessEvent(request.getRequestURI(), duration.getRight(),
						response.getStatus(), duration.getLeft());
				event.setSrcService(srcServiceName);

				// 如果有实现扩展属性的接口实现
				if (serviceAccessBizExtend != null) {
					serviceAccessBizExtend.setBizExtendAttrbute(event, request);
				}

				this.publisher.publishEvent(event);
			}

		} catch (Exception e) {
			logger.error("AbstractThreadProfileInterceptor execute doAfterCompletion failed!", e);

		} finally {
			clearThreadLocal();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		initBizExtend(applicationContext);
	}

	/**
	 * 初始化biz service acess 扩展
	 * 
	 * @param applicationContext
	 */
	private void initBizExtend(ApplicationContext applicationContext) {

		Map<String, ServiceAccessBizExtend> list = applicationContext.getBeansOfType(ServiceAccessBizExtend.class);

		// 判断是否为空
		if (MapUtils.isEmpty(list) || list.size() > 1) {

			logger.warn("AbstractThreadProfileInterceptor ServiceAccessBizExtend instance abnormal!instance.size()="
					+ (MapUtils.isEmpty(list) ? 0 : list.size()));
			return;
		}

		for (Entry<String, ServiceAccessBizExtend> entry : list.entrySet()) {
			serviceAccessBizExtend = entry.getValue();
		}
	}

	/**
	 * 是否在排除列表
	 * 
	 * @param request
	 * @return
	 */
	private boolean isInServiceAccessExclude(HttpServletRequest request) {

		try {
			Map<String, Object> map = alarmExcludeHandler
					.getExcludeMapByEventType(EventTypeEnum.EVENT_TYPE_SERVICE_ACCESS);

			if (MapUtils.isEmpty(map)) {
				return false;
			}

			String requestPath = request.getRequestURI();

			Object obj = map.get(requestPath);

			return obj != null;

		} catch (Exception e) {
			logger.error("AbstractThreadProfileInterceptor isInServiceAccessExclude failed", e);

		}

		return false;
	}

	/**
	 * 清理threadlocal
	 */
	private void clearThreadLocal() {

		localServiceNameThreadLocal.remove();
		excludeThreadLocal.remove();
		serviceNameThreadLocal.remove();

	}
	
	
	/**
	 * 是否在免打堆栈的列表
	 * @return
	 */
	public static boolean isInServiceAccessExclude() {

		return excludeThreadLocal.get() == null?false:excludeThreadLocal.get();
	}

}