package cn.ahyc.yjz.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ahyc.yjz.model.AccountBook;
import cn.ahyc.yjz.model.Period;
import cn.ahyc.yjz.service.AccountSubjectService;
import cn.ahyc.yjz.service.CashierService;
import cn.ahyc.yjz.util.Constant;


/**
 * 结账. Created by john Hu on 15-10-08. CashierController
 */
@Controller
@RequestMapping("/account/cashier")
public class CashierController  extends BaseController{
	
	public CashierController() {
		this.pathPrefix = this.pathPrefix + "cashier/";
	}
	
	@Autowired
	private AccountSubjectService accountSubjectService;
	@Autowired
	private CashierService cashierService;

	/**
	 * 初始化页面.
	 *
	 * @return
	 */
	@RequestMapping(value = ("/main"))
	public String main(Model model,HttpSession session) {
		Period period=(Period) session.getAttribute(Constant.CURRENT_PERIOD);
		AccountBook accountBook=(AccountBook) session.getAttribute(Constant.CURRENT_ACCOUNT_BOOK);
		int overFlag =accountBook.getOverFlag();
		int currentPeriod=period.getCurrentPeriod();
		model.addAttribute("overFlag", overFlag);
		model.addAttribute("currentPeriod", currentPeriod);
		return view("main");
	}

		/**
		 * 检查账套名称是否已经存在.
		 * @param session
		 * @return
		 */
	@RequestMapping("/is/balance")
	@ResponseBody
	public Boolean isBalance(HttpSession session) {
		Map<String,Object> map = new HashMap<String,Object>();
		Period period=(Period) session.getAttribute(Constant.CURRENT_PERIOD);
		Long bookId=period.getBookId();
		Long category_subject_code=0L;
		map=accountSubjectService.balance(bookId, category_subject_code);
		return (Boolean) map.get("isBalance");
	}

		/**
		 * 结账.
		 * @param session
		 * @return
		 */
	@RequestMapping("/submit")
	@ResponseBody
	public Map<String,Object> cashierSubmit(HttpSession session) {
		Map<String,Object> map = new HashMap<String,Object>();
		Period period=(Period) session.getAttribute(Constant.CURRENT_PERIOD);
		AccountBook accountBook=(AccountBook) session.getAttribute(Constant.CURRENT_ACCOUNT_BOOK);
		Long resultId=cashierService.cashierSubmit(period,accountBook);
		map.put("resultId", resultId);
		return map;
	}
}
