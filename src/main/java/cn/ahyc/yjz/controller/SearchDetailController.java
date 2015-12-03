package cn.ahyc.yjz.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ahyc.yjz.mapper.base.AccountSubjectMapper;
import cn.ahyc.yjz.model.AccountSubject;
import cn.ahyc.yjz.model.AccountSubjectExample;
import cn.ahyc.yjz.model.Period;
import cn.ahyc.yjz.service.SearchDetailService;
import cn.ahyc.yjz.util.Constant;

/**
 * 明细账控制器.
 * Created by John Hu on 15-10-14.
 */
@Controller
@RequestMapping("/search/detail")
public class SearchDetailController  extends BaseController{
	public SearchDetailController() {
		this.pathPrefix = this.pathPrefix + "search/detail/";
	}
	@Autowired
	private SearchDetailService searchDetailService;
	@Autowired
	private AccountSubjectMapper accountSubjectMapper;
	/**
	 * 初始化页面.
	 *
	 * @return
	 */
	@RequestMapping(value = ("/main"))
	public String main(Model model,String subjectCode,Integer currentPeriod,HttpSession session) {
		Period period = (Period) session.getAttribute(Constant.CURRENT_PERIOD);
        if(currentPeriod!=null&&!currentPeriod.equals("")){
        	model.addAttribute("currentPeriod", currentPeriod);
        }else{
        	model.addAttribute("currentPeriod", period.getCurrentPeriod());
        }
        model.addAttribute("subjectCode",subjectCode);
      //查找本年利润科目代码
        if(subjectCode!=null&&!subjectCode.equals("")){
	        AccountSubjectExample example=new AccountSubjectExample();
	  		cn.ahyc.yjz.model.AccountSubjectExample.Criteria criteria=example.createCriteria();
	  		criteria.andSubjectCodeEqualTo(Long.parseLong(subjectCode));
	  		criteria.andBookIdEqualTo(period.getBookId());
	  		List<AccountSubject> accountSubject=accountSubjectMapper.selectByExample(example);
	  		if(accountSubject.size()>0){
	  			model.addAttribute("subjectName",accountSubject.get(0).getSubjectName());
	  		}else{
	  			model.addAttribute("subjectName","");
	  		}
        }else{
        	model.addAttribute("subjectName","");
        }
		return view("main");
	}
	
	/**
	 * 调用查询明细账.
	 *
	 * @return
	 */
	@RequestMapping(value = ("/submitNow"))
	@ResponseBody
	public  List<Map> submitNow(String startPeriod,String endPeriod,String subjectCode,HttpSession session) {
		 return searchDetailService.submitNow(startPeriod, endPeriod,subjectCode,session);
	}
}
