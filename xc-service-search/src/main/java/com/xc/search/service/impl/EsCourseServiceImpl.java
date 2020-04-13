package com.xc.search.service.impl;

import com.xc.model.course.CoursePub;
import com.xc.model.course.TeachplanMediaPub;
import com.xc.model.search.CourseSearchParam;
import com.xc.ommon.model.response.CommonCode;
import com.xc.ommon.model.response.QueryResponseResult;
import com.xc.ommon.model.response.QueryResult;
import com.xc.search.config.EsIndexParams;
import com.xc.search.service.EsCourseService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EsCourseServiceImpl implements EsCourseService {

    @Autowired
    private EsIndexParams esIndexParams;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam) {
        if (courseSearchParam == null) {
            courseSearchParam = new CourseSearchParam();
        }
        // 构建搜索请求对象
        SearchRequest searchRequest = new SearchRequest(esIndexParams.getCourseIndex());
        // 搜索文档类型
        searchRequest.types(esIndexParams.getCourseIndexType());
        // 构建搜索源对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 指定过滤原字段
        sourceBuilder.fetchSource(esIndexParams.getFeilds().split(","), new String[]{});
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 根据关键字搜索
        MultiMatchQueryBuilder multiMatchQuery = null;
        if (StringUtils.isNotEmpty(courseSearchParam.getKeyword())) {
            multiMatchQuery = QueryBuilders
                    .multiMatchQuery(courseSearchParam.getKeyword(), "name", "description", "teachplan")
                    .minimumShouldMatch("70%")
                    .field("name", 10);
            boolQuery.must(multiMatchQuery);
        }
        TermQueryBuilder termQueryBuilder1 = null;
        // 过滤器
        if (StringUtils.isNotEmpty(courseSearchParam.getMt())) {
            // 根据一级分类
            termQueryBuilder1 = QueryBuilders.termQuery("mt", courseSearchParam.getMt());
            boolQuery.filter(termQueryBuilder1);
        }
        TermQueryBuilder termQueryBuilder2 = null;
        if (StringUtils.isNotEmpty(courseSearchParam.getSt())) {
            // 根据二级分类
            termQueryBuilder2 = QueryBuilders.termQuery("st", courseSearchParam.getSt());
            boolQuery.filter(termQueryBuilder2);
        }
        TermQueryBuilder termQueryBuilder3 = null;
        if (StringUtils.isNotEmpty(courseSearchParam.getGrade())) {
            // 根据难度等级
            termQueryBuilder3 = QueryBuilders.termQuery("grade", courseSearchParam.getGrade());
            boolQuery.filter(termQueryBuilder3);
        }
        if (page < 0) {
            page = 1;
        }
        if (size <= 0 || size > 20) {
            size = 10;
        }
        // 高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder().fragmentSize(8000).numOfFragments(0).requireFieldMatch(false);
        highlightBuilder.preTags("<span class='eslight'>");
        highlightBuilder.postTags("</span>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        // 分页
        sourceBuilder.from((page - 1) * size);
        sourceBuilder.size(size);
        sourceBuilder.query(boolQuery);
        sourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(sourceBuilder);

        QueryResult queryResult = new QueryResult();
        List<CoursePub> list = new ArrayList<>();
        // 执行搜索
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

            // 获取响应结果
            SearchHits responseHits = searchResponse.getHits();
            // 总记录数
            long totalHits = responseHits.totalHits;
            queryResult.setTotal(totalHits);
            SearchHit[] hits = responseHits.getHits();
            for (SearchHit hit : hits) {
                // 原始文档记录
                CoursePub coursePub = new CoursePub();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                // 名称
                String name = (String) sourceAsMap.get("name");
                coursePub.setName(name);
                // 取出id
                String id = (String) sourceAsMap.get("id");
                coursePub.setId(id);
                // 取出高亮字段
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if (highlightFields != null) {
                    HighlightField field = highlightFields.get("name");
                    if (field != null) {
                        Text[] fragments = field.fragments();
                        StringBuffer sb = new StringBuffer();
                        for (Text text : fragments) {
                            sb.append(text);
                        }
                        name = sb.toString();
                    }
                }
                coursePub.setName(name);
                // 图片
                String pic = (String) sourceAsMap.get("pic");
                coursePub.setPic(pic);
                // 价格
                Double price = null;
                try{
                    if (sourceAsMap.get("price") != null) {
                        price = (Double) sourceAsMap.get("price");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                coursePub.setPrice(price);
                // 老价格
                Double priceOld = null;
                try{
                    if (sourceAsMap.get("price_old") != null) {
                        priceOld = (Double) sourceAsMap.get("price_old");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                coursePub.setPrice_old(priceOld);
                list.add(coursePub);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        queryResult.setList(list);
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult );
    }


    @Override
    public Map<String, CoursePub> getall(String id) {

        SearchRequest searchRequest = new SearchRequest(esIndexParams.getCourseIndex());
        searchRequest.types(esIndexParams.getCourseIndexType());
        // 构建搜索源
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("id", id);
        sourceBuilder.query(queryBuilder);
        searchRequest.source(sourceBuilder);
        CoursePub coursePub = new CoursePub();
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] hitsHits = hits.getHits();
            Map<String, CoursePub> map = new HashMap<>();
            for (SearchHit hitsHit : hitsHits) {
                Map<String, Object> sourceAsMap = hitsHit.getSourceAsMap();
                String courseId = (String) sourceAsMap.get("id");
                String name = (String) sourceAsMap.get("name");
                String grade = (String) sourceAsMap.get("grade");
                String charge = (String) sourceAsMap.get("charge");
                String pic = (String) sourceAsMap.get("pic");
                String description = (String) sourceAsMap.get("description");
                String teachplan = (String) sourceAsMap.get("teachplan");
                coursePub.setId(courseId);
                coursePub.setName(name);
                coursePub.setGrade(grade);
                coursePub.setCharge(charge);
                coursePub.setPic(pic);
                coursePub.setDescription(description);
                coursePub.setTeachplan(teachplan);
                map.put(courseId, coursePub);
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public QueryResponseResult getMediaById(String[] teachplanIds) {
        SearchRequest searchRequest = new SearchRequest(esIndexParams.getCourseMediaIndex());
        searchRequest.types(esIndexParams.getCourseIndexType());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 多值查询
        TermsQueryBuilder termsQuery = QueryBuilders.termsQuery("teachplan_id", teachplanIds);
        // 过滤字段
        String[] split = esIndexParams.getMediaFeilds().split(",");
        sourceBuilder.fetchSource(split, new String[]{});
        sourceBuilder.query(termsQuery);
        searchRequest.source(sourceBuilder);

        List<TeachplanMediaPub> list = new ArrayList<>();
        long total = 0;
        try {
            // 执行搜索
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            total = hits.totalHits;
            for (SearchHit searchHit : searchHits) {
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
                String teachplanId = (String) sourceAsMap.get("teachplan_id");
                String mediaId = (String) sourceAsMap.get("media_id");
                String mediaUrl = (String) sourceAsMap.get("media_url");
                String courseid = (String) sourceAsMap.get("courseid");
                String mediaFileoriginalname = (String) sourceAsMap.get("media_fileoriginalname");
                teachplanMediaPub.setCourseId(courseid);
                teachplanMediaPub.setMediaFileOriginalName(mediaFileoriginalname);
                teachplanMediaPub.setMediaId(mediaId);
                teachplanMediaPub.setMediaUrl(mediaUrl);
                teachplanMediaPub.setTeachplanId(teachplanId);
                list.add(teachplanMediaPub);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        QueryResult queryResult = new QueryResult();
        queryResult.setList(list);
        queryResult.setTotal(total);
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }
}
