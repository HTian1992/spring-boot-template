package com.controller;

import com.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lizehao
 */
@Api(value = "CallBackController", description = "")
@RestController
@RequestMapping("/rest/callback")
public class CallBackController {

    private static final Logger logger = LoggerFactory.getLogger(CallBackController.class);

    @Autowired
    private RestHighLevelClient restClient;

    @ApiOperation(value = "查询")
    @RequestMapping(value = "/load", method = RequestMethod.GET)
    public Result<Map<String, Object>> load(@RequestParam String index ,@RequestParam String type,@RequestParam String id) throws Exception {
        GetRequest request = new GetRequest(index,type,id);
        GetResponse response = restClient.get(request);
        return Result.success(response.getSource());
    }

    @ApiOperation(value = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public Result<UpdateResponse> update(@RequestParam String index ,@RequestParam String type,@RequestParam String id,@RequestParam String desc) throws Exception {
        Map<String,Object> doc = new HashMap<>();
        doc.put("desc",desc);
        UpdateRequest request = new UpdateRequest(index,type,id);
        request.doc(doc);
        UpdateResponse updateResponse = restClient.update(request);
        return Result.success(updateResponse);
    }

    @ApiOperation(value = "批量更新")
    @RequestMapping(value = "/bulkUpdate", method = RequestMethod.GET)
    public Result<BulkResponse> bulkUpdate(@RequestParam String index ,@RequestParam String type,@RequestParam List<String> ids,@RequestParam List<String> desc) throws Exception {
        BulkRequest request = new BulkRequest();
        for(int i = 0 ; i < ids.size() ;i++){
            Map<String,Object> source = new HashMap<>();
            source.put("desc",desc.get(i));
            UpdateRequest doc = new UpdateRequest(index,type,ids.get(i));
            doc.doc(source);
            request.add(doc);
        }
        BulkResponse updateResponse = restClient.bulk(request);
        return Result.success(updateResponse);
    }

    @ApiOperation(value = "新增")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public Result<IndexResponse> add(@RequestParam String index , @RequestParam String type, @RequestParam String id, @RequestParam String desc) throws Exception {
        Map<String,Object> doc = new HashMap<>();
        doc.put("name","遂溪张学友");
        doc.put("age","28");
        doc.put("desc",desc);
        doc.put("birthday","1992-12-11");
        IndexRequest request = new IndexRequest(index, type, id);
        request.source(doc);
        IndexResponse response = restClient.index(request);
        return Result.success(response);
    }

    @ApiOperation(value = "批量新增")
    @RequestMapping(value = "/bulkAdd", method = RequestMethod.GET)
    public Result<BulkResponse> bulkAdd(@RequestParam String index , @RequestParam String type, @RequestParam String id, @RequestParam String desc) throws Exception {
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0; i <3; i++){
            Map<String,Object> source = new HashMap<>();
            source.put("name","遂溪张学友");
            source.put("age","28");
            source.put("desc",desc);
            source.put("birthday","1992-12-11");
            IndexRequest doc = new IndexRequest(index, type, id);
            doc.source(source);
            bulkRequest.add(doc);
        }
        BulkResponse response = restClient.bulk(bulkRequest);
        return Result.success(response);
    }

    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Result<DeleteResponse> bulkDelete(@RequestParam String index , @RequestParam String type, @RequestParam String id) throws Exception {
        DeleteRequest request = new DeleteRequest(index,type,id);
        DeleteResponse response = restClient.delete(request);
        return Result.success(response);
    }

    @ApiOperation(value = "批量删除")
    @RequestMapping(value = "/bulkDelete", method = RequestMethod.GET)
    public Result<BulkResponse> delete(@RequestParam String index , @RequestParam String type, @RequestParam List<String> ids) throws Exception {
        BulkRequest bulkRequest = new BulkRequest();
        for (String id : ids){
            DeleteRequest doc = new DeleteRequest(index,type, id);
            bulkRequest.add(doc);
        }
        BulkResponse response = restClient.bulk(bulkRequest);
        return Result.success(response);
    }

    @ApiOperation(value = "列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<SearchHits> heighLoad(@RequestParam String index , @RequestParam String type) throws Exception {
        SearchRequest request = new SearchRequest();
        request.indices(index).types(type);
        SearchHits response = restClient.search(request).getHits();
        return Result.success(response);
    }

    @ApiOperation(value = "关键字搜索")
    @RequestMapping(value = "/keyword", method = RequestMethod.GET)
    public Result<SearchResponse> keyword(@RequestParam("index") String index, @RequestParam("type") String type, @RequestParam(value = "name",required = false) String name,@RequestParam(value = "keyword" , required = false) String keyword) throws Exception {

        SearchRequest request = new SearchRequest();
        //索引
        request.indices(index).types(type);
        //各种组合条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //组合条件，或 | 去匹配查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(StringUtils.isNotEmpty(name)){
            boolQueryBuilder.should(QueryBuilders.termQuery("name",name));
        }
        if(StringUtils.isNotEmpty(keyword)){
            boolQueryBuilder.should(QueryBuilders.matchQuery("desc",keyword));
        }
        sourceBuilder.explain(false).profile(false).from(0).size(10)
                .postFilter(boolQueryBuilder);

//        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("birthday").gte("1998-12-11");
//        boolQueryBuilder.must(rangeQueryBuilder);
//        QueryBuilders.boostingQuery(queryBuilder,matchQueryBuilder)
        request.source(sourceBuilder);
        SearchResponse response = restClient.search(request);
        return Result.success(response);
    }
}
