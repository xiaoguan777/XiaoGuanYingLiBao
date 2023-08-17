package com.xiaoguan.front.view;

import com.xiaoguan.common.enums.Rcode;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 统一的应答结果类,controller方法的返回值都是它
 */
public class RespResult implements Serializable {
    private int code;//应答码,自定义数字用来标识
    private String msg;//code的文字说明,一般做提示给用户看
    private Object date;//单个数据
    private List list;//集合数据
    private PageInfo pageInfo;//分页数据
    private String accessToken;//访问token

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "RespResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", date=" + date +
                ", list=" + list +
                ", pageInfo=" + pageInfo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RespResult that = (RespResult) o;
        return getCode() == that.getCode() && Objects.equals(getMsg(), that.getMsg()) && Objects.equals(getDate(), that.getDate()) && Objects.equals(getList(), that.getList()) && Objects.equals(getPageInfo(), that.getPageInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getMsg(), getDate(), getList(), getPageInfo());
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public RespResult(int code, String msg, Object date, List list, PageInfo pageInfo) {
        this.code = code;
        this.msg = msg;
        this.date = date;
        this.list = list;
        this.pageInfo = pageInfo;
    }

    public RespResult(int code, String msg, Object date, List list) {
        this.code = code;
        this.msg = msg;
        this.date = date;
        this.list = list;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public void setRcode(Rcode rcode){
        this.code=rcode.getCode();
        this.msg=rcode.getText();
    }
    //表示成功的ResultSet对象
    public static RespResult ok(){
        RespResult respResult = new RespResult();
        respResult.setRcode(Rcode.SUCCESS);
        return respResult;
    }
    //表示失败的RespResult对象
    public static RespResult fail(){
        RespResult respResult = new RespResult();
       respResult.setRcode(Rcode.UNKOWN);
        return respResult;
    }

    public RespResult(int code, String msg, Object date) {
        this.code = code;
        this.msg = msg;
        this.date = date;
    }

    public RespResult() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }
}
