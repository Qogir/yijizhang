package cn.ahyc.yjz.util;
/**
 * NetworkUtil
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/9/28
 */

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sanlli on 15/9/28.
 */
public class NetworkUtil {

		/**
		 * 从request获取真实IP
		 * @param request
		 * @return
		 */
		public static String getRemoteHost(HttpServletRequest request){
				String ip = request.getHeader("x-forwarded-for");
				if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
						ip = request.getHeader("Proxy-Client-IP");
				}
				if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
						ip = request.getHeader("WL-Proxy-Client-IP");
				}
				if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
						ip = request.getRemoteAddr();
				}
				return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
		}

}
