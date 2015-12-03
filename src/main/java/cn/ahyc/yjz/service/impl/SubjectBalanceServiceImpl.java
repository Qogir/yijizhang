package cn.ahyc.yjz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ahyc.yjz.mapper.extend.PeriodExtendMapper;
import cn.ahyc.yjz.mapper.extend.SubjectBalanceExtendMapper;
import cn.ahyc.yjz.model.PeriodExample;
import cn.ahyc.yjz.service.SubjectBalanceService;

/**
 * @ClassName: SubjectBalanceServiceImpl
 * @Description: TODO
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月18日 上午10:11:52
 * 
 */
@Service
public class SubjectBalanceServiceImpl implements SubjectBalanceService {

    @Autowired
    private SubjectBalanceExtendMapper subjectBalanceExtendMapper;

    @Autowired
    private PeriodExtendMapper periodExtendMapper;

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.SubjectBalanceService#querySubjectBalanceList(java.
     * lang.Long, java.lang.Long)
     */
    @Override
    public List<Map<String, Object>> querySubjectBalanceList(Long subjectCode, Long periodId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("subjectCode", subjectCode);
        map.put("periodId", periodId);
        return subjectBalanceExtendMapper.selectSubjectBalanceList(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.SubjectBalanceService#querySubjectBalanceList(java.
     * lang.Long, java.lang.Integer, java.lang.Integer, java.lang.Long,
     * java.lang.Long, java.lang.Long, java.lang.Long)
     */
    @Override
    public List<Map<String, Object>> querySubjectBalanceList(Long bookId, Integer periodFrom, Integer periodTo,
            Long level, Long subjectCodeFrom, Long subjectCodeTo, Long valueNotNull) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bookId", bookId);
        map.put("periodFrom", periodFrom);
        map.put("periodTo", periodTo);
        map.put("level", level);
        map.put("subjectCodeFrom", subjectCodeFrom);
        map.put("subjectCodeTo", subjectCodeTo);
        map.put("valueNotNull", valueNotNull);
        return subjectBalanceExtendMapper.selectBalanceList(map);
    }

    /**
     * 查询最新余额.（For east.ftl）
     *
     * @param map <p>{'currentPeriod':currentPeriod,'bookId':book_id}</p>
     * @return
     */
    @Override
    public List<Map> selectLatestBalance(Map map) {
        return subjectBalanceExtendMapper.selectLatestBalance(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.SubjectBalanceService#queryLedgerList(java.lang.Long,
     * java.lang.Integer, java.lang.Integer, java.lang.Long, java.lang.Long,
     * java.lang.Long, java.lang.Long)
     */
    @Override
    public List<Map<String, Object>> queryLedgerList(Long bookId, Integer periodFrom, Integer periodTo, Long level,
            Long subjectCodeFrom, Long subjectCodeTo, Long valueNotNull) {
        Map<String, Object> map = new HashMap<String, Object>();
        PeriodExample example = new PeriodExample();
        PeriodExample.Criteria criteria = example.createCriteria();
        criteria.andBookIdEqualTo(bookId);
        criteria.andCurrentPeriodEqualTo(1);
        map.put("lastYear", periodFrom == 1 && periodExtendMapper.countByExample(example) <= 0 ? true : null);
        map.put("bookId", bookId);
        map.put("periodFrom", periodFrom);
        map.put("periodTo", periodTo);
        map.put("level", level);
        map.put("subjectCodeFrom", subjectCodeFrom);
        map.put("subjectCodeTo", subjectCodeTo);
        map.put("valueNotNull", valueNotNull);
        return subjectBalanceExtendMapper.selectLedgerList(map);
    }

}
