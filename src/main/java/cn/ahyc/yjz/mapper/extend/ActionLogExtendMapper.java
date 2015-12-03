package cn.ahyc.yjz.mapper.extend;
/**
 * ActionLogExtendMapper
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/10/14
 */

import cn.ahyc.yjz.mapper.base.ActionLogMapper;
import cn.ahyc.yjz.model.ActionLog;

import java.util.List;
import java.util.Map;

/**
 * Created by sanlli on 15/10/14.
 */
public interface ActionLogExtendMapper extends ActionLogMapper {

		/**
		 * 获取total.
		 * @return
		 */
		int selectCount();

		/**
		 * 分页获取操作日志.
		 * @param map
		 * @return
		 */
		List<ActionLog> actionLoglist(Map map);

}
