package cn.ahyc.yjz.service.impl;
/**
 * ActionLogServiceImpl
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/10/12
 */

import cn.ahyc.yjz.dto.Page;
import cn.ahyc.yjz.mapper.base.ActionLogMapper;
import cn.ahyc.yjz.mapper.extend.ActionLogExtendMapper;
import cn.ahyc.yjz.model.ActionLog;
import cn.ahyc.yjz.service.ActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sanlli on 15/10/12.
 */
@Service
public class ActionLogServiceImpl implements ActionLogService {

		@Autowired
		ActionLogExtendMapper actionLogExtendMapper;

		/**
		 * 记录日志.
		 *
		 * @param actionLog
		 */
		@Override
		public void writeActionLog(ActionLog actionLog) {
				actionLogExtendMapper.insert(actionLog);
		}

		/**
		 * 操作日志列表.
		 *
		 * @param rows
		 * @param page
		 * @return
		 */
		@Override
		public Page<ActionLog> actionLogList(int rows, int page) {
				int start = (page-1)*rows;
				int end = rows;
				Map map = new HashMap();
				map.put("start",start);
				map.put("end",end);
				return new Page(actionLogExtendMapper.selectCount(),actionLogExtendMapper.actionLoglist(map));
		}


}
