package com.jqy.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jqy.admin.model.po.Logs;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName LogsVo
 * @Author 阳
 * @Date 2021/2/21 10:37
 * @Version 1.0
 **/

public class LogsVo extends Logs {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endDate;
    private Integer current;

    private Integer size;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
