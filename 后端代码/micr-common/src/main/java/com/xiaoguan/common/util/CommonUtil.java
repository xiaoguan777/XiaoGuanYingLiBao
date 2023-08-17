package com.xiaoguan.common.util;

import java.util.regex.Pattern;

public class CommonUtil {
    //处理pageNo
    public static int defaultPageNo(Integer pageno){
        int pNo=pageno;
        if(pageno==null||pageno<1){
            pNo=1;
        }
        return pNo;
    }
    //处理pageSize
    public static int defaultPageSize(Integer pageSize){
        int pNo=pageSize;
        if (pageSize==null ||pageSize<1) {
            pNo=1;
        }
        return pageSize;
    }
    //手机号脱敏
    public static String tuoMinPhone(String phone){
        String result="***********";
        if(phone!=null&&phone.trim().length()>5){
            result=phone.substring(0,3)+"******"+phone.substring(phone.length()-2,phone.length());
        }
        return result;
    }
    //检查手机格式
    public static Boolean checkPhone(String phone){
        boolean flag=false;
        if(phone!=null){
            flag = Pattern.matches("^1[1-9]\\d{9}$", phone);
        }
        return flag;
    }
}
