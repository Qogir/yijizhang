package cn.ahyc.yjz.mapper.extend;

import cn.ahyc.yjz.mapper.base.UserMapper;
import cn.ahyc.yjz.model.User;
import cn.ahyc.yjz.model.UserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserExtendMapper extends UserMapper{

    /**
     * 根据用户名查询用户.
     * @param username
     * @return
     */
    User selectUserByName(String username);


    /**
     * 修改密码.
     * @param map
     * @return
     */
    int updatePassword(Map map);
}