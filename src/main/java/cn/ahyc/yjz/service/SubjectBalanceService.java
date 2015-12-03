package cn.ahyc.yjz.service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: SubjectBalanceService
 * @Description: TODO
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月18日 上午10:12:42
 * 
 */
public interface SubjectBalanceService {

    /**
     * 查询科目余额
     * 
     * @param subjectCode
     * @param periodId
     * @return
     */
    List<Map<String, Object>> querySubjectBalanceList(Long subjectCode, Long periodId);

    /**
     * 查询科目余额
     * 
     * @param bookId
     * @param periodFrom
     * @param periodTo
     * @param level
     * @param subjectCodeFrom
     * @param subjectCodeTo
     * @param valueNotNull
     * @return
     */
    List<Map<String, Object>> querySubjectBalanceList(Long bookId, Integer periodFrom, Integer periodTo, Long level,
            Long subjectCodeFrom, Long subjectCodeTo, Long valueNotNull);


    /**
     * 查询最新余额.（For east.ftl）
     * @param map
     * <p>{'currentPeriod':currentPeriod,'bookId':book_id}</p>
     * @return
     */
    List<Map> selectLatestBalance(Map map);

    /**
     * 查询总账
     * 
     * @param bookId
     * @param periodFrom
     * @param periodTo
     * @param level
     * @param subjectCodeFrom
     * @param subjectCodeTo
     * @param valueNotNull
     * @return
     */
    List<Map<String, Object>> queryLedgerList(Long bookId, Integer periodFrom, Integer periodTo, Long level,
            Long subjectCodeFrom, Long subjectCodeTo, Long valueNotNull);

}
