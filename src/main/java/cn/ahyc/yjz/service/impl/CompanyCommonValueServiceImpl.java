package cn.ahyc.yjz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ahyc.yjz.mapper.base.CompanyCommonValueMapper;
import cn.ahyc.yjz.model.CompanyCommonValue;
import cn.ahyc.yjz.model.CompanyCommonValueExample;
import cn.ahyc.yjz.model.CompanyCommonValueExample.Criteria;
import cn.ahyc.yjz.service.CompanyCommonValueService;

/**
 * @ClassName: CompanyCommonValueServiceImpl
 * @Description: TODO
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月17日 上午11:25:59
 * 
 */
@Service
public class CompanyCommonValueServiceImpl implements CompanyCommonValueService {

    @Autowired
    private CompanyCommonValueMapper companyCommonValueMapper;

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.CompanyCommonValueService#queryListByType(java.lang.
     * Long)
     */
    @Override
    public List<CompanyCommonValue> queryListByType(Long typeId) {
        CompanyCommonValueExample example = new CompanyCommonValueExample();
        Criteria criteria = example.createCriteria();
        criteria.andTypeIdEqualTo(typeId);
        return companyCommonValueMapper.selectByExample(example);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.CompanyCommonValueService#save(cn.ahyc.yjz.model.
     * CompanyCommonValue)
     */
    @Override
    public void save(CompanyCommonValue entity) {
        if (entity.getId() != null) {
            companyCommonValueMapper.updateByPrimaryKey(entity);
        } else {
            companyCommonValueMapper.insertSelective(entity);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.ahyc.yjz.service.CompanyCommonValueService#delete(java.lang.Long)
     */
    @Override
    public void delete(Long id) {
        companyCommonValueMapper.deleteByPrimaryKey(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.ahyc.yjz.service.CompanyCommonValueService#check(java.lang.String,
     * java.lang.Long, java.lang.Long)
     */
    @Override
    public boolean check(String name, Long id, Long type) {
        CompanyCommonValueExample example = new CompanyCommonValueExample();
        cn.ahyc.yjz.model.CompanyCommonValueExample.Criteria criteria = example.createCriteria();
        criteria.andShowValueEqualTo(name);
        criteria.andTypeIdEqualTo(type);
        if (id != null) {
            criteria.andIdNotEqualTo(id);
        }
        return companyCommonValueMapper.countByExample(example) < 1;
    }

}
