package com.bean.es;

import lombok.Data;

@Data
public class EsUserVO {
    private String id;
    private String name;
    private String desc;
    private String birthday;
    private Integer age;
}
