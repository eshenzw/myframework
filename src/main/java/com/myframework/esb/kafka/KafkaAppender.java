package com.myframework.esb.kafka;

import java.util.Date;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;

import com.myframework.constant.Constants;
import com.myframework.esb.MessageCenterHandler;
import com.myframework.util.DateUtil;
import com.myframework.util.PropertiesUtil;

public class KafkaAppender<E> extends AppenderBase<E>
{
	String formatStr = "%s [%s] [%s] [%s] %s %s";
	static String appsvr = PropertiesUtil.getProp(Constants.SYSTEM_FILE_PATH, "init.appsvr");

	@Override
	protected void append(E eventObject)
	{
		LoggingEvent le = (LoggingEvent) eventObject;

		String ts = DateUtil.date2String(DateUtil.DATETIME_PATTENT, new Date(le.getTimeStamp()));
		String lll = String.format(formatStr, ts, appsvr, le.getThreadName(), le.getLevel(), le.getLoggerName(),
				le.getMessage());
		try
		{
			MessageCenterHandler.sendSystemLog(le.getThreadName(), lll);
		}
		catch (Exception e)
		{
		}

	}
}
