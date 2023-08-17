package com.xiaoguan.dataservice.service;

import com.xiaoguan.api.model.FinanceAccount;
import com.xiaoguan.api.model.User;
import com.xiaoguan.api.pojo.UserAccountInfo;
import com.xiaoguan.api.service.UserService;
import com.xiaoguan.common.util.CommonUtil;
import com.xiaoguan.dataservice.mapper.FinanceAccountMapper;
import com.xiaoguan.dataservice.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@DubboService(interfaceClass = UserService.class,version = "1.0")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Value("${ylb.config.pwdsalt}")
    private String pwdSalt;
    @Resource
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public User queryByPhone(String phone) {
        User user=null;
        if (CommonUtil.checkPhone(phone)) {
            user=userMapper.selectByPhone(phone);
        }
        return user;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized int userRegister(String phone, String pwd) {
        int result=0;//默认参数不正确
        User queryUser = userMapper.selectByPhone(phone);
        if (queryUser==null) {
            if(CommonUtil.checkPhone(phone)&&(pwd!=null&&pwd.length()==32)){
                //注册密码的md5二次加密，给原始密码加盐
                pwd=DigestUtils.md5Hex(pwd+pwdSalt);
                //注册user表
                User user=new User();
                user.setPhone(phone);
                user.setLoginPassword(pwd);
                user.setAddTime(new Date());
                userMapper.insertReturnPrimaryKey(user);
                //添加资金账户
                FinanceAccount financeAccount = new FinanceAccount();
                financeAccount.setUid(user.getId());
                financeAccount.setAvailableMoney(new BigDecimal("0"));
                financeAccountMapper.insertSelective(financeAccount);
                result=1;

            }
        }else {
            result=2;//2手机号已经注册了
        }

        return result;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User userLogin(String phone, String pwd) {
        User user=null;
        if (CommonUtil.checkPhone(phone)&&(pwd!=null&&pwd.length()==32)) {
            //注册密码的md5二次加密，给原始密码加盐
            pwd=DigestUtils.md5Hex(pwd+pwdSalt);
            user=userMapper.selectLogin(phone,pwd);
            //更新最后登录时间
            if (user!=null) {
                user.setLastLoginTime(new Date());
                userMapper.updateByPrimaryKeySelective(user);
            }
        }
        return user;
    }

    @Override
    public String queryUserAvatar(Integer uid) {
        String path=null;
        if(uid!=null&&uid>0){
            User user = userMapper.selectByPrimaryKey(uid);
            if(user!=null){
                path=user.getHeaderImage();
            }
        }
        return path;
    }

    //通过uid查询账户信息
    @Override
    public UserAccountInfo queryUserAccountInfo(Integer uid) {
        UserAccountInfo userAccountInfo=null;
        if(uid!=null&&uid>0){
            userAccountInfo=userMapper.selectUserAccountInfoByUid(uid);
        }
        return userAccountInfo;
    }

    //更新实名认证信息
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean modifyRealName(String phone, String name, String idCard) {
        boolean result=false;
        if (CommonUtil.checkPhone(phone)&& StringUtils.isNotBlank(name)&&StringUtils.isNotBlank(idCard)) {
            int rows=userMapper.updateRealNameByPhone(phone,name,idCard);//更新条数
            if(rows==1){
                result=true;
            }
        }
        return result;
    }

    //检查手机号是否存在
    @Override
    public Boolean checkPhoneHasRegister(String Phone) {
        Boolean flag=false;
        User queryUser = userMapper.selectByPhone(Phone);
        if (queryUser!=null) {
            flag=true;
        }
        return flag;
    }
}
