package com.xiaoguan.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaoguan.api.model.User;
import com.xiaoguan.api.pojo.UserAccountInfo;
import com.xiaoguan.common.enums.Rcode;
import com.xiaoguan.common.enums.SendCodeWays;
import com.xiaoguan.common.util.CommonUtil;
import com.xiaoguan.common.util.JwtUtil;
import com.xiaoguan.front.UpdateException;
import com.xiaoguan.front.view.RespResult;
import com.xiaoguan.front.vo.RealNameInfo;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户功能")
@RestController
@RequestMapping("/v1/user")
public class UserController extends BaseController{

    /**
     * 手机号注册用户
     */
    @ApiOperation(value = "用户注册",notes = "7可以注册，1004格式不正确，1005已经注册过了")
    @PostMapping ("/register")

    public RespResult userRegister(@RequestParam("phone") String phone,
                                   @RequestParam("pwd") String pwd,
                                   @RequestParam("scode") String scode){
        RespResult respResult=RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
             if (pwd!=null&&pwd.length()==32) {
                 if (smsService.checkSmsCode(phone,scode, SendCodeWays.Register)) {
                     //可以注册
                     int registerResilt =userService.userRegister(phone,pwd);
                     if (registerResilt==1) {
                         respResult=RespResult.ok();
                     }else if(registerResilt==2){
                         respResult.setRcode(Rcode.PHONE_EXISTS);
                     }else {
                         respResult.setRcode(Rcode.REQUEST_PARAM_ERROR);
                     }

                 }else {
                     //验证码无效
                     respResult.setRcode(Rcode.SMSCODE_INVALID);
                 }
            }else {
                 respResult.setRcode(Rcode.REQUEST_PARAM_ERROR);
             }
        }else {
            respResult.setRcode(Rcode.PHONE_FORMAT_ERROR);
        }
        return respResult;
    }
    /**
     * 手机号是否存在
     */
    @GetMapping("/phone/exists")
    @ApiOperation(value = "校验用户手机号是否存在",notes = "7可以注册，1004格式不正确，1005已经注册过了")
    public RespResult phoneExists(@RequestParam("phone") String phone){
        RespResult respResult=RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            //调用数据服务
            User user = userService.queryByPhone(phone);
            if(user==null){
                //可以注册
                respResult=RespResult.ok();
            }else {
                respResult.setRcode(Rcode.PHONE_EXISTS);
            }
        }else {
            respResult.setRcode(Rcode.PHONE_FORMAT_ERROR);
        }
        return respResult;
    }
    /**
     * 登录,获取token-jwt
     */
    @ApiOperation(value = "用户登录",notes = "判断用户是否能登录，并且发送登录token")
    @PostMapping ("/login")
    public RespResult userLogin(@RequestParam("phone") String phone,
                                @RequestParam("pwd") String pwd,
                                @RequestParam("scode") String scode){
        RespResult respResult=RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            if (pwd!=null&&pwd.length()==32) {
                if (smsService.checkSmsCode(phone,scode,SendCodeWays.Login)) {
                   User user= userService.userLogin(phone,pwd);
                   if(user!=null){
                       try {
                           Map<String,Object> data=new HashMap<>();
                           data.put("uid",user.getId());
                           String jwtToken = jwtUtil.createJwt(data, tokenTime);
                           respResult=RespResult.ok();
                           respResult.setAccessToken(jwtToken);
                           Map<String,Object> userInfo=new HashMap<>();
                           userInfo.put("uid",user.getId());
                           userInfo.put("phone",user.getPhone());
                           userInfo.put("name",user.getName());
                           respResult.setDate(userInfo);
                       } catch (Exception e) {
                           e.printStackTrace();
                           respResult.setRcode(Rcode.TOKEN_ERROR);
                       }
                   }else{
                       if (!userService.checkPhoneHasRegister(phone)){
                           respResult.setRcode(Rcode.USER_NOT_EXISTS);
                       }else{
                           respResult.setRcode(Rcode.PASSWORD_ERROR);
                       }
                   }
                }else{
                    //验证码无效
                    respResult.setRcode(Rcode.SMSCODE_INVALID);
                }

            }else {
                respResult.setRcode(Rcode.REQUEST_PARAM_ERROR);
            }
        }else {
            respResult.setRcode(Rcode.PHONE_FORMAT_ERROR);
        }


        return respResult;
    }
    @ApiOperation(value = "实名认证",notes = "对用户进行实名认证")
    @PostMapping ("/realName")
    public RespResult userRealName(@RequestBody RealNameInfo realNameInfo){
        RespResult respResult=RespResult.fail();
        if (CommonUtil.checkPhone(realNameInfo.getPhone())) {
            if (StringUtils.isNotBlank(realNameInfo.getRealName())&&
                    StringUtils.isNotBlank(realNameInfo.getIdCard())) {
                //判断验证码是否正确
                if (smsService.checkSmsCode(realNameInfo.getPhone(),realNameInfo.getScode(), SendCodeWays.RealName)) {
                    User user=userService.queryByPhone(realNameInfo.getPhone());
                    //判断是否已经实名认证
                    if(user!=null&&!StringUtils.isNotBlank(user.getName()))
                    {
                        //验证实名认证信息
                        try {
                            boolean judgeResult = realNameService.judgeNameAndCard(realNameInfo.getRealName(),
                                    realNameInfo.getIdCard(), realNameInfo.getPhone());
                            if (judgeResult) {
                                respResult=RespResult.ok();
                            }else {
                                respResult.setRcode(Rcode.JUDGE_NAME_IDCARD_FAILD);
                            }
                        } catch (UpdateException e) {
                            e.printStackTrace();
                            respResult.setRcode(Rcode.UDDATE_DATABASE_ERROR);
                        }
                    }else {
                        respResult.setRcode(Rcode.PHONE_INVALID);
                    }
                }else{
                    respResult.setRcode(Rcode.SMSCODE_INVALID);
                }
            }else {
                respResult.setRcode(Rcode.REQUEST_PARAM_ERROR);
            }
        }else{
            respResult.setRcode(Rcode.PHONE_FORMAT_ERROR);
        }
        return respResult;
    }
    @ApiOperation(value = "用户账户信息",notes = "根据用户id发送用户信息及其账户资金信息")
    @GetMapping("/userAcountInfo")
    public RespResult userCenter(@RequestHeader("uid") Integer uid){
        RespResult respResult=RespResult.fail();
        if (uid!=null&&uid>0) {
            UserAccountInfo userAccountInfo = userService.queryUserAccountInfo(uid);
            if (userAccountInfo!=null) {
                respResult=RespResult.ok();
                Map<String,Object> data=new HashMap<>();
                data.put("name",userAccountInfo.getName());
                data.put("phone",userAccountInfo.getPhone());
                if(userAccountInfo.getLastLoginTime()!=null){
                    data.put("lastLoginTime", DateFormatUtils
                            .format(userAccountInfo.getLastLoginTime(),"yyyy-MM-dd HH:mm:ss"));
                }else {
                    data.put("lastLoginTime","-");
                }
                data.put("headerUrl",userAccountInfo.getHeaderImage());
                data.put("balance",userAccountInfo.getAvailableMoney());
                respResult.setDate(data);
            }
        }else {
            respResult.setRcode(Rcode.REQUEST_PARAM_ERROR);
        }
        return respResult;
    }
    @ApiOperation(value = "判断用户是否登录",notes = "判断用户是否登录")
    @GetMapping ("/judge/login")
    public RespResult judgeLogin(@RequestHeader("uid")String headerUid,@RequestHeader("Authorization")  String headerToken){
        //获取token的值并进行验证
        RespResult respResult=RespResult.fail();//用户是否是登录状态8为未登录,7为登录
        try {
            if (StringUtils.isNotBlank(headerToken)) {
                String token = headerToken.substring(7);
                JwtUtil jwtUtil = new JwtUtil(jwtSecret);
                Claims jwtInfo = jwtUtil.readJwt(token);
                Integer jwtuid = jwtInfo.get("uid", Integer.class);
                if(headerUid!=null){
                    if(headerUid.equals(String.valueOf(jwtuid))){
                        //两者一样认为已经登录
                        respResult=RespResult.ok();
                    }
                }

            }
        }catch (Exception e){
            System.out.println("[info]"+e.getMessage());
        }
        return respResult;
    }
}
