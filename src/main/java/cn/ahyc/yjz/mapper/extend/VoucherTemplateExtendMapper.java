package cn.ahyc.yjz.mapper.extend;

import cn.ahyc.yjz.mapper.base.VoucherTemplateMapper;
import cn.ahyc.yjz.model.VoucherTemplate;

/**
 * @ClassName: VoucherTemplateExtendMapper
 * @Description: TODO
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月18日 上午10:13:06
 * 
 */
public interface VoucherTemplateExtendMapper extends VoucherTemplateMapper {

    /**
     * 保存模式凭证，返回主键
     * 
     * @param record
     * @return
     */
    int insertSelectiveReturnId(VoucherTemplate record);
}