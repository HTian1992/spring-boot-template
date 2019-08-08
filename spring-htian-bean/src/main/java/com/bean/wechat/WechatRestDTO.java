package com.bean.wechat;

import lombok.Data;

@Data
public class WechatRestDTO {

    private Integer errcode;

    private String errmsg;

    private Long msgid;
}
