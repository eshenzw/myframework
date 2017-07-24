package com.myframework.core.alarm.event.handler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import com.myframework.core.alarm.EventReporter;
import com.myframework.core.alarm.event.CommonEvent;
import com.myframework.core.common.utils.LocalhostIpFetcher;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用事件处理 Created by zhangjun
 */
public abstract class AbstractEventHandler<T extends CommonEvent> implements ApplicationListener<T> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	// tag: 本地IP和context
	private final String localIP = LocalhostIpFetcher.fetchLocalIP();

	protected final String DB_EVENT = "db_monitor";

	protected final String SERVER_EVENT = "server_monitor";

	// pring setter
	private String tag_context;

	private EventReporter eventReporter;

	@Override
	public void onApplicationEvent(T event) {

		logger.debug("receive event:{}", event);

		if (event == null || StringUtils.isEmpty(event.getName()) || this.isIgnore(event)) {
			logger.debug("event is empty of ignore");
			return;
		}

		// 参数
		Map<String, Object> params = new HashMap<>();
		params.put("ip", localIP);
		this.addArgs(event, params);

		// tags
		Map<String, String> tags = new HashMap<>();
		
		tags.put("event", event.getName());

		//如果传入了url，根据url分析属于哪个模块的
		if(StringUtils.isEmpty(event.getName())) {
		    tags.put("context", tag_context);
		} else {
		    tags.put("context", parseModuleFromRequestUrl(event.getName()));
		}
		
		// tags.put("host", event.getName());
		this.addTags(event, tags);
		// 保证所有的tag value都不能为空
		for (Map.Entry<String, String> tagEntry : tags.entrySet()) {
			String key = tagEntry.getKey();
			String value = tagEntry.getValue();
			if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
				logger.warn("can not find tag value at: {}", event);
				return;
			}
		}

		// catalog
		final String catalog = this.getCatalog(event);

		this.eventReporter.report(this.getDatabase(), catalog, tags, params);

		// System.out.println("catalog="+catalog+",tags="+tags+",params="+params);

	}

	/**
	 * 获取catalog
	 *
	 * @param event
	 * @return catalog
	 */
	abstract String getCatalog(final T event);

	/**
	 * 设置参数
	 *
	 * @param args
	 *            args
	 */
	protected void addArgs(final T event, final Map<String, Object> args) {
	}

	/**
	 * 设置tags
	 *
	 * @param tags
	 *            tags
	 */
	protected void addTags(final T event, final Map<String, String> tags) {
	}

	/**
	 * 保存到那个数据库中
	 */
	protected String getDatabase() {
		return SERVER_EVENT;
	}

	/**
	 * 忽略某个事件
	 *
	 * @param event
	 *            事件
	 * @return 是否忽略这个事件
	 */
	protected boolean isIgnore(final T event) {

		return false;
	}

	// ******************** spring call this **************************
	public void setEventReporter(EventReporter eventReporter) {
		this.eventReporter = eventReporter;
	}

	public void setTag_context(String tag_context) {
		this.tag_context = tag_context;
	}

	/**
	 * 根据请求的url，定位当前请求属于哪个模块
	 * 
	 * @param requestUrl
	 * @return
	 */
	public String parseModuleFromRequestUrl(String requestUrl) {
		// app下的模块,请求模式 /app/xx/..
		if (requestUrl.startsWith("/app/")) {

			return spliteAndGetModule("app", requestUrl);
			// sysapp下的模块,请求模式 /sysapp/xx/..
		} else if (requestUrl.startsWith("/sysapp/")) {
			return spliteAndGetModule("sysapp", requestUrl);

			// sysapp下的模块,请求模式 /baseapp/xx/..
		} else if (requestUrl.startsWith("/baseapp/")) {
			return spliteAndGetModule("baseapp", requestUrl);

		} else {
			return "platform";
		}

	}

	/**
	 * 
	 * @param prefix
	 * @param url
	 * @return
	 */
	private String spliteAndGetModule(String prefix, String url) {

		StringBuilder sb = new StringBuilder();

		String[] args = url.split("/");

		if (args.length > 3) {
			sb.append(args[2]);
		} else {
			sb.append("unknow(" + url + ")");
		}

		return sb.toString();
	}
}
