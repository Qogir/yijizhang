package cn.ahyc.yjz.service.impl;

import cn.ahyc.yjz.mapper.base.BalanceSheetMapper;
import cn.ahyc.yjz.mapper.extend.PeriodExtendMapper;
import cn.ahyc.yjz.mapper.extend.SubjectBalanceExtendMapper;
import cn.ahyc.yjz.model.*;
import cn.ahyc.yjz.service.BalanceSheetService;
import com.googlecode.aviator.AviatorEvaluator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joey Yan on 15-10-15.
 */
@Service
public class BalanceSheetServiceImpl implements BalanceSheetService {

    private Logger logger = LoggerFactory.getLogger(BalanceSheetServiceImpl.class);

    @Autowired
    private BalanceSheetMapper balanceSheetMapper;

    @Autowired
    private SubjectBalanceExtendMapper subjectBalanceExtendMapper;

    @Autowired
    private PeriodExtendMapper periodExtendMapper;


    private Map mappingRelationship = new HashMap();

    @Autowired
    private void initMapping() {

        mappingRelationship.put(".DC", "initial_credit_balance");
        mappingRelationship.put(".JC", "initial_debit_balance");
        mappingRelationship.put(".JF", "period_debit_occur");
        mappingRelationship.put(".DF", "period_credit_occur");
        mappingRelationship.put(".JL", "year_debit_occur");
        mappingRelationship.put(".DL", "year_credit_occur");
        mappingRelationship.put(".JY", "terminal_debit_balance");
        mappingRelationship.put(".DY", "terminal_credit_balance");
        mappingRelationship.put(".SY", "profit_loss_occur_amount");
        mappingRelationship.put(".LY", "profit_loss_total_occur_amount");

    }


    @Override
    public List<Map> balanceSheets(Period period, AccountBook accountBook, Long code) {

        List list = new ArrayList();
        Map map;

        BalanceSheetExtExample balanceSheetExample = new BalanceSheetExtExample();
        balanceSheetExample.andCodeFLike(code.toString().concat("%"));
        balanceSheetExample.setOrderByClause("CAST(CODE AS CHAR)");

        //获取资产负债（只有表达式）
        List<BalanceSheet> balanceSheets = balanceSheetMapper.selectByExample(balanceSheetExample);

        //根据当前期获取科目余额表信息
        Map subjectBalanceMap = this.subjectBalances(period.getId());

        //获取该账套起始期间期科目余额信息
        Map firstPeriodSubjectBalanceMap = null;
        Long firstPeriodId = null;

        //起始期间和当前期间一样，不用重复计算
        if (accountBook.getInitPeriod() == period.getCurrentPeriod()) {
            firstPeriodSubjectBalanceMap = subjectBalanceMap;
            firstPeriodId = period.getId();
        } else {
            PeriodExample periodExample = new PeriodExample();
            PeriodExample.Criteria criteria = periodExample.createCriteria();
            criteria.andBookIdEqualTo(period.getBookId());
            criteria.andCurrentPeriodEqualTo(accountBook.getInitPeriod());
            List<Period> periods = periodExtendMapper.selectByExample(periodExample);
            if (!periods.isEmpty()) {
                firstPeriodId = periods.get(0).getId();
                firstPeriodSubjectBalanceMap = this.subjectBalances(firstPeriodId);
            }
        }

        //解析公式.
        for (BalanceSheet balanceSheet : balanceSheets) {

            String periodEndExp = balanceSheet.getPeriodEndExp();
            String yearBeginExp = balanceSheet.getYearBeginExp();

//            logger.info("subjectBalanceMap={}", subjectBalanceMap.toString());
//            logger.info("firstPeriodSubjectBalanceMap={}", firstPeriodSubjectBalanceMap.toString());

            Object periodEnd = StringUtils.isEmpty(periodEndExp) ? 0 : parseAndCalcu(subjectBalanceMap, firstPeriodSubjectBalanceMap, periodEndExp, period.getId(), firstPeriodId);
            Object yearBegin = StringUtils.isEmpty(yearBeginExp) ? 0 : parseAndCalcu(subjectBalanceMap, firstPeriodSubjectBalanceMap, yearBeginExp, period.getId(), firstPeriodId);
            map = new HashMap();
            map.put("id", balanceSheet.getId());
            map.put("name", balanceSheet.getName());
            map.put("level", balanceSheet.getLevel());
            map.put("needSum", balanceSheet.getNeedSum());
            map.put("periodEndExp", periodEndExp);
            map.put("yearBeginExp", yearBeginExp);
            map.put("periodEnd", periodEnd);
            map.put("yearBegin", yearBegin);
            list.add(map);
        }
        return list;
    }

