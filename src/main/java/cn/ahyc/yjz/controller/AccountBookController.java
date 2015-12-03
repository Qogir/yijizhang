package cn.ahyc.yjz.controller;

import cn.ahyc.yjz.model.AccountBook;
import cn.ahyc.yjz.model.DictValue;
import cn.ahyc.yjz.service.AccountBookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新建账套. Created by john Hu on 15-9-22. AccountBookController
 */
@Controller
@RequestMapping("/account/book")
public class AccountBookController extends BaseController {

		public AccountBookController() {
				this.pathPrefix = this.pathPrefix + "accountBook/";
		}

		@Resource
		private AccountBookService accountBookService;

		// 新建账套首页
		@RequestMapping(value = ("/main"))
		public String main(Model model) {
				List<DictValue> subjectSystem = accountBookService.selectSubjectSystem();
				model.addAttribute("categories", subjectSystem);
				return view("main");
		}

		// 制度说明页面
		@RequestMapping(value = ("/info"))
		public String info(Model model) {
				return view("info");
		}

		/**
		 * 检查账套名称是否已经存在.
		 *
		 * @param name
		 * @return
		 */
		@RequestMapping("/is/exist")
		@ResponseBody
		public boolean isExist(String name) {
				List<AccountBook> accountBooks = accountBookService.selectAccountBookByName(name);
				return accountBooks.size()>0?true:false;
		}

		/**
		 * 获取账套列表.
		 * @return
		 */
		@RequestMapping("/list")
		@ResponseBody
		public List<AccountBook> listAccountBooks(){
			return accountBookService.selectAllAccountBook();
		}

		/**
		 * 完成新建账套配置
		 *
		 * @param accountBook
		 * @param level2      二级会计科目 level3 三级会计科目
		 * @param level4      四级会计科目 level5 五级会计科目 均指长度设置
		 * @return
		 */
		@RequestMapping(value = ("/complete"))
		@ResponseBody
		public Map<String,Object> complete(AccountBook accountBook, int level2, int level3, int level4, int level5) {
				Map<String,Object> map = new HashMap<String,Object>();
				int level1 = 4;
				//保存业务数据
				Long resultId = accountBookService.createAccountBook(accountBook, level1, level2, level3, level4, level5);
				map.put("resultId", resultId);
				return map;
		}
}
