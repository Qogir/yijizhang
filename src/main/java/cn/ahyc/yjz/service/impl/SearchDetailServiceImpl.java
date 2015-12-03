package cn.ahyc.yjz.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ahyc.yjz.mapper.extend.SubjectBalanceExtendMapper;
import cn.ahyc.yjz.mapper.extend.VoucherDetailExtendMapper;
import cn.ahyc.yjz.model.AccountBook;
import cn.ahyc.yjz.model.Period;
import cn.ahyc.yjz.service.SearchDetailService;
import cn.ahyc.yjz.util.Constant;
@Service
public class SearchDetailServiceImpl implements SearchDetailService{
	
	@Autowired
	private SubjectBalanceExtendMapper subjectBalanceExtendMapper;
	@Autowired
	private VoucherDetailExtendMapper voucherDetailExtendMapper;
	/**
	 * 查询明细账now.
	 * @param bookId
	 * @return
	 */
	@Override
	public List<Map> submitNow(String startPeriod, String endPeriod, String subjectCode,HttpSession session) {
		Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
		AccountBook accountBook=(AccountBook) session.getAttribute(Constant.CURRENT_ACCOUNT_BOOK);
		Long bookId=period.getBookId();
		List<Map> returnList=new ArrayList<Map>();
		//查询期初余额
		Map startMap=startBalance(bookId,startPeriod,subjectCode,accountBook);
		if(startMap==null){
			return returnList;
		}
		returnList.add(startMap);
		//查询明细账
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("bookId", bookId);
		param.put("subjectCode", subjectCode.concat("%"));
		param.put("startPeriod", Integer.parseInt(startPeriod));
		param.put("endPeriod", Integer.parseInt(endPeriod));
		List<Map> queryList=voucherDetailExtendMapper.searchDetail(param);
		BigDecimal tmp=(BigDecimal)startMap.get("balance");
		BigDecimal creditTmp=new BigDecimal(0.00);
		BigDecimal debitTmp=new BigDecimal(0.00);
		if(queryList.size()>0){
			for(Map detailMap:queryList){
				creditTmp=detailMap.get("credit")==null?new BigDecimal(0.00):(BigDecimal) detailMap.get("credit");
				debitTmp =detailMap.get("debit")==null? new BigDecimal(0.00):(BigDecimal) detailMap.get("debit");
				if(detailMap.get("voucherId").toString()!=null&&!detailMap.get("voucherId").toString().equals("")){
					if(startMap.get("direction").equals("借")){
						tmp= tmp.add(debitTmp).subtract(creditTmp);
					}else{
						tmp=tmp.subtract(debitTmp).add(creditTmp);
					}
				}
				detailMap.put("balance", tmp); 
				detailMap.put("direction",startMap.get("direction"));
				returnList.add(detailMap);
			}
		}
		return returnList;
	}
	/**
	 * 查询期初余额.
	 * @param 
	 * @return
	 */
	public Map startBalance(Long bookId,String startPeriod,String subjectCode,AccountBook accountBook){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("bookId", bookId);
		param.put("currentPeriod", startPeriod);
		param.put("subjectCode", subjectCode);
		Map<String, Object> startBalance=subjectBalanceExtendMapper.selectBalanceBySubjectCode(param);
		if(startBalance==null){
			return null;
		}
		Map startMap = new HashMap();
		int year =accountBook.getInitYear();
		startPeriod=startPeriod.length()<2?"0"+startPeriod:startPeriod;
		startMap.put("voucherTime", year+"-"+startPeriod+"-01");
		startMap.put("voucherWord", "");
		startMap.put("summary", "期初余额");
		startMap.put("subjectName", "");
		startMap.put("credit", "");
		startMap.put("debit", "");
		startMap.put("direction", startBalance.get("direction_value"));
		startMap.put("balance", startBalance.get("balance"));
		return  startMap;
	}
}
