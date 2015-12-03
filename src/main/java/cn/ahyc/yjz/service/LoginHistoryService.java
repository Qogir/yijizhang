package cn.ahyc.yjz.service;/**
 * LoginService
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/9/28
 */

import cn.ahyc.yjz.model.LoginHistory;

/**
 * Created by sanlli on 15/9/28.
 */
public interface LoginHistoryService {

		/**
		 * 保存登录历史.
		 * @param loginHistory
		 */
		int saveLoginHistory(LoginHistory loginHistory);

		/**
		 * 更新登录历史.
		 * @param loginHistory
		 */
		int updateLoginHistory(LoginHistory loginHistory);

		/**
		 * 根据Username查询上一次登录成功的历史记录.
		 * @param username
		 * @return
		 */
		LoginHistory selectLastSuccessLoginHistory(String username);

}
