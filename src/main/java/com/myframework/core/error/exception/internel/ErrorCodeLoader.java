package com.myframework.core.error.exception.internel;


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;



/**
 * 读取配置文件，加载message和code的映射关系
 * 
 * created by zw
 *
 */
public class ErrorCodeLoader implements InitializingBean{

    private final Logger logger = LoggerFactory.getLogger(ErrorCodeLoader.class);
    
    private static  ErrorCodeLoader loader;
    
    
    private Map<String, Object> bizErrorMap = new HashMap<String, Object>();
    
    private Map<String,String> errorCodeMap = new HashMap<String,String>();

    public Map<String, Object> getBizErrorMap() {
		return bizErrorMap;
	}


	public void setBizErrorMap(Map<String, Object> bizErrorMap) {
		this.bizErrorMap = bizErrorMap;
	}


	public Map<String, String> getErrorCodeMap() {
		return errorCodeMap;
	}


	public void setErrorCodeMap(Map<String, String> errorCodeMap) {
		this.errorCodeMap = errorCodeMap;
	}


	/**
     * 初始化error code. 由spring 初始化
     */
    public void init() {

        logger.info("start to init error code info");
        
        loader = new ErrorCodeLoader();
 
        if(MapUtils.isEmpty(bizErrorMap)) {
        	return;
        }
        
        for(Entry<String,Object> record:bizErrorMap.entrySet()) {
        	
        	Map<String,String> obj = (Map<String, String>) record.getValue();
        	String key = record.getKey();

        	//获取code对应的message信息，key前面带了 []符号，需要去掉
        	errorCodeMap.put(key.substring(1, key.length()-1), obj.get("message"));
        	
        }
        

        logger.info("init error code success. Biz error size {}",
                this.errorCodeMap.size());
        
        loader.errorCodeMap = this.errorCodeMap;

    }


	@Override
	public void afterPropertiesSet() throws Exception {

		init();
	}

    
    
    /**
     * 根据异常码，获取异常信息
     * @param errorCode
     */
    public static String getErrorMessageByCode(int errorCode)  {
    	
    	String message = loader.errorCodeMap.get(String.valueOf(errorCode));
//    
    	return message;
    }
}    