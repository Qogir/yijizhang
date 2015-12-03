package cn.ahyc.yjz.service;

import cn.ahyc.yjz.model.Period;

import java.util.List;
import java.util.Map;

/**
 * Created by Joey Yan on 15-10-12.
 */
public interface SearchVoucherService {


    /**
     * 凭证查询：根据期查询出所有品种.
     *
     * @param period
     * @param keyword
     * @return
     */
    List<Map> vouchers(Period period, String keyword);

    List<Period> periods(Period period);

    /**
     * 凭证整理.
     *
     * @param period
     */
    void set(Period period) throws Exception;
}
