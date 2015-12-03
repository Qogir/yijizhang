package cn.ahyc.yjz.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import cn.ahyc.yjz.dto.ProfitPeriod;
import cn.ahyc.yjz.dto.ReportRow;
import cn.ahyc.yjz.mapper.base.ProfitPeriodMapper;
import cn.ahyc.yjz.mapper.extend.ProfitExtendMapper;
import cn.ahyc.yjz.mapper.extend.SubjectBalanceExtendMapper;
import cn.ahyc.yjz.model.ProfitPeriodExample;
import cn.ahyc.yjz.service.ProfitService;
import cn.ahyc.yjz.util.CellValueFunction;
import cn.ahyc.yjz.util.MyAviator;

/**
 * @ClassName: ProfitServiceImpl
 * @Description: TODO
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月23日 下午5:06:31
 * 
 */
@Service
public class ProfitServiceImpl implements ProfitService {

    @Autowired
    private ProfitExtendMapper profitExtendMapper;

    @Autowired
    private SubjectBalanceExtendMapper subjectBalanceExtendMapper;

    @Autowired
    private ProfitPeriodMapper profitPeriodMapper;

    /*
     * (non-Javadoc)
     * 
     * @see cn.ahyc.yjz.service.ProfitService#getList(java.lang.Integer,
     * java.lang.Long)
     */
    @Override
    public List<Map<String, Object>> getList(Integer currentPeriod, Long bookId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("currentPeriod", currentPeriod);
        map.put("bookId", bookId);
        List<Map<String, Object>> list = profitExtendMapper.selectProfitWithPeriod(map);
        if (list != null && list.size() > 0) {
            return list;
        }
        List<ReportRow> colList = profitExtendMapper.selectProfitExpressionColumn();
        list = exeExpression(currentPeriod, bookId, colList);
        return list;
    }

    /**
     * 利润表：计算公式
     * 
     * @param currentPeriod
     * @param bookId
     * @param colList
     * @return
     */
    private List<Map<String, Object>> exeExpression(Integer currentPeriod, Long bookId,
            List<ReportRow> colList) {
        List<Expression> compileList = new ArrayList<Expression>();
        Map<String, Object> envMap = new HashMap<String, Object>();
        // 注册函数
        AviatorEvaluator.addFunction(new CellValueFunction());
        // 编译表达式、设置变量
        for (ReportRow col : colList) {
            MyAviator.compile(compileList, MyAviator.getEnvAndExpression(envMap, col.getcB()));
            MyAviator.compile(compileList, MyAviator.getEnvAndExpression(envMap, col.getcC()));
        }
        // 获取变量值
        envMap = MyAviator.getEnvValue(envMap, currentPeriod, bookId, subjectBalanceExtendMapper);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        // 执行表达式
        int j = 0;
        for (ReportRow col : colList) {
            map = new HashMap<String, Object>();
            map.put("fix", col.getFix());
            map.put("cA", col.getcA());
            map.put("cAVal", col.getcA());
            map.put("cB", col.getcB());
            map.put("cC", col.getcC());
            map.put("cBVal", compileList.get(j++).execute(envMap));
            map.put("cCVal", compileList.get(j++).execute(envMap));
            list.add(map);
            envMap.put("list", list);
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.ahyc.yjz.service.ProfitService#countExp(java.lang.Integer,
     * java.lang.Long, java.lang.String)
     */
    @Override
    public Object countExp(Integer currentPeriod, Long bookId, String expStr) {
        List<Expression> compileList = new ArrayList<Expression>();
        Map<String, Object> envMap = new HashMap<String, Object>();
        MyAviator.compile(compileList, MyAviator.getEnvAndExpression(envMap, expStr));
        envMap = MyAviator.getEnvValue(envMap, currentPeriod, bookId, subjectBalanceExtendMapper);
        return MyAviator.execute(compileList.get(0), envMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.ahyc.yjz.service.ProfitService#getListWithExpList(java.util.List,
     * java.lang.Integer, java.lang.Long)
     */
    @Override
    public List<Map<String, Object>> getListWithExpList(List<ReportRow> expList, Integer currentPeriod,
            Long bookId) {
        List<Map<String, Object>> list = exeExpression(currentPeriod, bookId, expList);
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.ahyc.yjz.service.ProfitService#save(java.util.List,
     * java.lang.Integer, java.lang.Long)
     */
    @Override
    public void save(List<ReportRow> expList, Integer currentPeriod, Long bookId) {
        // 删除
        ProfitPeriodExample example = new ProfitPeriodExample();
        ProfitPeriodExample.Criteria criteria = example.createCriteria();
        criteria.andBookIdEqualTo(bookId);
        criteria.andCurrentPeriodEqualTo(currentPeriod);
        profitPeriodMapper.deleteByExample(example);
        // 新增
        ProfitPeriod entry;
        for (ReportRow rr : expList) {
            entry = new ProfitPeriod();
            entry.setFix(StringUtils.isNotBlank(rr.getFix()) ? 1 : null);
            entry.setName(StringUtils.isNotBlank(rr.getcAVal()) ? rr.getcAVal().replaceAll("&nbsp;", " ") : rr.getcA());
            entry.setMonthExp(rr.getcB());
            entry.setMonthMoney(
                    StringUtils.isNotBlank(rr.getcBVal()) ? new BigDecimal(rr.getcBVal()) : BigDecimal.ZERO);
            entry.setLastMonthExp(rr.getcC());
            entry.setLastMonthMoney(
                    StringUtils.isNotBlank(rr.getcCVal()) ? new BigDecimal(rr.getcCVal()) : BigDecimal.ZERO);
            entry.setBookId(bookId);
            entry.setCurrentPeriod(currentPeriod);
            profitExtendMapper.insertSelective(entry);
        }
    }
}
