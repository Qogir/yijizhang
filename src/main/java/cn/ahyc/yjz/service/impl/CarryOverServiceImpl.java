package cn.ahyc.yjz.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ahyc.yjz.dto.SubjectBalanceDto;
import cn.ahyc.yjz.mapper.base.AccountSubjectMapper;
import cn.ahyc.yjz.mapper.extend.AccountSubjectExtendMapper;
import cn.ahyc.yjz.mapper.extend.SubjectBalanceExtendMapper;
import cn.ahyc.yjz.model.AccountBook;
import cn.ahyc.yjz.model.AccountSubject;
import cn.ahyc.yjz.model.AccountSubjectExample;
import cn.ahyc.yjz.model.Period;
import cn.ahyc.yjz.model.Voucher;
import cn.ahyc.yjz.model.VoucherDetail;
import cn.ahyc.yjz.service.CarryOverService;
import cn.ahyc.yjz.service.VoucherService;
import cn.ahyc.yjz.util.Constant;

@Service
public class CarryOverServiceImpl implements  CarryOverService{
	
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private AccountSubjectExtendMapper accountSubjectExtendMapper;
	@Autowired
	private SubjectBalanceExtendMapper subjectBalanceExtendMapper;
	@Autowired
	private AccountSubjectMapper accountSubjectMapper;
	 /**
	 * 结转损益.
	 *
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public String CarryoverSubmit(String summary, String voucherWord,HttpSession session) {
		String result="";
		//取出session 当前期与当前账套id
		Period period=(Period) session.getAttribute(Constant.CURRENT_PERIOD);
		AccountBook accountBook=(AccountBook) session.getAttribute(Constant.CURRENT_ACCOUNT_BOOK);
		Integer initYear=accountBook.getInitYear();
		Integer currentPeriod=period.getCurrentPeriod();
		Long bookId=period.getBookId();
		Long periodId=period.getId();
		//查找损益其中一个会计科目代码
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("bookId", bookId);
		Map<String, Object> subjectCode=accountSubjectExtendMapper.getSubjectCodeByRoot(param);
		String startCode=subjectCode.get("subject_code").toString().substring(0,1);
		//根据期id与账套id以及损益类开始数字查找所有科目的余额
		param.put("periodId", periodId);
		param.put("startCode",startCode);
		List<SubjectBalanceDto> datas=subjectBalanceExtendMapper.selectProfitAndLoss(param);
		//设置记账凭证详细表数据
		//记账凭证无数据
		if(datas.size()==0){
			result="各损益类科目期末余额已经不需要结转,没有生成转账凭证。";
			return result;
		}
		List<VoucherDetail> details=generateDetails(datas,period,summary);
		//记账凭证组织数据
		if(details.size()==0){
			result="各损益类科目期末余额已经不需要结转,没有生成转账凭证。";
			return result;
		}
		//设置记账凭证主表数据
		Voucher voucher=new Voucher();
		voucher.setVoucherWord(voucherWord);
		Date voucherTime=new Date();
		voucher.setVoucherTime(voucherTime);
		voucher.setBillNum(0);
		voucher.setPeriodId(periodId);
        voucher.setCarryOver(1);// 1:结账凭证
        voucher.setVoucherTime(getLastDayOfMonth(initYear,currentPeriod));
		//保存凭证
		String reslutSuccess=voucherService.save(voucher, details);
		result="已生成一张转账凭证，凭证字号为："+reslutSuccess;
		return result;
	}
	public List<VoucherDetail> generateDetails(List<SubjectBalanceDto> datas,Period period,String summary){
		List<VoucherDetail> details=new ArrayList<VoucherDetail>();
		int i=0;
		BigDecimal totalDebit=new BigDecimal(0.00);
		BigDecimal totalCredit=new BigDecimal(0.00);
		for(SubjectBalanceDto data:datas){
			VoucherDetail detail=new VoucherDetail();
			if(i==0){
				detail.setSummary(summary);
				i++;
			}
			detail.setSubjectCode(data.getSubjectCode());
			if(data.getDirection()==1){
				detail.setCredit(data.getTerminalDebitBalance());
				totalDebit=totalDebit.add(data.getTerminalDebitBalance());
			}else{
				detail.setDebit(data.getTerminalCreditBalance());
				totalCredit=totalCredit.add(data.getTerminalCreditBalance());
			}
			details.add(detail);
		}
		//查找本年利润科目代码
		AccountSubjectExample example=new AccountSubjectExample();
		cn.ahyc.yjz.model.AccountSubjectExample.Criteria criteria=example.createCriteria();
		criteria.andSubjectNameEqualTo("本年利润");
		criteria.andBaseFlagEqualTo(0);
		criteria.andBookIdEqualTo(period.getBookId());
		List<AccountSubject> accountSubject=accountSubjectMapper.selectByExample(example);
		//设置本年利润计算
		if(accountSubject.size()>0){
			if(totalDebit.compareTo(new BigDecimal(0.00))!=0){
				VoucherDetail detailDebit=new VoucherDetail();
				detailDebit.setSubjectCode(accountSubject.get(0).getSubjectCode());
				detailDebit.setDebit(totalDebit);
				details.add(detailDebit);
			}
			if(totalCredit.compareTo(new BigDecimal(0.00))!=0){
				VoucherDetail detailCredit=new VoucherDetail();
				detailCredit.setSubjectCode(accountSubject.get(0).getSubjectCode());
				detailCredit.setCredit(totalCredit);
				details.add(detailCredit);
			}
		}else{
			return new ArrayList<VoucherDetail>();
		}
		return details;
	}
	 /**
     * 获取某月的最后一天
     * @Title:getLastDayOfMonth
     * @Description:
     * @param:@param year
     * @param:@param month
     * @param:@return
     * @return:String
     * @throws
     */
    public static Date getLastDayOfMonth(int year,int month){
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        return cal.getTime();
    }
}
