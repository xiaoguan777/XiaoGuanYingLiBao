package com.xiaoguan.api.service;

import com.xiaoguan.api.model.User;
import com.xiaoguan.api.pojo.UserAccountInfo;

public interface UserService {
    /**
     * 判断手机号是否存在
     */
    User queryByPhone(String phone);

    int userRegister(String phone, String pwd);

    User userLogin(String phone, String pwd);
    Boolean checkPhoneHasRegister(String Phone);
    //更新实名认证信息
    boolean modifyRealName(String phone, String name, String idCard);
    //获取用户及资金信息
    UserAccountInfo queryUserAccountInfo(Integer uid);
    //查询用户头像地址
    String queryUserAvatar(Integer uid);
}
