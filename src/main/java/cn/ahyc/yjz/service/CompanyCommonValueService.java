package cn.ahyc.yjz.service;

import java.util.List;

import cn.ahyc.yjz.model.CompanyCommonValue;

/**
 * @ClassName: CompanyCommonValueService
 * @Description: TODO
 * @author chengjiarui 1256064203@qq.com
 * @date 2015年10月17日 上午11:22:41
 * 
 */
public interface CompanyCommonValueService {


    /**
     * 获取企业通用配置值列表
     * 
     * @param typeId
     * @return
     */
    List<CompanyCommonValue> queryListByType(Long typeId);

    /**
     * 保存
     * 
     * @param entity
     */
    void save(CompanyCommonValue entity);

    /**
     * 删除
     * 
     * @param id
     */
    void delete(Long id);

    /**
     * 检查是否已存在
     * 
     * @param name
     * @param id
     * @param type
     * @return
     */
    boolean check(String name, Long id, Long type);
}
