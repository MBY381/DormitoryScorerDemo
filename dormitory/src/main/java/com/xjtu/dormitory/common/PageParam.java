package com.xjtu.dormitory.common;

public class PageParam {

    private Integer pageNum = 1;//页码
    private Integer pageSize = 5;//每页记录数

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
