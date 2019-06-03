package com.xuecheng.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class testSearch {
    @Autowired
    RestHighLevelClient client;
    @Autowired
    RestClient restClient;

    // 分页查询
    @Test
    public void testSearchPage() throws IOException, ParseException {
        // 指定请求搜索对象
        SearchRequest searchRequest = new SearchRequest("course");
        // 设置类型
        searchRequest.types("doc");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置分页参数
        // 页码
        // 下标
        // 公式就是 页码=（页码-1）* 下标
        searchSourceBuilder.from(1); // 启始记录下标
        searchSourceBuilder.size(1);// 每页的记录数量
        // 设置请求条件 分页操作
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // source源字段过滤
        // 第一参数是includes 第二个参数是 excludes
        SearchSourceBuilder searchSourceBuilder1 = searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "price", "timestamp"}, new String[]{});
        // 向请求对象设置请求源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索结果
        SearchResponse response = client.search(searchRequest);
        // 搜索结果
        SearchHits hits = response.getHits();
        // 得到匹配度高的结果
        SearchHit[] hitsArray = hits.getHits();
        // 声明一个时间转换类型‐
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 遍历搜索的内容
        for (SearchHit hit : hitsArray) {
            // 主键
            hit.getId();
            // 拿到里面的内容 使用map形式
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date date = simpleDateFormat.parse((String) sourceAsMap.get("timestamp"));
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(price);
            System.out.println(date);
        }

    }
    // 搜索全部的内容
    @Test
    public void testSearch() throws IOException, ParseException {
        // 指定请求搜索对象
        SearchRequest searchRequest = new SearchRequest("course");
        // 设置类型
        searchRequest.types("doc");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置请求条件 （搜索全部）
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // source源字段过滤
        // 第一参数是includes 第二个参数是 excludes  
        SearchSourceBuilder searchSourceBuilder1 = searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "price", "timestamp"}, new String[]{});
        // 向请求对象设置请求源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索结果
        SearchResponse response = client.search(searchRequest);
        // 搜索结果
        SearchHits hits = response.getHits();
        // 得到匹配度高的结果
        SearchHit[] hitsArray = hits.getHits();
        // 声明一个时间转换类型‐
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 遍历搜索的内容
        for (SearchHit hit : hitsArray) {
            // 主键
            hit.getId();
            // 拿到里面的内容 使用map形式
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date date = simpleDateFormat.parse((String) sourceAsMap.get("timestamp"));
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(price);
            System.out.println(date);
        }

    }
    // 精确搜索
    @Test
    public void testSearchTerm() throws IOException, ParseException {
        // 指定请求搜索对象
        SearchRequest searchRequest = new SearchRequest("course");
        // 设置类型
        searchRequest.types("doc");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置请求条件
        searchSourceBuilder.query(QueryBuilders.termQuery("name","spring"));
        // source源字段过滤
        // 第一参数是includes 第二个参数是 excludes
        SearchSourceBuilder searchSourceBuilder1 = searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "price", "timestamp"}, new String[]{});
        // 向请求对象设置请求源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索结果
        SearchResponse response = client.search(searchRequest);
        // 搜索结果
        SearchHits hits = response.getHits();
        // 得到匹配度高的结果
        SearchHit[] hitsArray = hits.getHits();
        // 声明一个时间转换类型‐
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 遍历搜索的内容
        for (SearchHit hit : hitsArray) {
            // 主键
            hit.getId();
            // 拿到里面的内容 使用map形式
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date date = simpleDateFormat.parse((String) sourceAsMap.get("timestamp"));
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(price);
            System.out.println(date);
        }

    }
    // 根据ids查询
    @Test
    public void testSearchByIds() throws IOException, ParseException {
        // 指定请求搜索对象
        SearchRequest searchRequest = new SearchRequest("course");
        // 设置类型
        searchRequest.types("doc");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置请求条件
        String[]  ids=new String[]{"kOXoFmsBGOwzIRAxQ19P","keXoFmsBGOwzIRAxQ19P"};
//        searchSourceBuilder.query(QueryBuilders.termQuery("_id",ids));
        // 使用多个参数的时候 注意要使用ternsQuery的基本操作
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id",ids));
        // source源字段过滤
        // 第一参数是includes 第二个参数是 excludes
        SearchSourceBuilder searchSourceBuilder1 = searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "price", "timestamp"}, new String[]{});
        // 向请求对象设置请求源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索结果
        SearchResponse response = client.search(searchRequest);
        // 搜索结果
        SearchHits hits = response.getHits();
        // 得到匹配度高的结果
        SearchHit[] hitsArray = hits.getHits();
        // 声明一个时间转换类型‐
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 遍历搜索的内容
        for (SearchHit hit : hitsArray) {
            // 主键
            hit.getId();
            // 拿到里面的内容 使用map形式
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date date = simpleDateFormat.parse((String) sourceAsMap.get("timestamp"));
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(price);
            System.out.println(date);
        }

    }
    // 根据match查询 设置查询方式
    @Test
    public void testSearchMatch() throws IOException, ParseException {
        // 指定请求搜索对象
        SearchRequest searchRequest = new SearchRequest("course");
        // 设置类型
        searchRequest.types("doc");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置请求条件
        String[]  ids=new String[]{"kOXoFmsBGOwzIRAxQ19P","keXoFmsBGOwzIRAxQ19P"};
        // searchSourceBuilder.query(QueryBuilders.termQuery("_id",ids));
        // 使用多个参数的时候 注意要使用ternsQuery的基本操作
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id",ids));
        // source源字段过滤
        // 第一参数是includes 第二个参数是 excludes
        SearchSourceBuilder searchSourceBuilder1 = searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "price", "timestamp"}, new String[]{});
        // 向请求对象设置请求源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索结果
        SearchResponse response = client.search(searchRequest);
        // 搜索结果
        SearchHits hits = response.getHits();
        // 得到匹配度高的结果
        SearchHit[] hitsArray = hits.getHits();
        // 声明一个时间转换类型‐
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 遍历搜索的内容
        for (SearchHit hit : hitsArray) {
            // 主键
            hit.getId();
            // 拿到里面的内容 使用map形式
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date date = simpleDateFormat.parse((String) sourceAsMap.get("timestamp"));
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(price);
            System.out.println(date);
        }

    }

}
