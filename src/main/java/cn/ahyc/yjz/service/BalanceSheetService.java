package cn.ahyc.yjz.service;

import cn.ahyc.yjz.model.AccountBook;
import cn.ahyc.yjz.model.BalanceSheet;
import cn.ahyc.yjz.model.Period;

import java.util.List;
import java.util.Map;

/**
 * Created by Joey Yan on 15-10-15.
 */
public interface BalanceSheetService {
    List<Map> balanceSheets(Period period, AccountBook accountBook, Long code);

    List<BalanceSheet> balanceSheetsByParentCode(Long parentCode);

    void saveExp(BalanceSheet balanceSheet) throws Exception;
}
