package cn.ahyc.yjz.mapper.extend;

import java.util.List;
import java.util.Map;

import cn.ahyc.yjz.mapper.base.VoucherTemplateDetailMapper;
import cn.ahyc.yjz.model.VoucherTemplateDetail;

/**
 * @ClassName: VoucherTemplateDetailExtendMapper
 * @Description: TODO
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月18日 上午10:13:13
 * 
 */
public interface VoucherTemplateDetailExtendMapper extends VoucherTemplateDetailMapper {

    /**
     * 查询模式凭证详细
     * 
     * @param map
     * @return
     */
    List<VoucherTemplateDetail> selectTemplateDetailList(Map<String, Object> map);

    /**
     * 查询明细
     * 
     * @param param
     * @return
     */
    List<Map<String, Object>> selectDetailList(Map<String, Object> param);

    /**
     * 查询明细合计
     * 
     * @param voucherTemplateId
     * @return
     */
    Map<String, Object> selectDetailTotal(Long voucherTemplateId);

}