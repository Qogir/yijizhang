package cn.ahyc.yjz.mapper.extend;

import java.util.Map;

import cn.ahyc.yjz.mapper.base.AccountSubjectTemplateMapper;

public interface AccountSubjectTemplateExtendMapper extends AccountSubjectTemplateMapper{
	 /**
     * copy 会计科目模板表数据到会计科目表
     *
     * @mbggenerated Wed Sep 23 16:59:46 CST 2015
     */
    int copyAccountSubject(Map<String, Object> param);
}
