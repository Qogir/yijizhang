package cn.ahyc.yjz.service.impl;
/**
 * UserServiceImpl
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/10/8
 */

import cn.ahyc.yjz.mapper.extend.UserExtendMapper;
import cn.ahyc.yjz.model.User;
import cn.ahyc.yjz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanlli on 15/10/8.
 */
@Service
public class UserServiceImpl implements UserService {

		@Autowired
		PasswordEncoder passwordEncoder;

		@Autowired
		UserExtendMapper userExtendMapper;


		/**
		 * 验证密码是否匹配.
		 *
		 * @param username
		 * @param password
		 * @return
		 */
		@Override
		public boolean isPassWdMatch(String username, String password) {
				User user = userExtendMapper.selectUserByName(username);
				if(user==null){
						return false;
				}
				return passwordEncoder.matches(password,user.getPassword());
		}

		/**
		 * 修改密码.
		 *
		 * @param username
		 * @param newPassword
		 * @return
		 */
		@Override
		public boolean modifyPasswd(String username, String newPassword) {
				Map map = new HashMap();
				map.put("username",username);
				map.put("password",passwordEncoder.encode(newPassword));
				int result = userExtendMapper.updatePassword(map);
				return result<=0?false:true;
		}
}
