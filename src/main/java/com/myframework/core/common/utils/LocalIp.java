package com.myframework.core.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * 通过UDP获取本机的IP地址
 * 
 */
public class LocalIp {

    private static String  localIp = LocalhostIpFetcher.fetchLocalIP();

    //do not new me
    private LocalIp(){

    }

    /**
     * 获取本机IP地址
     * @return 本机IP地址
     */
    public static String getLocalIp()
    {
        return  localIp;
    }
    
    
    
	
  /**
   * 获取访问IP
   * @param request
   * @return
   */
  public static String getRealIp(HttpServletRequest request) {
      String ip = request.getHeader("x-forwarded-for");
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("Proxy-Client-IP");
      }
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("WL-Proxy-Client-IP");
      }
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getRemoteAddr();
      }
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("http_client_ip");
      }
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("HTTP_X_FORWARDED_FOR");
      }
      // 如果是多级代理，那么取第一个ip为客户ip
      if (ip != null && ip.indexOf(",") != -1) {
          ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
      }
      return ip;
  }
    

}
