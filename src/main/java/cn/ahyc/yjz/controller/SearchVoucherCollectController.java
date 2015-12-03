package cn.ahyc.yjz.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ahyc.yjz.service.SearchVoucherCollectService;

/**
 * 凭证汇总功能.
 */
@Controller
@RequestMapping("/search/vouchercollect")
public class SearchVoucherCollectController extends BaseController {
		@Autowired
		private SearchVoucherCollectService searchVoucherCollectService;

		public SearchVoucherCollectController() {
				this.pathPrefix = this.pathPrefix + "search/vouchercollect/";
		}

		/**
		 * 初始化页面.
		 *
		 * @return
		 */
		@RequestMapping(value = ("/main"))
		public String main(Model model, String subjectCode, HttpSession session) {
				Map<String, Object> paramList = searchVoucherCollectService.InitParam(session);
				model.addAttribute("startTime", paramList.get("startTime"));
				model.addAttribute("endTime", paramList.get("endTime"));
				return view("main");
		}

		/**
		 * 查看凭证汇总表数据.
		 *
		 * @param startTime
		 * @param endTime
		 * @param voucherWord
		 * @param voucherStartNo
		 * @param voucherEndNo
		 * @return
		 */
		@RequestMapping("/list")
		@ResponseBody
		public List<Map<String, Object>> vouchercollectList(String startTime, String endTime, String voucherWord,
		                                                    Integer voucherStartNo, Integer voucherEndNo, HttpSession session) {
				return searchVoucherCollectService.queryList(startTime, endTime, voucherWord, voucherStartNo, voucherEndNo, session);
		}

		/**
		 * 过滤页面.
		 * @param model
		 * @param startTime
		 * @param endTime
		 * @param voucherWord
		 * @param voucherStartNo
		 * @param voucherEndNo
		 * @return
		 */
		@RequestMapping("/search")
		public String search(Model model, String startTime, String endTime, String voucherWord,
		                     Integer voucherStartNo, Integer voucherEndNo) {
				model.addAttribute("startTime", startTime);
				model.addAttribute("endTime", endTime);
				model.addAttribute("voucherWord", voucherWord);
				model.addAttribute("voucherStartNo", voucherStartNo);
				model.addAttribute("voucherEndNo", voucherEndNo);
				return view("search");
		}

		/**
		 * 过滤请求页面.
		 * @param model
		 * @param startTime
		 * @param endTime
		 * @param voucherWord
		 * @param voucherStartNo
		 * @param voucherEndNo
		 * @return
		 */
		@RequestMapping("/filter")
		public String filter(Model model, String startTime, String endTime, String voucherWord,
		                     Integer voucherStartNo, Integer voucherEndNo) {
				model.addAttribute("startTime", startTime);
				model.addAttribute("endTime", endTime);
				model.addAttribute("voucherWord", voucherWord);
				model.addAttribute("voucherStartNo", voucherStartNo);
				model.addAttribute("voucherEndNo", voucherEndNo);
				return view("main");
		}
}
