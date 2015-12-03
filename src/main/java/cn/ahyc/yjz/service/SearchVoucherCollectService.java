package cn.ahyc.yjz.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

public interface SearchVoucherCollectService {
	/**
	 * 初始化参数
	 * @param session
	 * @return
	 */
	Map<String,Object> InitParam(HttpSession session);
	/**
	 * 查询凭证汇总
	 * @param startTime
     * @param endTime
     * @param voucherWord
     * @param voucherStartNo
     * @param voucherEndNo
     * @param session
	 * @return
	 */
	List<Map<String, Object>> queryList(String startTime, String  endTime,String  voucherWord, 
			 Integer voucherStartNo, Integer voucherEndNo, HttpSession session);
}