    @Override
    public List<BalanceSheet> balanceSheetsByParentCode(Long parentCode) {
        BalanceSheetExample balanceSheetExample = new BalanceSheetExample();
        BalanceSheetExample.Criteria criteria = balanceSheetExample.createCriteria();
        criteria.andParentCodeEqualTo(parentCode);
        return balanceSheetMapper.selectByExample(balanceSheetExample);
    }

    @Override
    public void saveExp(BalanceSheet balanceSheet) throws Exception {
        this.balanceSheetMapper.updateByPrimaryKeySelective(balanceSheet);
    }

    /**
     * =<1101>.JC
     * =<1101:1211>.JC
     * =<1101>.JC@1
     * =<1101>.JC@(2001,0)
     * =<1101>.JC + <1101>.JC@(2001,0) * (<1101>.JC+()<1101>.JC)
     *
     * @param subjectBalanceMap
     * @param firstPeriodSubjectBalanceMap
     * @param express
     * @param periodId
     * @param firstPeriodId
     * @return
     */
    private Object parseAndCalcu(Map subjectBalanceMap, Map firstPeriodSubjectBalanceMap, String express, Long periodId, Long firstPeriodId) {

        if (StringUtils.isEmpty(express)) {
            return 0;
        }

        Map storeMap = new HashMap();
        String replaceStr = "x"; //

        String newExpress = express.replaceAll("[\\=\" \"]", ""); //去掉=号 <1101:1109>.JC + <1102>.C
        String expStr = newExpress; //<1101>.JC + <1102>.C
        expStr = expStr.replaceAll("[\\\" \\\"=\\\\+\\-/*/]", "&"); //<1101>.JC&<1102>.C
        String[] exps = expStr.split("&"); //["<1101>.JC", "<1102>.C"]
        String result = "";

        for (String exp : exps) { //<1101>.JC

            replaceStr += replaceStr; //xx
            newExpress = newExpress.replace(exp, replaceStr); //xx + <1102>.C

            if (exp.contains("@(")) { //计算其他年数据
                return 0;
            } else if (exp.contains("@1")) { //计算本年其他期数据.
                exp = exp.replace("@1", "");
                if (firstPeriodSubjectBalanceMap == null) {
                    return 0;
                } else {
                    if (exp.contains(":")) {  //<1001:1009>.JC
                        this.parseMultiCode(storeMap, exp, replaceStr, firstPeriodId);
                    } else {
                        storeMap.put(replaceStr, firstPeriodSubjectBalanceMap.getOrDefault(exp, 0)); //xx
                    }
                }
            } else {
                if (exp.contains(":")) {  //<1001:1009>.JC
                    this.parseMultiCode(storeMap, exp, replaceStr, periodId);
                } else {
                    storeMap.put(replaceStr, subjectBalanceMap.getOrDefault(exp, 0)); //xx
                }
            }
        }

        Object r = 0;
        try {
            r = AviatorEvaluator.execute(newExpress, storeMap);  //xx + xxxx, {xx:11, xxxx:12}
        } catch (Exception e) {
            logger.error("解析公式失败,express={},newExpress={}, {}", express, newExpress, storeMap.toString());
        }

        return r;
    }


