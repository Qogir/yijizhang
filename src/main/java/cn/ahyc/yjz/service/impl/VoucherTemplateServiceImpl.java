package cn.ahyc.yjz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ahyc.yjz.mapper.extend.VoucherTemplateDetailExtendMapper;
import cn.ahyc.yjz.mapper.extend.VoucherTemplateExtendMapper;
import cn.ahyc.yjz.model.VoucherTemplate;
import cn.ahyc.yjz.model.VoucherTemplateDetail;
import cn.ahyc.yjz.model.VoucherTemplateDetailExample;
import cn.ahyc.yjz.model.VoucherTemplateExample;
import cn.ahyc.yjz.service.VoucherTemplateService;

/**
 * @ClassName: VoucherTemplateServiceImpl
 * @Description: TODO
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月18日 下午5:07:06
 * 
 */
@Service
public class VoucherTemplateServiceImpl implements VoucherTemplateService {

    @Autowired
    private VoucherTemplateExtendMapper voucherTemplateExtendMapper;
    @Autowired
    private VoucherTemplateDetailExtendMapper voucherTemplateDetailExtendMapper;

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.VoucherTemplateService#queryDetailList(java.lang.
     * Long, long)
     */
    @Override
    public List<Map<String, Object>> queryDetailList(Long voucherTemplateId, long bookId) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("voucherTemplateId", voucherTemplateId);
        param.put("bookId", bookId);
        return voucherTemplateDetailExtendMapper.selectDetailList(param);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.VoucherTemplateService#queryDetailTotal(java.lang.
     * Long)
     */
    @Override
    public Map<String, Object> queryDetailTotal(Long voucherTemplateId) {
        return voucherTemplateDetailExtendMapper.selectDetailTotal(voucherTemplateId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.VoucherTemplateService#queryVoucherTemplateList()
     */
    @Override
    public List<VoucherTemplate> queryVoucherTemplateList() {
        VoucherTemplateExample example = new VoucherTemplateExample();
        return voucherTemplateExtendMapper.selectByExample(example);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.VoucherTemplateService#queryVoucherTemplate(java.lang
     * .Long)
     */
    @Override
    public VoucherTemplate queryVoucherTemplate(Long voucherTemplateId) {
        return voucherTemplateExtendMapper.selectByPrimaryKey(voucherTemplateId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.VoucherTemplateService#queryVoucherTemplateDetailList
     * (java.lang.Long, java.lang.Long)
     */
    @Override
    public List<VoucherTemplateDetail> queryVoucherTemplateDetailList(Long voucherTemplateId, Long bookId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bookId", bookId);
        map.put("voucherTemplateId", voucherTemplateId);
        return voucherTemplateDetailExtendMapper.selectTemplateDetailList(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.VoucherTemplateService#saveTemplate(cn.ahyc.yjz.model
     * .VoucherTemplate, java.util.List)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTemplate(VoucherTemplate voucherTemplate, List<VoucherTemplateDetail> details) {
        Long templateId;
        /** 新增、更新记账模式凭证 **/
        if (voucherTemplate != null && voucherTemplate.getId() != null) {
            templateId = voucherTemplate.getId();
            voucherTemplateExtendMapper.updateByPrimaryKeySelective(voucherTemplate);
            /** 删除模式凭证明细 **/
            deleteTemplateDetails(templateId);
        } else {
            voucherTemplateExtendMapper.insertSelectiveReturnId(voucherTemplate);
            templateId = voucherTemplate.getId();
        }
        /** 新增模式凭证明细 **/
        for (VoucherTemplateDetail detail : details) {
            detail.setTemplateId(templateId);
            detail.setId(null);
            voucherTemplateDetailExtendMapper.insertSelective(detail);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.VoucherTemplateService#checkTemplateName(java.lang.
     * String, java.lang.Long)
     */
    @Override
    public boolean checkTemplateName(String name, Long id) {
        VoucherTemplateExample example = new VoucherTemplateExample();
        cn.ahyc.yjz.model.VoucherTemplateExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        if (id != null) {
            criteria.andIdNotEqualTo(id);
        }
        return voucherTemplateExtendMapper.countByExample(example) < 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.VoucherTemplateService#deleteTemplate(java.lang.Long)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplate(Long id) {
        voucherTemplateExtendMapper.deleteByPrimaryKey(id);
        deleteTemplateDetails(id);
    }

    /**
     * 删除模式凭证明细
     * 
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateDetails(Long id) {
        VoucherTemplateDetailExample example = new VoucherTemplateDetailExample();
        cn.ahyc.yjz.model.VoucherTemplateDetailExample.Criteria criteria = example.createCriteria();
        criteria.andTemplateIdEqualTo(id);
        voucherTemplateDetailExtendMapper.deleteByExample(example);
    }

}
