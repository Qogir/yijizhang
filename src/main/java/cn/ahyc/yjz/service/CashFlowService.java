package cn.ahyc.yjz.service;

import cn.ahyc.yjz.model.Period;

import java.util.List;
import java.util.Map;

/**
 * Created by Joey Yan on 15-11-3.
 */
public interface CashFlowService {
    List<Map> cashflows(Period period, Integer currentPeriod);
}
