package com;

import com.netflix.discovery.converters.Auto;
import com.xc.course_manager.CourseManagerMain;
import com.xc.course_manager.dao.*;
import com.xc.model.course.Category;
import com.xc.model.course.CoursePub;
import com.xc.model.course.Teachplan;
import com.xc.model.course.TeachplanMediaPub;
import com.xc.model.course.ext.CategoryNode;
import com.xc.model.course.ext.CourseInfo;
import com.xc.model.course.ext.TeachplanNode;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest(classes = CourseManagerMain.class)
@RunWith(SpringRunner.class)
public class T {

    @Autowired
    private CategoryDao categoryDao;

    @Test
    public void test(){
        Category byId = categoryDao.findById("1");
        System.out.println(categoryDao);
    }

    @Autowired
    private TeachplanDao teachplanDao;

    @Test
    public void test1(){
        TeachplanNode list = teachplanDao.findTeachplanList("4028e58161bcf7f40161bcf8b77c0000");
        System.out.println(list);
    }
    @Test
    public void test2(){
        List<Teachplan> list = teachplanDao.findTeachListByCourseIdAndParentId("4028e58161bcf7f40161bcf8b77c0000", "0");
        System.out.println(list);
    }
    @Test
    public void test3(){
        System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
    }

    @Test
    public void test4(){
        Teachplan byId = teachplanDao.findById("4028e58161bd14c20161bd14f1520000");
        System.out.println(byId);
    }

    @Test
    public void test5(){
        CategoryNode list = categoryDao.findList("1");
        System.out.println(list);
    }

    @Autowired
    private CourseBaseDao courseBaseDao;
    @Test
    public void test6(){
        List<CourseInfo> limitPage = courseBaseDao.findLimitPage(3, 10, null);
        System.out.println(limitPage);
    }

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private CoursePubDao coursePubDao;
    // 添加
    @Test
    public void add() throws IOException {

        Map<String, Object> map = new HashMap<>();
        List<CoursePub> list = coursePubDao.findAll();
        for (CoursePub coursePub : list) {
            IndexRequest indexRequest = new IndexRequest("xc_courses", "doc");
            map.put("id", coursePub.getId());
            map.put("name", coursePub.getName());
            map.put("users", coursePub.getUsers());
            map.put("mt", coursePub.getMt());
            map.put("st", coursePub.getSt());
            map.put("grade", coursePub.getGrade());
            map.put("studymodel", coursePub.getStudymodel());
            map.put("teachmode", coursePub.getTeachmode());
            map.put("description", coursePub.getDescription());
            map.put("pic", coursePub.getPic());
            map.put("timestamp", coursePub.getTimestamp());
            map.put("charge", coursePub.getCharge());
            map.put("valid", coursePub.getValid());
            map.put("price", coursePub.getPrice());
            map.put("price_old", coursePub.getPrice_old());
            map.put("expires", coursePub.getExpires());
            map.put("teachplan", coursePub.getTeachplan());
            map.put("pub_time", coursePub.getPubTime());
            map.put("start_time", coursePub.getStart_time());
            map.put("end_time", coursePub.getEnd_time());
            indexRequest.source(map);
            restHighLevelClient.index(indexRequest);
        }

    }

    @Autowired
    private TeachplanMediaPubDao teachplanMediaPubDao;

    @Test
    public void add11() throws IOException {

        Map<String, Object> map = new HashMap<>();
        List<TeachplanMediaPub> list = teachplanMediaPubDao.findAll();
        for (TeachplanMediaPub TeachplanMediaPub : list) {
            IndexRequest indexRequest = new IndexRequest("xc_course_media", "doc");
            map.put("teachplan_id", TeachplanMediaPub.getTeachplanId());
            map.put("media_id", TeachplanMediaPub.getMediaId());
            map.put("courseid", TeachplanMediaPub.getCourseId());
            map.put("media_url", TeachplanMediaPub.getMediaUrl());
            map.put("media_fileoriginalname", TeachplanMediaPub.getMediaFileOriginalName());
            indexRequest.source(map);
            restHighLevelClient.index(indexRequest);
        }

    }

