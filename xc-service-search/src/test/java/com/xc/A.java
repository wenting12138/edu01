package com.xc;

import com.xc.search.SearchMain;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = SearchMain.class)
@RunWith(SpringRunner.class)
public class A {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private RestClient restClient;

    @Test
    public void deleteIndex() throws IOException {
        // 删除索引库
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");
        IndicesClient indicesClient = restHighLevelClient.indices();
        DeleteIndexResponse delete = indicesClient.delete(deleteIndexRequest);
        boolean b = delete.isAcknowledged();
        System.out.println(b);
    }

    @Test
    public void createIndex() throws IOException {
        // 创建索引库
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        // 设置参数
        createIndexRequest.settings(Settings.builder().put("number_of_shards", "1").put("number_of_replicas", "0").build());
        createIndexRequest.mapping("doc", "{\n" +
                "    \"xc_course\": {\n" +
                "        \"mappings\": {\n" +
                "            \"doc\": {\n" +
                "                \"properties\": {\n" +
                "                    \"description\": {\n" +
                "                        \"type\": \"text\"\n" +
                "                    },\n" +
                "                    \"name\": {\n" +
                "                        \"type\": \"text\"\n" +
                "                    },\n" +
                "                    \"studymodel\": {\n" +
                "                        \"type\": \"keyword\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}", XContentType.JSON);
        // 操作索引的客户端
        IndicesClient indicesClient = restHighLevelClient.indices();
        // 返回响应
        CreateIndexResponse createIndexResponse = indicesClient.create(createIndexRequest);
        boolean b = createIndexResponse.isAcknowledged();
        System.out.println(b);
    }

    // 添加
    public void add() throws IOException {

        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", "23");
        IndexRequest indexRequest = new IndexRequest("xc_course", "doc");
        indexRequest.source(map);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest);
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println(result);
    }

    // 查询
    public void get() throws IOException {
        // 查询请求对象
        GetRequest getRequest = new GetRequest("xc_course", "doc", "xxxxxxx");
        GetResponse getResponse = restHighLevelClient.get(getRequest);
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        System.out.println(sourceAsMap);
    }

    @Test
    public void search() throws IOException {
        // 构建搜索对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        // 构建搜索源对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 搜索方式:
        //  1. 匹配全部
//        sourceBuilder.query(QueryBuilders.matchAllQuery());
        //  2. termQuery 根据属性进行查询  不会分词, 适用于精确查询
//        sourceBuilder.query(QueryBuilders.termQuery("name", "spring"));
        // 3.  根据id查询 , 精确匹配  termsQuery
//        sourceBuilder.query(QueryBuilders.termsQuery("_id", new String[]{"1", "2"}));
        // 4. 全文检索 会分词  minimumShouldMatch: 假设spring开发框架分为三个词, 70% * 3 = 2.1 , 只有包含两个以上才可以搜索到
//        sourceBuilder.query(QueryBuilders.matchQuery("description", "spring开发框架").minimumShouldMatch("70%"));
        // operator: and or , spring开发分为两个词, and是两个词都有才能搜索到, or是有一个就可以搜索到
//        sourceBuilder.query(QueryBuilders.matchQuery("description", "spring开发").operator(Operator.OR));
        //5.  multiMatchQuery: 多个域进行搜索   field("name", 10)): 权重提升十倍  提升boost
        // sourceBuilder.query(QueryBuilders.multiMatchQuery("spring css", "name", "description").
        //         minimumShouldMatch("50%").
        //         field("name", 10));  // 权重提升十倍
        // 设置过滤字段, param1. 包括哪些字段,  param2. 不包括哪些字段
        // 6. booleans搜索
        sourceBuilder.fetchSource(new String[]{"name", "studymodel", "price", "timestamp"}, new String[]{});
        // 设置分页
        // 页码
        int page = 1;
        // 每页记录数
        int size = 10;
        int from = (page - 1) * size;
        sourceBuilder.from(from);  // 起始下标
        sourceBuilder.size(size); // 每页显示的记录数
        // 向搜索对象设置搜索源
        searchRequest.source(sourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        // 搜索结果
        SearchHits hits = searchResponse.getHits();
        // 匹配总记录数
        long totalHits = hits.getTotalHits();
        // 得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            // 文档主键
            String id = searchHit.getId();
            // 源文档内容
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");

        }
    }

    // boolean 查询
    @Test
    public void bool() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 定义一个 MultiMatchQueryBuilder
        MultiMatchQueryBuilder matchQueryBuilder = QueryBuilders.multiMatchQuery("spring css", "name", "description").
                field("name", 10).minimumShouldMatch("50%");
        // 定义一个 TermQueryBuilder
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "spring");
        // 定义一个 BoolQueryBuilder
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(matchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);
        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hitsHits = hits.getHits();
        for (SearchHit hitsHit : hitsHits) {
            System.out.println(hitsHit.getSourceAsMap());
        }
    }

    // 过滤器, 根据搜索结果进行过滤, 不会去计算得分和匹配度, 所以性能更好一点
    @Test
    public void filterResult(){
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring css", "name", "description").
                field("name", 10).minimumShouldMatch("70%");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);
        // 过滤器
        // 根据studymodel 搜索
        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel", "201001"));
        // 范围搜索
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));
        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);
    }

    // 排序
    @Test
    public void sort(){
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 过滤器
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("price").gte(60).lte(100);
        boolQuery.filter(rangeQueryBuilder);
        sourceBuilder.query(boolQuery);
        // 按照price进行降序
        sourceBuilder.sort("price", SortOrder.DESC);
        // 按照studymodel进行升序
        sourceBuilder.sort("studymodel", SortOrder.ASC);
    }

    // 高亮显示
    @Test
    public void highLight() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders
                .multiMatchQuery("spring开发基础", "name")
                .field("name", 10);
        // 过滤器
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("price").gte(0).lte(100);
        boolQuery.must(rangeQueryBuilder);
        boolQuery.must(multiMatchQueryBuilder);
        // 排序
        sourceBuilder.sort("price", SortOrder.DESC);
        // 设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color: red'>");
        highlightBuilder.postTags("</span");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
//        highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
        // 设置到搜索源
        sourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            // 取出源文档
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            // 取出高亮显示的内容
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            if (highlightFields != null) {
                // 取出高亮字段
                HighlightField highlightField = highlightFields.get("name");
                if (highlightField != null) {
                    Text[] fragments = highlightField.fragments();
                    StringBuffer buffer = new StringBuffer();
                    for (Text fragment : fragments) {
                        buffer.append(fragment);
                    }
                    name = buffer.toString();
                }
            }
        }
    }
}
