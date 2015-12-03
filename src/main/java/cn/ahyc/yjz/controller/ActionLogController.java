package cn.ahyc.yjz.controller;
/**
 * LogController
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/10/10
 */

import cn.ahyc.yjz.dto.Page;
import cn.ahyc.yjz.model.ActionLog;
import cn.ahyc.yjz.service.ActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sanlli on 15/10/10.
 */
@Controller
@RequestMapping("/action/log")
public class ActionLogController extends BaseController {

		@Autowired
		ActionLogService actionLogService;

		public ActionLogController() {
				this.pathPrefix = this.pathPrefix + "actionLog/";
		}

		/**
		 * 查询操作日志页面.
		 * @return
		 */
		@RequestMapping("/main")
		public String main(){
				return view("main");
		}

		/**
		 * 操作日志列表.
		 * @param rows
		 * @param page
		 * @return
		 */
		@RequestMapping("/list")
		@ResponseBody
		public Page<ActionLog> actionLogs(int rows, int page){
				Page<ActionLog> actionLogPage = new Page<ActionLog>(0,new ArrayList<ActionLog>());
				actionLogPage = actionLogService.actionLogList(rows, page);
				return actionLogPage;
		}

}
