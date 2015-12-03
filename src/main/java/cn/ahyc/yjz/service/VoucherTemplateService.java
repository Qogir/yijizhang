package cn.ahyc.yjz.service;

import java.util.List;
import java.util.Map;

import cn.ahyc.yjz.model.VoucherTemplate;
import cn.ahyc.yjz.model.VoucherTemplateDetail;

/**
 * @ClassName: VoucherTemplateService
 * @Description: TODO
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月18日 下午5:06:25
 * 
 */
public interface VoucherTemplateService {

    /**
     * 查询明细列表
     * 
     * @param voucherTemplateId
     * @param bookId
     * @return
     */
    List<Map<String, Object>> queryDetailList(Long voucherTemplateId, long bookId);

    /**
     * 统计明细金额
     * 
     * @param voucherTemplateId
     * @return
     */
    Map<String, Object> queryDetailTotal(Long voucherTemplateId);

    /**
     * 查询模式凭证列表
     * 
     * @return
     */
    List<VoucherTemplate> queryVoucherTemplateList();

    /**
     * 查询模式凭证
     * 
     * @param voucherTemplateId
     * @return
     */
    VoucherTemplate queryVoucherTemplate(Long voucherTemplateId);

    /**
     * 查询模式凭证详细
     * 
     * @param voucherTemplateId
     * @param long1
     * @return
     */
    List<VoucherTemplateDetail> queryVoucherTemplateDetailList(Long voucherTemplateId, Long long1);

    /**
     * 保存模式凭证
     * 
     * @param voucherTemplate
     * @param details
     */
    void saveTemplate(VoucherTemplate voucherTemplate, List<VoucherTemplateDetail> details);

    /**
     * 检查模式凭证名称是否重复
     * 
     * @param name
     * @param id
     * @return
     */
    boolean checkTemplateName(String name, Long id);

    /**
     * 删除模式凭证
     * 
     * @param id
     */
    void deleteTemplate(Long id);

}
