package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dao.entity.BLog;
import com.dao.mapper.BLogMapper;
import com.web.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lizehao
 */
@Api(value = "LogController", description = "")
@RestController
@RequestMapping("/rest/log")
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private BLogMapper bLogMapper;


    @ApiOperation(value = "")
    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public Result<BLog> selectOne(@RequestParam Long key) {
        BLog a = bLogMapper.selectOne(new QueryWrapper<BLog>().eq("id",key));
        return Result.success(a);
    }


}
