package cn.ahyc.yjz.service;

import java.util.List;
import java.util.Map;

import cn.ahyc.yjz.dto.ReportRow;

/**
 * @ClassName: ProfitService
 * @Description: TODO
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月23日 下午3:41:29
 * 
 */
public interface ProfitService {

    /**
     * 数据库查询统计数据列表
     * 
     * @param currentPeriod
     * @param bookId
     * @return
     */
    List<Map<String, Object>> getList(Integer currentPeriod, Long bookId);

    /**
     * 单个公式计算
     * 
     * @param currentPeriod
     * @param bookId
     * @param expStr
     */
    Object countExp(Integer currentPeriod, Long bookId, String expStr);

    /**
     * 请求参数查询统计数据列表
     * 
     * @param expList
     * @param currentPeriod
     * @param bookId
     * @return
     */
    List<Map<String, Object>> getListWithExpList(List<ReportRow> expList, Integer currentPeriod, Long bookId);

    /**
     * 保存
     * 
     * @param expList
     * @param currentPeriod
     * @param bookId
     */
    void save(List<ReportRow> expList, Integer currentPeriod, Long bookId);

}
