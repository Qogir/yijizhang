package cn.ahyc.yjz.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ahyc.yjz.mapper.extend.AccountSubjectExtendMapper;
import cn.ahyc.yjz.model.Period;
import cn.ahyc.yjz.service.SearchVoucherCollectService;
import cn.ahyc.yjz.util.Constant;

@Service
public class SearchVoucherCollectServiceImpl implements SearchVoucherCollectService{

	@Autowired
	private AccountSubjectExtendMapper accountSubjectExtendMapper;
	/**
	 * 初始化参数
	 * @param session
	 * @return
	 */
	@Override
	public Map<String,Object> InitParam(HttpSession session) {
		Map<String,Object> param=new HashMap<String,Object>();
		Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
		String startTime=getStartTime(period.getStartTime());
		String endTime=getEndTime(period.getStartTime());
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		return param;
	}
	/**
	 * 取月份第一天
	 * @param startTime
	 * @return
	 */
	private String getStartTime(Date startTime){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String s = df.format(startTime);
		StringBuffer str = new StringBuffer().append(s);
		return str.toString();
	}
	/**
	 * 取月份最后一天
	 * @param startTime
	 * @return
	 */
	private String getEndTime(Date startTime){
		Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = df.format(cal.getTime());
		StringBuffer str = new StringBuffer().append(s);
        return str.toString();
	}
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
	@Override
	public List<Map<String, Object>> queryList(String startTime, String  endTime,String  voucherWord, 
			 Integer voucherStartNo, Integer voucherEndNo, HttpSession session){
		Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
		Long bookId=period.getBookId();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("bookId", bookId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("voucherWord", voucherWord);
		params.put("voucherStartNo", voucherStartNo);
		params.put("voucherEndNo", voucherEndNo);
		return accountSubjectExtendMapper.searchVoucherCollect(params);
	}
}
