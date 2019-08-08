package com.bean.wechat;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TemplateMessageParam {

    private String touser;

    private String template_id;

    private String url;

    private Map<String,TemplateParam> data;
}
