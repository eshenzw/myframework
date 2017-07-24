package com.myframework.core.alarm.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class RequestStatistic {

	//打印运行日志
	private static final Logger runLogger = LoggerFactory.getLogger(RequestStatistic.class);

	/**
	 * 记录url的请求次数，key为url
	 */
	private static final Map<String,int[]> requests = new LinkedHashMap<String,int[]>();

	/**
	 *时间区间，用于统计每个区间的请求次数，得到响应时长的分布
	 */
	private static final String SECTION_1 = " [0-50] ";
	private static final String SECTION_2 = " [50-200] ";
	private static final String SECTION_3 = " [200-500] ";
	private static final String SECTION_4 = " [500-] ";

	private static final int section2 = 50 ;
	private static final int section3 = 200 ;
	private static final int section4 = 500 ;

	/**
	 * 启动线程，每分钟打印日志，重置统计区间的取值
	 */
	static{
//		Thread thread = new Thread(new Runnable() {
//			public void run() {
//				while(true) {
//					try {
//						//每分钟输出一次
//						Thread.sleep(60 * 1000);
//						RequestStatistic.dump();
//					} catch (Exception e) {
//						//do nothing
//						//catch all exception
//					}
//				}
//			}
//		});
//		thread.start();
	}
	
	/**
	 * 记录统计信息
	 * @param requestURI  请求的完整url
	 * @param duration   请求的响应延时
	 */
	public static synchronized void put(String requestURI, int duration){

		int[] sectionArr = requests.get(requestURI);
		if( sectionArr == null ){
			sectionArr = new int[4];
			requests.put(requestURI,sectionArr) ;
		}

		if( duration < section2 ){
			sectionArr[0]++ ;  //[0,50]
		}else if( duration < section3 ){
			sectionArr[1]++ ;  //[50,200]
		}else if( duration < section4 ){
			sectionArr[2]++ ;  //[200,500]
		}else{
			sectionArr[3]++ ;  //[500,]
		}
		//
	}

	/**
	 * 打印统计结果
	 */
	public static synchronized void dump(){


		runLogger.info("**************************** Begin dump request statistic info ****************************");

		for(Map.Entry<String,int[]>  entry : requests.entrySet() ){
			int[] sectionArr = entry.getValue() ;
			if( sectionArr == null ){
				return ;
			}
			StringBuffer sb = new StringBuffer();

			sb.append("[request time monitor][url:")
					.append(entry.getKey()).append("]")
					.append(SECTION_1).append(sectionArr[0])
					.append(SECTION_2).append(sectionArr[1])
					.append(SECTION_3).append(sectionArr[2])
					.append(SECTION_4).append(sectionArr[3]);

			runLogger.info(sb.toString());
		}

		runLogger.info("**************************** End dump request statistic info ****************************");
		/**
		 * dump完成后清理map记录
		 */
		//requests.clear();
	}

}
