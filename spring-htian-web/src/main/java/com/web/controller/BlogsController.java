package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dao.entity.BBlog;
import com.dao.mapper.BBlogMapper;
import com.web.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lizehao
 */
@Api(value = "LogController", description = "")
@RestController
@RequestMapping("/rest/blog")
public class BlogsController {

    private static final Logger logger = LoggerFactory.getLogger(BlogsController.class);

    @Autowired
    private BBlogMapper bBlogMapper;


    @ApiOperation(value = "新增")
    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public Result<BBlog> selectOne(@RequestBody BBlog record) {
        int flag = bBlogMapper.insert(record);
        return flag == 1 ? Result.success(): Result.error();
    }

    @ApiOperation(value = "查询文章评论")
    @RequestMapping(value = "/getCommentList", method = RequestMethod.GET)
    public Result<BBlog> getCommentList(@RequestParam Long blogId) {
        BBlog result = bBlogMapper.selectOne(new QueryWrapper<BBlog>().eq("blog_id",blogId));
        return Result.success(result);
    }


}
