package cn.ahyc.yjz.service;/**
 * ActionLogService
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/10/12
 */

import cn.ahyc.yjz.dto.Page;
import cn.ahyc.yjz.model.ActionLog;

import java.util.List;

/**
 * Created by sanlli on 15/10/12.
 */
public interface ActionLogService {

		/**
		 * 记录日志.
		 * @param actionLog
		 */
		void writeActionLog(ActionLog actionLog);

		/**
		 * 操作日志列表.
		 * @param rows
		 * @param page
		 * @return
		 */
		Page<ActionLog> actionLogList(int rows, int page);

}
