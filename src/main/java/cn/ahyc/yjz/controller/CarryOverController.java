package cn.ahyc.yjz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ahyc.yjz.model.CompanyCommonValue;
import cn.ahyc.yjz.service.CarryOverService;
import cn.ahyc.yjz.service.CompanyCommonValueService;

/**
 * 结转损益. Created by john Hu on 15-9-22. CarryOverController
 */
@Controller
@RequestMapping("/account/carryOver")
public class CarryOverController extends BaseController {

		@Autowired
		private CarryOverService carryOverService;
		@Autowired
		private CompanyCommonValueService companyCommonValueService;

		public CarryOverController() {
				this.pathPrefix = this.pathPrefix + "carryOver/";
		}

		/**
		 * 初始化页面.
		 *
		 * @return
		 */
		@RequestMapping(value = ("/main"))
		public String main(Model model) {
				return view("main");
		}

		/**
		 * 凭证字.
		 *
		 * @return
		 */
		@RequestMapping(value = ("/category/detail"))
		@ResponseBody
		public List<CompanyCommonValue> getCategoryDetail() {
				List<CompanyCommonValue> categoryDetails = companyCommonValueService.queryListByType(1L);
				return categoryDetails;
		}

		/**
		 * 结转损益.
		 *
		 * @return
		 */
		@RequestMapping(value = ("/complete"))
		@ResponseBody
		public Map<String, String> complete(String summary, String voucherWord, HttpSession session) {
				Map<String, String> map = new HashMap<String, String>();
				//保存业务数据
				String result = carryOverService.CarryoverSubmit(summary, voucherWord, session);
				map.put("result", result);
				return map;
		}
}
