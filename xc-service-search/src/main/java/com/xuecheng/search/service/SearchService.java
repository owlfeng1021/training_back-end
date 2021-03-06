package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchService.class);

    @Value("${owl.course.index}")
    private String index;
    @Value("${owl.course.type}")
    private String type;
    @Value("${owl.course.source_field}")
    private String source_field;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam) {
        if (courseSearchParam == null) {
            courseSearchParam = new CourseSearchParam();
        }
        // 创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        // 设置搜索类型
        searchRequest.types(type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 过滤源字段
        String[] source_field_array = source_field.split(",");

        searchSourceBuilder.fetchSource(source_field_array, new String[]{});
        // 创建布尔查询对象
        // 搜索条件
        // 根据关键字搜索
        if (StringUtils.isNotEmpty((courseSearchParam.getKeyword()))) { // 使用multi match query
            // 匹配关键字
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(), "name", "teachplan", "description");
            // 设置匹配占比
            multiMatchQueryBuilder.minimumShouldMatch("70%");
            // 提升name字段的boost值
            multiMatchQueryBuilder.field("name", 10);

            boolQueryBuilder.must(multiMatchQueryBuilder);

        }
        // 根据分类查询
        if (StringUtils.isNotEmpty((courseSearchParam.getMt()))) {
            // 根据一级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt", courseSearchParam.getMt()));
        }
        if (StringUtils.isNotEmpty((courseSearchParam.getSt()))) {
            // 根据二级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("st", courseSearchParam.getSt()));
        }
        if (StringUtils.isNotEmpty((courseSearchParam.getGrade()))) {
            // 根据难度等级
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade", courseSearchParam.getGrade()));
        }
        // 分页效果
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 12;
        }
        // 起始记录数
        int from = (page - 1) * size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        // 设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        //  设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);
        // 布尔查询
        searchSourceBuilder.query(boolQueryBuilder);
        // 请求搜索
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("xuecheng search error..{}", e.getMessage());
            return new QueryResponseResult(CommonCode.SUCCESS, new QueryResult<CoursePub>());
        }
        // 处理返回的结果集
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        // 记录总数 传到 QueryResult
        long totalHits = hits.getTotalHits();
        // 数据列表
        ArrayList<CoursePub> list = new ArrayList<>();

        for (SearchHit Hit : searchHits) {
            CoursePub coursePub = new CoursePub();
            // 取出coursePub
            Map<String, Object> sourceAsMap = Hit.getSourceAsMap();

            // 取出需要的数据
            // id
            String id = (String) sourceAsMap.get("id");
            coursePub.setId(id);
            // 名称
            String name = (String) sourceAsMap.get("name");
            Map<String, HighlightField> highlightFields = Hit.getHighlightFields();
            if (highlightFields != null) {
                if (highlightFields.get("name") != null) {
                    HighlightField highlightField = highlightFields.get("name");
                    Text[] fragments = highlightField.fragments();
                    // 然后把字符串拼接起来
                    if (fragments != null) {
                        StringBuffer stringBuffer = new StringBuffer();
                        for (Text text : fragments) {
                            stringBuffer.append(text);
                        }
                        name = stringBuffer.toString();
                    }
                }
            }
            //高亮显示
            coursePub.setName(name);
            // 图片
            String pic = (String) sourceAsMap.get("pic");
            coursePub.setPic(pic);
            // 价格
            Double price = null;
            if (sourceAsMap.get("price") != null) {
                price = (Double) sourceAsMap.get("price");
            }

            coursePub.setPrice(price);
            // 以往的价格
            Double old_price = null;
            if (sourceAsMap.get("old_price") != null) {
                old_price = (Double) sourceAsMap.get("old_price");
            }

            coursePub.setPrice_old(old_price);
            list.add(coursePub);
        }
        QueryResult<CoursePub> queryResult = new QueryResult<>();
        queryResult.setList(list);
        queryResult.setTotal(totalHits);
        // 设置返回的结果
        QueryResponseResult<CoursePub> responseResult = new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);
        return responseResult;

    }
}

