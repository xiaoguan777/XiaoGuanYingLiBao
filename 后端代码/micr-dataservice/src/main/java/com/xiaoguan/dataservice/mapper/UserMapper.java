package com.xiaoguan.dataservice.mapper;

import com.xiaoguan.api.model.User;
import com.xiaoguan.api.pojo.UserAccountInfo;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    //统计注册的人数
    int selectUsersCount();
    //添加记录获取主键值
    int insertReturnPrimaryKey(User user);
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    //使用手机号查询用户
    User selectByPhone(@Param("phone") String phone);
    //登录验证
    User selectLogin(@Param("phone") String phone,@Param("pwd") String pwd);
    //更新实名认证信息
    int updateRealNameByPhone(@Param("phone") String phone,
                              @Param("name") String name,
                              @Param("idCard") String idCard);
    //查询用户账户信息
    UserAccountInfo selectUserAccountInfoByUid(@Param("uid") Integer uid);
}