    @Test
    public void test10() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        // 设置参数
        createIndexRequest.settings(Settings.builder().put("number_of_shards", "1").put("number_of_replicas", "0").build());
        createIndexRequest.mapping("doc", "{\n" +
                "         \"properties\" : {\n" +
                "            \"charge\" : {\n" +
                "               \"type\" : \"keyword\"\n" +
                "            },\n" +
                "            \"description\" : {\n" +
                "               \"analyzer\" : \"ik_max_word\",\n" +
                "               \"search_analyzer\" : \"ik_smart\",\n" +
                "               \"type\" : \"text\"\n" +
                "            },\n" +
                "            \"end_time\" : {\n" +
                "               \"format\" : \"yyyy-MM-dd HH:mm:ss\",\n" +
                "               \"type\" : \"date\"\n" +
                "            },\n" +
                "            \"expires\" : {\n" +
                "               \"format\" : \"yyyy-MM-dd HH:mm:ss\",\n" +
                "               \"type\" : \"date\"\n" +
                "            },\n" +
                "            \"grade\" : {\n" +
                "               \"type\" : \"keyword\"\n" +
                "            },\n" +
                "            \"id\" : {\n" +
                "               \"type\" : \"keyword\"\n" +
                "            },\n" +
                "            \"mt\" : {\n" +
                "               \"type\" : \"keyword\"\n" +
                "            },\n" +
                "            \"name\" : {\n" +
                "               \"analyzer\" : \"ik_max_word\",\n" +
                "               \"search_analyzer\" : \"ik_smart\",\n" +
                "               \"type\" : \"text\"\n" +
                "            },\n" +
                "            \"pic\" : {\n" +
                "               \"index\" : false,\n" +
                "               \"type\" : \"keyword\"\n" +
                "            },\n" +
                "            \"price\" : {\n" +
                "               \"type\" : \"float\"\n" +
                "            },\n" +
                "            \"price_old\" : {\n" +
                "               \"type\" : \"float\"\n" +
                "            },\n" +
                "            \"pub_time\" : {\n" +
                "               \"format\" : \"yyyy-MM-dd HH:mm:ss\",\n" +
                "               \"type\" : \"date\"\n" +
                "            },\n" +
                "            \"qq\" : {\n" +
                "               \"index\" : false,\n" +
                "               \"type\" : \"keyword\"\n" +
                "            },\n" +
                "            \"st\" : {\n" +
                "               \"type\" : \"keyword\"\n" +
                "            },\n" +
                "            \"start_time\" : {\n" +
                "               \"format\" : \"yyyy-MM-dd HH:mm:ss\",\n" +
                "               \"type\" : \"date\"\n" +
                "            },\n" +
                "            \"status\" : {\n" +
                "               \"type\" : \"keyword\"\n" +
                "            },\n" +
                "            \"studymodel\" : {\n" +
                "               \"type\" : \"keyword\"\n" +
                "            },\n" +
                "            \"teachmode\" : {\n" +
                "               \"type\" : \"keyword\"\n" +
                "            },\n" +
                "            \"teachplan\" : {\n" +
                "               \"analyzer\" : \"ik_max_word\",\n" +
                "               \"search_analyzer\" : \"ik_smart\",\n" +
                "               \"type\" : \"text\"\n" +
                "            },\n" +
                "            \"users\" : {\n" +
                "               \"index\" : false,\n" +
                "               \"type\" : \"text\"\n" +
                "            },\n" +
                "            \"valid\" : {\n" +
                "               \"type\" : \"keyword\"\n" +
                "            }\n" +
                "         }\n" +
                "      }", XContentType.JSON);
        // 操作索引的客户端
        IndicesClient indicesClient = restHighLevelClient.indices();
        // 返回响应
        CreateIndexResponse createIndexResponse = indicesClient.create(createIndexRequest);
    }
}
