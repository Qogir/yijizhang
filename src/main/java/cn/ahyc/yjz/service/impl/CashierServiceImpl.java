package cn.ahyc.yjz.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ahyc.yjz.mapper.base.AccountBookMapper;
import cn.ahyc.yjz.mapper.base.AccountSubjectMapper;
import cn.ahyc.yjz.mapper.base.PeriodMapper;
import cn.ahyc.yjz.mapper.extend.AccountBookExtendMapper;
import cn.ahyc.yjz.mapper.extend.AccountSubjectExtendMapper;
import cn.ahyc.yjz.mapper.extend.PeriodExtendMapper;
import cn.ahyc.yjz.mapper.extend.SubjectBalanceExtendMapper;
import cn.ahyc.yjz.mapper.extend.SubjectLengthExtendMapper;
import cn.ahyc.yjz.model.AccountBook;
import cn.ahyc.yjz.model.AccountSubject;
import cn.ahyc.yjz.model.AccountSubjectExample;
import cn.ahyc.yjz.model.Period;
import cn.ahyc.yjz.service.CashierService;

@Service
public class CashierServiceImpl implements CashierService{

	@Autowired
	private AccountSubjectMapper accountSubjectMapper;
	@Autowired
	private SubjectBalanceExtendMapper subjectBalanceExtendMapper;
	@Autowired
	private PeriodMapper periodMapper;
	@Autowired
	private PeriodExtendMapper periodExtendMapper;
	@Autowired
	private AccountBookMapper accountBookMapper;
	@Autowired
	private AccountBookExtendMapper accountBookExtendMapper;
	@Autowired
	private SubjectLengthExtendMapper subjectLengthExtendMapper;
	@Autowired
	private AccountSubjectExtendMapper accountSubjectExtendMapper;
	 /**
	 * 结账
	 *
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Long cashierSubmit(Period period,AccountBook accountBook) {
		Long bookId=period.getBookId();
		Long periodId=period.getId();
		int currentPeriod=period.getCurrentPeriod();
		Long resultId=bookId;
		//结账先锁定表数据(会计科目表)
		AccountSubject accountSubject = new AccountSubject();
		accountSubject.setEndFlag(1);
		AccountSubjectExample accountSubjectExample=new AccountSubjectExample();
		cn.ahyc.yjz.model.AccountSubjectExample.Criteria criteria = accountSubjectExample.createCriteria();
		criteria.andBookIdEqualTo(bookId);
		accountSubjectMapper.updateByExampleSelective(accountSubject, accountSubjectExample);
		//判断是否年末结账，如果是年末结账则新建账套数据，当前账套锁定
		if(currentPeriod==12){//年末结账
			//锁定账套表
			AccountBook accountBook1=new AccountBook();
			accountBook1.setId(bookId);
			accountBook1.setOverFlag(1);
			accountBook1.setBookName(accountBook.getBookName()+"_"+dateToString(period.getStartTime()).substring(2, 4)+"年结");
			accountBookMapper.updateByPrimaryKeySelective(accountBook1);
			//新生成账套数据并设置返回id为新建账套id
			Long newBookId=createNewAccountBook(period,accountBook);
			resultId=newBookId;
			//锁定年末结账期表数据并新生成一条数据
			Long newPeriodId=operateYearPeriod(period,accountBook,newBookId);
			//更新科目余额表数据
			operateSubjectBalance(newBookId,newPeriodId,periodId,true);
		}else{//期末结账
			//锁定期表并新生成一条数据
			Long newPeriodId=operatePeriod(period);
			//更新科目余额表数据
			operateSubjectBalance(bookId,newPeriodId,periodId,false);
		}
		return resultId;
	}
	//科目余额表数据处理
	public void operateSubjectBalance(Long bookId,Long newPeriodId,Long periodId,Boolean isYearEnd){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("bookId", bookId);
		param.put("newPeriodId", newPeriodId);
		param.put("periodId" , periodId);
		if(isYearEnd==true){
			param.put("isYearEnd" , isYearEnd);
		}
		subjectBalanceExtendMapper.insertNextPeriodSubjectBalance(param);
	}
	//锁定年末结账期表数据并新生成一条数据
	public Long operateYearPeriod(Period period,AccountBook accountBook,Long newBookId){
		//先将当前的期表数据更新
		Period period1=new Period();
		period1.setEndFlag(1);
		period1.setEndTime(new Date());
		period1.setId(period.getId());
		periodMapper.updateByPrimaryKeySelective(period1);
		//再生成新一期表数据
		String  InitYear=String.valueOf(Integer.parseInt(dateToString(period.getStartTime()).substring(0, 4))+1);
		Date startTime = generateStartTime(InitYear,"01");
		Period period2 = new Period();
		period2.setStartTime(startTime);
		period2.setCurrentPeriod(1);
		period2.setFlag(1);
		period2.setBookId(newBookId);
		period2.setEndFlag(0);
		periodExtendMapper.insertReturnId(period2);
		return period2.getId();
	}
	//创建新的账套,copy对应的所有数据
	public Long createNewAccountBook(Period period,AccountBook accountBook){
		AccountBook accountBook1=new AccountBook();
		accountBook1.setBookName(accountBook.getBookName());
		accountBook1.setMoneyCode(accountBook.getMoneyCode());
		accountBook1.setMoneyName(accountBook.getMoneyName());
		String  InitYear=String.valueOf(Integer.parseInt(dateToString(period.getStartTime()).substring(0, 4))+1);
		accountBook1.setStartTime(generateStartTime(InitYear,"01"));
		accountBook1.setInitYear(Integer.parseInt(InitYear));
		accountBook1.setInitPeriod(1);
		accountBook1.setCompanyName(accountBook.getCompanyName());
		accountBook1.setCompanyId(accountBook.getCompanyId());
		accountBook1.setDictValueId(accountBook.getDictValueId());
		accountBook1.setOverFlag(0);
		accountBook1.setLastYearId(accountBook.getId());
		accountBookExtendMapper.insertSelectiveReturnId(accountBook1);
		//复制科目代码长度数据
		copySubjectLength(period.getBookId(),accountBook1.getId());
		//复制会计科目代码数据
		copyAccountSubject(period.getId(),period.getBookId(),accountBook1.getId());
		return accountBook1.getId();
	}
	//复制会计科目代码数据
	public void copyAccountSubject(Long periodId,Long oldBookId,Long newBookId){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("oldBookId", oldBookId);
		param.put("newBookId", newBookId);
		param.put("periodId" , periodId);
		accountSubjectExtendMapper.copyAccountSubject(param);
	}
	//复制科目代码长度数据
	public void copySubjectLength(Long oldBookId,Long newBookId){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("oldBookId", oldBookId);
		param.put("newBookId", newBookId);
		subjectLengthExtendMapper.copySubjectLength(param);
	}
	//期表数据处理
	public Long operatePeriod(Period period){
		//先将当前的期表数据更新
		Period period1=new Period();
		period1.setFlag(0);
		period1.setEndFlag(1);
		period1.setEndTime(new Date());
		period1.setId(period.getId());
		periodMapper.updateByPrimaryKeySelective(period1);
		//再生成新一期表数据
		Date startTime = generateStartTime(dateToString(period.getStartTime()).substring(0, 4),String.valueOf(period.getCurrentPeriod()+1));
		Period period2 = new Period();
		period2.setStartTime(startTime);
		period2.setCurrentPeriod(period.getCurrentPeriod()+1);
		period2.setFlag(1);
		period2.setBookId(period.getBookId());
		period2.setEndFlag(0);
		periodExtendMapper.insertReturnId(period2);
		return period2.getId();
	}
	//生成日期,字符串转化成日期
	public Date generateStartTime(String InitYear,String InitPeriod) {
		String str = InitYear + "-" + InitPeriod + "-01";
		Date date = null;
		try {
				date = DateUtils.parseDate(str,"yyyy-MM-dd");
		} catch (ParseException e) {
				e.printStackTrace();
		}
		return date;
	}
    //日期转换成字符串处理
	public String dateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        return  sdf.format(date);  
	}
}
