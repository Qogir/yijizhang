package cn.ahyc.yjz.service.impl;

import cn.ahyc.yjz.mapper.extend.PeriodExtendMapper;
import cn.ahyc.yjz.mapper.extend.VoucherDetailExtendMapper;
import cn.ahyc.yjz.model.Period;
import cn.ahyc.yjz.model.PeriodExample;
import cn.ahyc.yjz.service.SearchVoucherService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joey Yan on 15-10-12.
 */
@Service
public class SearchVoucherServiceImpl implements SearchVoucherService {

    @Autowired
    private VoucherDetailExtendMapper voucherDetailExtendMapper;

    @Autowired
    private PeriodExtendMapper periodExtendMapper;

    @Override
    public List<Map> vouchers(Period period, String keyword) {

        Map param = new HashMap();
        param.put("periodId", period.getId());

        /**
         * 关键字查询.
         */
        if (!StringUtils.isEmpty(keyword)) {
            param.put("keyword", "%".concat(keyword).concat("%"));
            return voucherDetailExtendMapper.selectVoucherDetailForSearchByKeyWord(param);
        }

        return voucherDetailExtendMapper.selectVoucherDetailForSearch(param);
    }

    @Override
    public List<Period> periods(Period period) {

        PeriodExample example = new PeriodExample();
        PeriodExample.Criteria criteria = example.createCriteria();
        criteria.andBookIdEqualTo(period.getBookId());

        return periodExtendMapper.selectByExample(example);
    }

    @Override
    public void set(Period period) throws Exception {
        voucherDetailExtendMapper.resetVoucherNo(period.getId());
    }
}
