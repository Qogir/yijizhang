package cn.ahyc.yjz.mapper.extend;

import java.util.List;
import java.util.Map;

import cn.ahyc.yjz.dto.ProfitPeriod;
import cn.ahyc.yjz.dto.ReportRow;
import cn.ahyc.yjz.mapper.base.ProfitTemplateMapper;

/**
 * @ClassName: ProfitExtendMapper
 * @Description: TODO
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月27日 下午7:14:33
 * 
 */
public interface ProfitExtendMapper extends ProfitTemplateMapper {

    /**
     * 查询利润表模板
     * 
     * @return
     */
    List<ReportRow> selectProfitExpressionColumn();

    /**
     * 根据期间和账套id查询保存的利润表
     * 
     * @param map
     * @return
     */
    List<Map<String, Object>> selectProfitWithPeriod(Map<String, Object> map);

    /**
     * 保存、不过滤name中的空格
     * 
     * @param entry
     */
    void insertSelective(ProfitPeriod entry);

}