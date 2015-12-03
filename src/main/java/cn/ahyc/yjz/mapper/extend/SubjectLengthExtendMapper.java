package cn.ahyc.yjz.mapper.extend;

import java.util.Map;

import cn.ahyc.yjz.mapper.base.SubjectLengthMapper;

public interface SubjectLengthExtendMapper extends SubjectLengthMapper{

	/**
	 * 复制前年的科目代码长度数据为今年的数据.
	 * @param map
	 * @return
	 */
	void copySubjectLength(Map<String, Object> param);
}
