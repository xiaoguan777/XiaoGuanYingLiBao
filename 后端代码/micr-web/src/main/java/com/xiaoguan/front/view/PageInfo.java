package com.xiaoguan.front.view;

import com.xiaoguan.common.util.CommonUtil;

import java.io.Serializable;
import java.util.Objects;

//分页数据类
public class PageInfo implements Serializable {
    public PageInfo(Integer pageNo, Integer pageSize, Integer totalRecord) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
        if (pageSize>0&&totalRecord>0) {
            Double nums = Double.valueOf(totalRecord+"");
            Integer totalPage =(int) Math.round( Math.ceil(nums / pageSize));
            this.setTotalPage(totalPage);
        }else {
            this.setTotalPage(0);
        }

    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", totalRecord=" + totalRecord +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageInfo pageInfo = (PageInfo) o;
        return Objects.equals(getPageNo(), pageInfo.getPageNo()) && Objects.equals(getPageSize(), pageInfo.getPageSize()) && Objects.equals(getTotalPage(), pageInfo.getTotalPage()) && Objects.equals(getTotalRecord(), pageInfo.getTotalRecord());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPageNo(), getPageSize(), getTotalPage(), getTotalRecord());
    }

    public PageInfo() {
    }

    public PageInfo(Integer pageNo, Integer pageSize, Integer totalPage, Integer totalRecord) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalRecord = totalRecord;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    //页号
    private Integer pageNo;
    //每页的大小
    private Integer pageSize;
    //总页数
    private Integer totalPage;
    //总记录数
    private Integer totalRecord;
}
