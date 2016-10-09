package com.myframework.esb;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import com.myframework.util.SpringBeanManager;

public final class ESB
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ESB.class);

	public static final Map<String, Object> execute(String beanName, Map<String, Object> in)
	{
		try
		{
			IOperation iop = (IOperation) SpringBeanManager.getBean(beanName);
			if (iop != null)
			{
				return iop.execute(in);
			}
		}
		catch (NoSuchBeanDefinitionException e)
		{
			LOGGER.info("呼叫ESB终止，bean: {}", beanName);
		}

		return null;
	}

}
