package cn.ahyc.yjz.service;/**
 * AccountBookService
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/9/23
 */

import java.util.List;

import cn.ahyc.yjz.model.AccountBook;
import cn.ahyc.yjz.model.DictValue;

/**
 * Created by sanlli on 15/9/23.
 */
public interface AccountBookService {

		/**
		 * 创建账套
		 *
		 * @param accountBook
		 * @throws Exception
		 */
		Long createAccountBook(AccountBook accountBook, int... level);

		/**
		 * 根据ID查询账套.
		 *
		 * @param id
		 * @return
		 */
		AccountBook selectAccountBookById(Long id);

		/**
		 * 根据账套名称查询账套.
		 *
		 * @param name
		 * @return
		 */
		List<AccountBook> selectAccountBookByName(String name);

		/**
		 * 查询账套列表.
		 *
		 * @return
		 */
		List<AccountBook> selectAllAccountBook();

		/**
		 * 查询科目体系数据
		 *
		 * @return
		 */
		List<DictValue> selectSubjectSystem();

		/**
		 * 查询最新的账套.
		 *
		 * @return
		 */
		AccountBook selectLatestAccountBook();

}
