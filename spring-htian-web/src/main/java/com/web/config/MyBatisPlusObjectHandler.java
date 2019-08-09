package com.web.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 在插入和更新数据时自动填充时间
 * @author lqh
 * @date 2019/3/28
 */
public class MyBatisPlusObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName("createDate", now, metaObject);
        this.setFieldValByName("updateDate", now, metaObject);
        this.setFieldValByName("delFlag", false, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateDate", LocalDateTime.now(), metaObject);
    }

}
