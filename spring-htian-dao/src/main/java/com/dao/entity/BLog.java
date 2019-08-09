package com.dao.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author lizehao
 * @since 2019-08-09
 */
@ApiModel(value="BLog对象", description="")
public class BLog extends Model<BLog> {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String logContent;

    private LocalDateTime createDate;

    private Long operationId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BLog{" +
        "id=" + id +
        ", logContent=" + logContent +
        ", createDate=" + createDate +
        ", operationId=" + operationId +
        "}";
    }
}
