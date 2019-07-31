package com.web.controller;

import com.bean.es.EsUserVO;
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
import org.springframework.web.bind.annotation.*;
import com.web.base.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lizehao
 */
@Api(value = "CallBackController", description = "")
@RestController
@RequestMapping("/rest/callback")
public class EsUserController {

    private static final Logger logger = LoggerFactory.getLogger(EsUserController.class);

    private static String index = "test2";

    private static String type = "d1";

    @Autowired
    private RestHighLevelClient restClient;

    @ApiOperation(value = "查询")
    @RequestMapping(value = "/load", method = RequestMethod.GET)
    public Result<Map<String, Object>> load(@RequestParam String id) throws Exception {
        GetRequest request = new GetRequest(index,type,id);
        GetResponse response = restClient.get(request);
        return Result.success(response.getSource());
    }

    @ApiOperation(value = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result<UpdateResponse> update(@RequestBody EsUserVO vo) throws Exception {
        Map<String,Object> doc = new HashMap<>();
        doc.put("name",vo.getName());
        doc.put("birthday",vo.getBirthday());
        doc.put("desc",vo.getDesc());
        doc.put("age",vo.getAge());
        UpdateRequest request = new UpdateRequest(index,type,vo.getId());
        request.doc(doc);
        UpdateResponse updateResponse = restClient.update(request);
        return Result.success(updateResponse);
    }

    @ApiOperation(value = "批量更新")
    @RequestMapping(value = "/bulkUpdate", method = RequestMethod.POST)
    public Result<BulkResponse> bulkUpdate(@RequestBody List<EsUserVO> list) throws Exception {
        BulkRequest request = new BulkRequest();
        for(int i = 0 ; i < list.size() ;i++){
            Map<String,Object> source = new HashMap<>();
            source.put("name",list.get(i).getName());
            source.put("birthday",list.get(i).getBirthday());
            source.put("desc",list.get(i).getDesc());
            source.put("age",list.get(i).getAge());
            UpdateRequest doc = new UpdateRequest(index,type,list.get(i).getId());
            doc.doc(source);
            request.add(doc);
        }
        BulkResponse updateResponse = restClient.bulk(request);
        return Result.success(updateResponse);
    }

    @ApiOperation(value = "新增")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<IndexResponse> add(@RequestBody EsUserVO vo) throws Exception {
        Map<String,Object> doc = new HashMap<>();
        doc.put("name",vo.getName());
        doc.put("birthday",vo.getBirthday());
        doc.put("desc",vo.getDesc());
        doc.put("age",vo.getAge());
        IndexRequest request = new IndexRequest(index, type);
        request.source(doc);
        IndexResponse response = restClient.index(request);
        return Result.success(response);
    }

    @ApiOperation(value = "批量新增")
    @RequestMapping(value = "/bulkAdd", method = RequestMethod.POST)
    public Result<BulkResponse> bulkAdd(@RequestBody List<EsUserVO> list) throws Exception {
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0; i <list.size(); i++){
            Map<String,Object> source = new HashMap<>();
            source.put("name",list.get(i).getName());
            source.put("birthday",list.get(i).getBirthday());
            source.put("desc",list.get(i).getDesc());
            source.put("age",list.get(i).getAge());;
            IndexRequest doc = new IndexRequest(index, type);
            doc.source(source);
            bulkRequest.add(doc);
        }
        BulkResponse response = restClient.bulk(bulkRequest);
        return Result.success(response);
    }

    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Result<DeleteResponse> bulkDelete(@RequestParam String id) throws Exception {
        DeleteRequest request = new DeleteRequest(index,type,id);
        DeleteResponse response = restClient.delete(request);
        return Result.success(response);
    }

    @ApiOperation(value = "批量删除")
    @RequestMapping(value = "/bulkDelete", method = RequestMethod.GET)
    public Result<BulkResponse> delete(@RequestParam List<String> ids) throws Exception {
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
    public Result<SearchHits> heighLoad() throws Exception {
        SearchRequest request = new SearchRequest(index);
        SearchHits response = restClient.search(request).getHits();
        return Result.success(response);
    }

    @ApiOperation(value = "关键字搜索")
    @RequestMapping(value = "/keyword", method = RequestMethod.GET)
    public Result<SearchResponse> keyword(@RequestParam(value = "name",required = false) String name,
                                          @RequestParam(value = "keyword" , required = false) String keyword) throws Exception {

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
