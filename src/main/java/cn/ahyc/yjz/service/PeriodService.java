package cn.ahyc.yjz.service;/**
 * PeriodService
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/9/28
 */

import cn.ahyc.yjz.model.Period;

/**
 * Created by sanlli on 15/9/28.
 */
public interface PeriodService {

		/**
		 * 当然账套ID所对应的当前期.
		 * @param bookId
		 * @return
		 */
		Period selectCurrentPeriod(Long bookId);

    /**
     * 查询凭证对应的期间
     * 
     * @param periodId
     * @return
     */
    Period queryPeriod(Long periodId);

}
