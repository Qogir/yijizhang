package cn.ahyc.yjz.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

public interface SearchDetailService {
	/**
	 * 查询明细账now.
	 * @param bookId
	 * @return
	 */
	 List<Map> submitNow(String startPeriod, String endPeriod,String subjectCode,HttpSession session);
}
