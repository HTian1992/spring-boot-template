package com.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-08-13
 */
@ApiModel(value="BComment对象", description="")
public class BComment extends Model<BComment> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "文章主键id")
    private Long blogId;

    @ApiModelProperty(value = "用户主键id")
    private Long userId;

    @ApiModelProperty(value = "被评论方id")
    private Integer refUserId;

    @ApiModelProperty(value = "内容")
    private String content;

    private LocalDateTime createDate;

    @ApiModelProperty(value = "偏移量，用于排序")
    private Integer offset;

    @ApiModelProperty(value = "是否为第一次评论")
    private Integer isRoot;

    @ApiModelProperty(value = "标记ID，用于识别为哪个评论/回复")
    private Integer flagId;

    @ApiModelProperty(value = "评论方名称")
    private String username;

    @ApiModelProperty(value = "被评论方名称")
    private String refUsername;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRefUserId() {
        return refUserId;
    }

    public void setRefUserId(Integer refUserId) {
        this.refUserId = refUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Integer isRoot) {
        this.isRoot = isRoot;
    }

    public Integer getFlagId() {
        return flagId;
    }

    public void setFlagId(Integer flagId) {
        this.flagId = flagId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRefUsername() {
        return refUsername;
    }

    public void setRefUsername(String refUsername) {
        this.refUsername = refUsername;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BComment{" +
        "id=" + id +
        ", blogId=" + blogId +
        ", userId=" + userId +
        ", refUserId=" + refUserId +
        ", content=" + content +
        ", createDate=" + createDate +
        ", offset=" + offset +
        ", isRoot=" + isRoot +
        ", flagId=" + flagId +
        ", username=" + username +
        ", refUsername=" + refUsername +
        "}";
    }
}
