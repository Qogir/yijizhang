package cn.ahyc.yjz.mapper.extend;

import java.util.List;
import java.util.Map;

import cn.ahyc.yjz.dto.SubjectBalanceDto;
import cn.ahyc.yjz.mapper.base.SubjectBalanceMapper;

public interface SubjectBalanceExtendMapper extends SubjectBalanceMapper {

    /**
     * 查询所有损益类科目余额
     *
     * @return
     */
    List<SubjectBalanceDto> selectProfitAndLoss(Map<String, Object> param);

    /**
     * 新增或更新科目余额表累计项、余额项
     *
     * @param periodId
     */
    int insertOrUpdateSubjectBalance(Long periodId);

    /**
     * 初始化下一期科目余额
     *
     * @param param
     * @return
     */
    int insertNextPeriodSubjectBalance(Map<String, Object> param);

    /**
     * 汇总科目余额表
     *
     * @param periodId
     * @return
     */
    int collectSubjectBalance(Long periodId);

    /**
     * 查询科目余额
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> selectSubjectBalanceList(Map<String, Object> map);

    /**
     * 查询科目余额
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> selectBalanceList(Map<String, Object> map);


    /**
     * 获取科目余额表和科目对应方向.
     *
     * @param periodId
     * @return
     */
    List<Map> subjectBalanceAndDirection(Long periodId);

    /**
     * 查询某一科目期初余额
     *
     * @param map
     * @return
     */
    Map<String, Object> selectBalanceBySubjectCode(Map<String, Object> map);

    /**
     * 查询最新余额.（For east.ftl）
     *
     * @param map <p>{'currentPeriod':currentPeriod,'bookId':book_id}</p>
     * @return
     */
    List<Map> selectLatestBalance(Map map);

    /**
     * 查询总账
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> selectLedgerList(Map<String, Object> map);

    /**
     * 资产负债表根据科目区间计算总数.
     *
     * @param paramMap
     * @return
     */
    Map getSumByStartAndEndCode(Map paramMap);

    /**
     * 账上取数
     * 
     * @param map
     */
    Long getExpressCellValueWithBook(Map<String, Object> map);
}
