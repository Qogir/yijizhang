package cn.ahyc.yjz.mapper.extend;

import cn.ahyc.yjz.mapper.base.AccountSubjectMapper;
import cn.ahyc.yjz.model.AccountSubject;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AccountSubjectExtendMapper extends AccountSubjectMapper {

    /**
     * 根据大分类获取所有会计科目.
     *
     * @param param
     * @return
     */
    List<Map> getSubjectsByCategoryId(Map<String, Object> param);

    /**
     * 根据父级id获取所有子级.
     *
     * @param param
     * @return
     */
    List<Map> getChildrenSubjectsByCategoryId(Map<String, Object> param);

    /**
     * 查询所有末节点
     *
     * @param subjectCode
     * @param bookId
     * @return
     */
    List<AccountSubject> selectLastChildSubject(@Param("subjectCode") String subjectCode, @Param("bookId") Long bookId);

    /**
     * 根据账套id查询出损益类其中之一会计科目代码
     *
     * @param param
     * @return
     */
    Map<String, Object> getSubjectCodeByRoot(Map<String, Object> param);

    /**
     * 所以叶子节点汇总.
     *
     * @param bookId
     * @return
     */
    List<Map> getLastChildSum(Long bookId);

    /**
     * 往父级几点汇总.
     *
     * @param parentSubjectCodes
     * @param bookId
     * @return
     */
    List<Map> getParentSum(@Param("parentSubjectCodes") List<Long> parentSubjectCodes, @Param("bookId") Long bookId);

    /**
     * 计算期初余额平衡值.
     *
     * @param bookId
     * @return
     */
    Map getInitLeftBalance(@Param("bookId") Long bookId);

    /**
     * 本年累计平衡值.
     *
     * @param bookId
     * @return
     */
    Map getTotalBalance(@Param("bookId") Long bookId);

    /**
     * copy上一年度会计科目数据为本年会计科目数据.
     *
     * @param bookId
     * @return
     */
    void copyAccountSubject(Map<String, Object> param);

    /**
     * 查询凭证汇总
     *
     * @param startTime
     * @param endTime
     * @param voucherWord
     * @param voucherStartNo
     * @param voucherEndNo
     * @param session
     * @return
     */
    List<Map<String, Object>> searchVoucherCollect(Map<String, Object> param);
}