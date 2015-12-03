package cn.ahyc.yjz.mapper.extend;/**
 * LoginHistoryExtendMapper
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/9/28
 */

import cn.ahyc.yjz.mapper.base.LoginHistoryMapper;
import cn.ahyc.yjz.model.LoginHistory;

/**
 * Created by sanlli on 15/9/28.
 */
public interface LoginHistoryExtendMapper extends LoginHistoryMapper {

		/**
		 * 根据Username查询上一次登录成功的历史记录.
		 * @param username
		 * @return
		 */
		LoginHistory selectLastSuccessLoginHistory(String username);


}
