package com.controller;

import com.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lizehao
 */
@Api(value = "CallBackController", description = "回调接口及其他服务调用的接口（前端忽略）")
@RestController
@RequestMapping("/rest/callback")
public class CallBackController {

    private static final Logger logger = LoggerFactory.getLogger(CallBackController.class);

    @Autowired
    private RestHighLevelClient restClient;

    @ApiOperation(value = "查询")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Result<Map<String, Object>> get(@RequestParam String index ,@RequestParam String type,@RequestParam String id) throws Exception {
        GetRequest request = new GetRequest(index,type,id);
        GetResponse response = restClient.get(request);
        return Result.success(response.getSource());
    }

    @ApiOperation(value = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public Result<UpdateResponse> test(@RequestParam String index ,@RequestParam String type,@RequestParam String id,@RequestParam String desc) throws Exception {
        Map<String,Object> doc = new HashMap<>();
        doc.put("desc",desc);
        UpdateRequest request = new UpdateRequest(index,type,id);
        request.doc(doc);
        UpdateResponse updateResponse = restClient.update(request);
        return Result.success(updateResponse);
    }
}
