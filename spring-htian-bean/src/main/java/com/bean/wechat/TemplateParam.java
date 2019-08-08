package com.bean.wechat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TemplateParam {

    private String value;

    private String color;

    @Override
    public String toString(){
        return "{\"value\":\""+value+"\",\"color\":\""+color+"\"}";
    }
}
