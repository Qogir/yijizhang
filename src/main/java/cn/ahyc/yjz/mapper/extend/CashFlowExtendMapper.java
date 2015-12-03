package cn.ahyc.yjz.mapper.extend;

import cn.ahyc.yjz.dto.ReportRow;
import cn.ahyc.yjz.mapper.base.CashFlowSheetMapper;

import java.util.List;

/**
 * Created by Joey Yan on 15-11-3.
 */
public interface CashFlowExtendMapper extends CashFlowSheetMapper {

    /**
     * 查询现金流量公式
     * @return
     */
    List<ReportRow> selectCashFlowExpressionColumn();
}
