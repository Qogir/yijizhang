package cn.ahyc.yjz.service;/**
 * UserService
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/10/8
 */

/**
 * Created by sanlli on 15/10/8.
 */
public interface UserService {

		/**
		 * 验证密码是否匹配.
		 * @param password
		 * @return
		 */
		boolean isPassWdMatch(String username,String password);


		/**
		 * 修改密码.
		 * @param username
		 * @param newPassword
		 * @return
		 */
		boolean modifyPasswd(String username,String newPassword);
}
