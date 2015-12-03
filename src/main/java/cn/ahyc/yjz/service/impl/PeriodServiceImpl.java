package cn.ahyc.yjz.service.impl;
/**
 * PeriodServiceImpl
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/9/28
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ahyc.yjz.mapper.extend.PeriodExtendMapper;
import cn.ahyc.yjz.model.Period;
import cn.ahyc.yjz.service.PeriodService;

/**
 * Created by sanlli on 15/9/28.
 */
@Service
public class PeriodServiceImpl implements PeriodService {

		@Autowired
		private PeriodExtendMapper periodExtendMapper;

		/**
		 * 当然账套ID所对应的当前期.
		 *
		 * @param bookId
		 * @return
		 */
		@Override
		public Period selectCurrentPeriod(Long bookId) {
				return periodExtendMapper.selectCurrentPeriod(bookId);
		}

    /*
     * (non-Javadoc)
     * 
     * @see cn.ahyc.yjz.service.PeriodService#queryPeriod(java.lang.Long)
     */
    @Override
    public Period queryPeriod(Long periodId) {
        return periodExtendMapper.selectByPrimaryKey(periodId);
    }
}
