package cn.ahyc.yjz.mapper.base;

import cn.ahyc.yjz.model.SubjectBalance;
import cn.ahyc.yjz.model.SubjectBalanceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SubjectBalanceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subject_balance
     *
     * @mbggenerated Tue Oct 20 10:18:09 CST 2015
     */
    int countByExample(SubjectBalanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subject_balance
     *
     * @mbggenerated Tue Oct 20 10:18:09 CST 2015
     */
    int deleteByExample(SubjectBalanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subject_balance
     *
     * @mbggenerated Tue Oct 20 10:18:09 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subject_balance
     *
     * @mbggenerated Tue Oct 20 10:18:09 CST 2015
     */
    int insert(SubjectBalance record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subject_balance
     *
     * @mbggenerated Tue Oct 20 10:18:09 CST 2015
     */
    int insertSelective(SubjectBalance record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subject_balance
     *
     * @mbggenerated Tue Oct 20 10:18:09 CST 2015
     */
    List<SubjectBalance> selectByExample(SubjectBalanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subject_balance
     *
     * @mbggenerated Tue Oct 20 10:18:09 CST 2015
     */
    SubjectBalance selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subject_balance
     *
     * @mbggenerated Tue Oct 20 10:18:09 CST 2015
     */
    int updateByExampleSelective(@Param("record") SubjectBalance record, @Param("example") SubjectBalanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subject_balance
     *
     * @mbggenerated Tue Oct 20 10:18:09 CST 2015
     */
    int updateByExample(@Param("record") SubjectBalance record, @Param("example") SubjectBalanceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subject_balance
     *
     * @mbggenerated Tue Oct 20 10:18:09 CST 2015
     */
    int updateByPrimaryKeySelective(SubjectBalance record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subject_balance
     *
     * @mbggenerated Tue Oct 20 10:18:09 CST 2015
     */
    int updateByPrimaryKey(SubjectBalance record);
}