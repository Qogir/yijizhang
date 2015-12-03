package cn.ahyc.yjz.service.impl;
/**
 * LoginHistoryServiceImpl
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/9/28
 */

import cn.ahyc.yjz.mapper.extend.LoginHistoryExtendMapper;
import cn.ahyc.yjz.model.LoginHistory;
import cn.ahyc.yjz.service.LoginHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sanlli on 15/9/28.
 */
@Service
public class LoginHistoryServiceImpl implements LoginHistoryService {

		@Autowired
		private LoginHistoryExtendMapper loginHistoryExtendMapper;

		/**
		 * 保存登录历史.
		 *
		 * @param loginHistory
		 */
		@Override
		public int saveLoginHistory(LoginHistory loginHistory) {
				return loginHistoryExtendMapper.insert(loginHistory);
		}

		/**
		 * 更新登录历史.
		 *
		 * @param loginHistory
		 */
		@Override
		public int updateLoginHistory(LoginHistory loginHistory) {
				return loginHistoryExtendMapper.updateByPrimaryKey(loginHistory);
		}

		/**
		 * 根据Username查询上一次登录成功的历史记录.
		 *
		 * @param username
		 * @return
		 */
		@Override
		public LoginHistory selectLastSuccessLoginHistory(String username) {
				return loginHistoryExtendMapper.selectLastSuccessLoginHistory(username);
		}

}
