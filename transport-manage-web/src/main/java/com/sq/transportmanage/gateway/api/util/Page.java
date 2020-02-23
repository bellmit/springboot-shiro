package com.sq.transportmanage.gateway.api.util;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: nysspring@163.com
 * @Description: 简单分页
 * @Date: 16:46 2019/7/11
 */
public class Page<T> implements Serializable {

    //当前页码
    private Integer page;
    //页面大小
    private Integer pageSize;
    //总记录数
    private Long total;
    //页面数据
    private List<T> dataList;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