    /**
     * 解析会计科目区间值.
     *
     * @param storeMap
     * @param exp
     * @param replaceStr
     * @param periodId
     */
    private void parseMultiCode(Map storeMap, String exp, String replaceStr, Long periodId) {
        Map paramMap = new HashMap();
        String codeStr = exp.substring(exp.indexOf("<") + 1, exp.indexOf(">")); //1001:1009
        String type = exp.substring(exp.indexOf(">") + 1); //.JC
        String[] codes = codeStr.split(":");  //["1001", "1009"]

        paramMap.put("startCode", codes[0]);
        paramMap.put("endCode", codes[1]);
        paramMap.put("periodId", periodId);

        if (StringUtils.isEmpty(type)) {
            Map sumMap = subjectBalanceExtendMapper.getSumByStartAndEndCode(paramMap);
            storeMap.put(replaceStr, sumMap == null ? 0 : sumMap.getOrDefault("terminal_balance", 0));
        } else if (".C".equals(type)) {
            Map sumMap = subjectBalanceExtendMapper.getSumByStartAndEndCode(paramMap);
            storeMap.put(replaceStr, sumMap == null ? 0 : sumMap.getOrDefault("initial_balance", 0));
        } else {
            paramMap.put("sum", type);
            Map sumMap = subjectBalanceExtendMapper.getSumByStartAndEndCode(paramMap);
            storeMap.put(replaceStr, sumMap == null ? 0 : sumMap.getOrDefault(type, 0));
        }
    }

    /**
     * 根据期间id获取科目余额表信息并转换成map.
     * <p>
     * (<1001>.C,440)
     *
     * @param periodId
     * @return
     */
    private Map subjectBalances(Long periodId) {

        //获取当前期科目余额表信息.
        List<Map> balanceAndDirects = subjectBalanceExtendMapper.subjectBalanceAndDirection(periodId);
        Map map = new HashMap();

        for (Map balanceAndDirect : balanceAndDirects) {

            String subjectCodeStr = balanceAndDirect.get("subject_code").toString();
            Integer direction = (Integer) balanceAndDirect.getOrDefault("direction", 1);
            String formatPre = "<".concat(subjectCodeStr).concat(">");

            if (direction == 1) {
                map.put(formatPre.concat(".C"), balanceAndDirect.getOrDefault("initial_debit_balance", 0));
                map.put(formatPre.concat(""), balanceAndDirect.getOrDefault("terminal_debit_balance", 0));
            } else {
                map.put(formatPre.concat(".C"), balanceAndDirect.getOrDefault("initial_credit_balance", 0));
                map.put(formatPre.concat(""), balanceAndDirect.getOrDefault("terminal_credit_balance", 0));
            }

            map.put(formatPre.concat(".DC"), balanceAndDirect.getOrDefault("initial_credit_balance", 0));
            map.put(formatPre.concat(".JC"), balanceAndDirect.getOrDefault("initial_debit_balance", 0));
            map.put(formatPre.concat(".JF"), balanceAndDirect.getOrDefault("period_debit_occur", 0));
            map.put(formatPre.concat(".DF"), balanceAndDirect.getOrDefault("period_credit_occur", 0));
            map.put(formatPre.concat(".JL"), balanceAndDirect.getOrDefault("year_debit_occur", 0));
            map.put(formatPre.concat(".DL"), balanceAndDirect.getOrDefault("year_credit_occur", 0));
            map.put(formatPre.concat(".JY"), balanceAndDirect.getOrDefault("terminal_debit_balance", 0));
            map.put(formatPre.concat(".DY"), balanceAndDirect.getOrDefault("terminal_credit_balance", 0));
            map.put(formatPre.concat(".SY"), balanceAndDirect.getOrDefault("profit_loss_occur_amount", 0));
            map.put(formatPre.concat(".LY"), balanceAndDirect.getOrDefault("profit_loss_total_occur_amount", 0));
        }

        return map;

    }

}
