package cn.ahyc.yjz.mapper.extend;

import cn.ahyc.yjz.mapper.base.PeriodMapper;
import cn.ahyc.yjz.model.Period;


public interface PeriodExtendMapper extends PeriodMapper{

    /**
     * 当然账套ID所对应的当前期.
     * @param bookId
     * @return
     */
    Period selectCurrentPeriod(Long bookId);
    /**
     * 插入当前期返回ID.
     * @param Period
     * @return Id
     */
    Long insertReturnId(Period period);
}