package com.xc.course_manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.xc.course_manager.config.CoursePublishConfigParams;
import com.xc.course_manager.constant.CourseBaseConstant;
import com.xc.course_manager.dao.*;
import com.xc.course_manager.feignclient.CmsPageClient;
import com.xc.course_manager.service.CourseService;
import com.xc.model.cms.CmsPage;
import com.xc.model.cms.response.CmsCode;
import com.xc.model.cms.response.CmsPageResult;
import com.xc.model.cms.response.CmsPostPageResult;
import com.xc.model.course.*;
import com.xc.model.course.ext.CategoryNode;
import com.xc.model.course.ext.CourseInfo;
import com.xc.model.course.ext.CourseView;
import com.xc.model.course.ext.TeachplanNode;
import com.xc.model.course.request.CourseListRequest;
import com.xc.model.course.response.CourseCode;
import com.xc.model.course.response.CoursePublishResult;
import com.xc.ommon.exception.ExceptionCast;
import com.xc.ommon.model.response.CommonCode;
import com.xc.ommon.model.response.QueryResponseResult;
import com.xc.ommon.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CourseServiceImpl implements CourseService {

    @Resource
    private TeachplanDao teachplanDao;

    @Override
    public TeachplanNode findTeachplanList(String courseId) {
        return teachplanDao.findTeachplanList(courseId);
    }

    @Override
    @Transactional
    public ResponseResult insert(Teachplan teachplan) {
        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID);
        }
        String courseId = teachplan.getCourseid();
        String parentId = teachplan.getParentid();
        // 如果父id 等于null, 表明是新添加的
        if (StringUtils.isEmpty(parentId)) {
            // 取出课程的根节点
            parentId = getTeachplanRoot(courseId);
        }
        if (StringUtils.isEmpty(parentId)) {
            ExceptionCast.cast(CommonCode.INVALID);
        }
        Teachplan parentTeachPlan = teachplanDao.findById(parentId);

        String parentGrade = parentTeachPlan.getGrade();
        Teachplan teachplanNew = new Teachplan();
        BeanUtils.copyProperties(teachplan, teachplanNew);
        teachplanNew.setCourseid(courseId);
        teachplanNew.setParentid(parentId);

        if (parentGrade.equals("1")) {
            teachplanNew.setGrade("2");
        }else {
            teachplanNew.setGrade("3");
        }
        teachplanNew.setId(UUID.randomUUID().toString().replace("-", ""));
        teachplanDao.insert(teachplanNew);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public CategoryNode findCategoryList(String id) {
        return categoryDao.findList(id);
    }

    @Resource
    private CourseBaseDao courseBaseDao;

    @Override
    public QueryResponseResult<CourseInfo> findCourse(String companyId, int page, int size, CourseListRequest courseListRequest) {
        if (page <= 0) {
            page = 1;
        }
        if (size < 0 || size > 100) {
            size = 10;
        }
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        courseListRequest.setCompanyId(companyId);
        int total = courseBaseDao.findCount();
        page = (page - 1) * size;
        List<CourseInfo> baseList = courseBaseDao.findLimitPage(page, size, courseListRequest);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", baseList);
        return new QueryResponseResult(CommonCode.SUCCESS, map);
    }

    @Override
    @Transactional
    public ResponseResult insertCourse(CourseBase courseBase) {
        if (courseBase == null) {
            ExceptionCast.cast(CommonCode.INVALID);
        }

        String id = UUID.randomUUID().toString().replace("-", "");
        courseBase.setId(id);
        courseBase.setStatus(CourseBaseConstant.courseNoPub);
        courseBaseDao.insert(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public CourseBase findById(String id) {
        return courseBaseDao.findById(id);
    }

    @Override
    @Transactional
    public ResponseResult update(String id, CourseBase courseBase) {
        CourseBase tem = findById(id);
        if (tem == null) {
            ExceptionCast.cast(CommonCode.INVALID);
        }
        courseBaseDao.update(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Autowired
    private CourseMarketDao courseMarketDao;
    @Override
    public CourseMarket findCourseMarketById(String id) {
        return courseMarketDao.findById(id);
    }

    @Override
    @Transactional
    public ResponseResult updateCourseMarket(String id, CourseMarket courseMarket) {

        CourseMarket tem = courseMarketDao.findById(id);
        if (StringUtils.isEmpty(id) || tem == null) {
            // 新增
            courseMarketDao.insert(courseMarket);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        // 更新
        courseMarket.setPrice_old(tem.getPrice_old());
        courseMarket.setId(id);
        courseMarketDao.update(courseMarket);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Autowired
    private CoursePicDao coursePicDao;

    @Override
    @Transactional
    public ResponseResult addCoursePic(String courseId, String pic) {
        CoursePic coursePic = coursePicDao.findById(courseId);
        if (coursePic == null) {
            coursePic = new CoursePic();
            coursePic.setCourseid(courseId);
            coursePic.setPic(pic);
            coursePicDao.insert(coursePic);
        }else {
            coursePic.setPic(pic);
            coursePicDao.update(coursePic);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public CoursePic findCoursePic(String courseId) {
        return coursePicDao.findById(courseId);
    }

    @Override
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        int result = coursePicDao.delete(courseId);
        if (result > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    @Override
    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();
        // 查询课程基本信息
        CourseBase baseDaoById = courseBaseDao.findById(id);
        if (baseDaoById != null) {
            courseView.setCourseBase(baseDaoById);
        }
        // 查询课程图片
        CoursePic picDaoById = coursePicDao.findById(id);
        if (picDaoById != null) {
            courseView.setCoursePic(picDaoById);
        }
        // 查询营销信息
        CourseMarket marketDaoById = courseMarketDao.findById(id);
        if (marketDaoById != null) {
            courseView.setCourseMarket(marketDaoById);
        }
        // 查询课程列表
        TeachplanNode teachplanNode = teachplanDao.findTeachplanList(id);
        if (teachplanNode != null) {
            courseView.setTeachplanNode(teachplanNode);
        }
        return courseView;
    }
    @Autowired
    private CmsPageClient cmsPageClient;
    @Autowired
    private CoursePublishConfigParams coursePublishConfigParams;

    @Override
    public CoursePublishResult preview(String id) {
        CourseBase courseBase = courseBaseDao.findById(id);
        if (courseBase == null) {
            ExceptionCast.cast(CmsCode.CMS_COURSE_ISNULL);
        }
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(coursePublishConfigParams.getPublish_siteId());
        cmsPage.setDataUrl(coursePublishConfigParams.getPublish_dataUrl() + id);
        cmsPage.setTemplateId(coursePublishConfigParams.getPublish_templateId());
        cmsPage.setPageName(id + ".html");
        cmsPage.setPageAliase(courseBase.getName());
        // 物理路径
        cmsPage.setPagePhysicalPath(coursePublishConfigParams.getPublish_pagePhysicalPath());
        // 页面访问路径
        cmsPage.setPageWebPath(coursePublishConfigParams.getPublish_pageWebPath());
        // 请求cms添加页面
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        // 拼接页面预览的url
        CmsPage tem = cmsPageResult.getCmsPage();
        String pageId = tem.getPageId();
        // 返回CoursePublishResult
        String previewUrl = coursePublishConfigParams.getPublish_previewUrl() + pageId;
        return new CoursePublishResult(CommonCode.SUCCESS, previewUrl);
    }

    @Override
    @Transactional
    public CoursePublishResult publish(String id) {
        CourseBase courseBase = courseBaseDao.findById(id);
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(coursePublishConfigParams.getPublish_siteId());
        cmsPage.setDataUrl(coursePublishConfigParams.getPublish_dataUrl() + id);
        cmsPage.setTemplateId(coursePublishConfigParams.getPublish_templateId());
        cmsPage.setPageName(id + ".html");
        cmsPage.setPageAliase(courseBase.getName());
        // 物理路径
        cmsPage.setPagePhysicalPath(coursePublishConfigParams.getPublish_pagePhysicalPath());
        // 页面访问路径
        cmsPage.setPageWebPath(coursePublishConfigParams.getPublish_pageWebPath());
        // 调用cms接口
        CmsPostPageResult pageResult = cmsPageClient.postPageQuick(cmsPage);
        if (!pageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        // 更新状态为已发布
        CourseBase coursetem = updatePubState(id);
        // 保存课程索引信息

        // 创建coursePub对象
        CoursePub coursePub = createCoursePub(id);
        // 保存到数据库
        saveCoursePub(id, coursePub);

        // 向teachplanmediapub保存
        saveTeachplanMediaPub(id);

        return new CoursePublishResult(CommonCode.SUCCESS, pageResult.getPageUrl());
    }

    @Autowired
    private TeachplanMediaPubDao teachplanMediaPubDao;

    private void saveTeachplanMediaPub(String courseId) {
        // 删除teachplanmediapub的数据
        teachplanMediaPubDao.deleteByCourseId(courseId);
        // 从teachplanmedia查
        List<TeachplanMedia> teachplanMedia = teachplanMediaDao.findByCourseId(courseId);
        List<TeachplanMediaPub> teachplanMediaPubs = new ArrayList<>();
        for (TeachplanMedia media : teachplanMedia) {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(media, teachplanMediaPub);
            teachplanMediaPub.setTimestamp(new Date());
            teachplanMediaPubs.add(teachplanMediaPub);
        }
        teachplanMediaPubDao.saveAll(teachplanMediaPubs);
    }

    @Autowired
    private TeachplanMediaDao teachplanMediaDao;

    @Override
    @Transactional
    public ResponseResult saveMedia(TeachplanMedia teachplanMedia) {

        // 保存课程计划和媒资的关联
        if (teachplanMedia == null || StringUtils.isEmpty(teachplanMedia.getTeachplanId())) {
            ExceptionCast.cast(CommonCode.INVALID);
        }

        // 校验课程计划是否是3级
        String teachplanId = teachplanMedia.getTeachplanId();
        // 查询到课程计划
        Teachplan teachplan = teachplanDao.findById(teachplanId);
        if (teachplan == null) {
            ExceptionCast.cast(CommonCode.INVALID);
        }
        String grade = teachplan.getGrade();
        if (grade == null || !grade.equals("3")) {
            ExceptionCast.cast(CourseCode.COURSE_MEDIS_TEACHPLAN_GRADEERROR);
        }
        TeachplanMedia media = teachplanMediaDao.findById(teachplanId);
        if (media == null) {
            // 新增
            teachplanMediaDao.save(teachplanMedia);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        // 更新
        media.setCourseId(teachplanMedia.getCourseId());
        media.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());
        media.setTeachplanId(teachplanId);
        media.setMediaId(teachplanMedia.getMediaId());
        media.setMediaUrl(teachplanMedia.getMediaUrl());
        teachplanMediaDao.update(media);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Autowired
    private CoursePubDao coursePubDao;

    private CoursePub saveCoursePub(String id, CoursePub coursePub) {
        CoursePub pub = coursePubDao.findById(id);
        if (pub == null) {
            coursePub.setId(id);
            // 给logstach使用
            coursePub.setTimestamp(new Date());
            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String s = format.format(new Date());
            // 发布时间
            coursePub.setPubTime(s);
            coursePubDao.save(coursePub);
        }else {
            BeanUtils.copyProperties(coursePub, pub);
            pub.setId(id);
            coursePubDao.update(coursePub);
        }
        return pub;
    }

    // 创建coursePub对象
    private CoursePub createCoursePub(String id) {
        CoursePub coursePub = new CoursePub();
        CourseBase courseBase = courseBaseDao.findById(id);
        if (courseBase != null) {
            // 拷贝基本信息
            BeanUtils.copyProperties(courseBase, coursePub);
        }
        CoursePic coursePic = coursePicDao.findById(id);
        if (coursePic != null) {
            // 拷贝图片信息
            coursePub.setPic(coursePic.getPic());
        }
        CourseMarket courseMarket = courseMarketDao.findById(id);
        if (courseMarket != null) {
            // 拷贝营销信息
            coursePub.setCharge(courseMarket.getCharge());
            coursePub.setValid(courseMarket.getValid());
            coursePub.setQq(courseMarket.getQq());
            coursePub.setPrice(courseMarket.getPrice());
            coursePub.setPrice_old(courseMarket.getPrice_old());
        }
        // 课程计划信息
        TeachplanNode teachplanNode = teachplanDao.findTeachplanList(id);
        String s = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(s);

        return coursePub;
    }

    private CourseBase updatePubState(String id) {
        CourseBase courseBase = this.findById(id);
        if (courseBase == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        courseBase.setStatus(CourseBaseConstant.courseISPub);
        courseBaseDao.update(courseBase);
        return courseBase;
    }

    private String getTeachplanRoot(String courseId) {
        CourseBase courseBase = courseBaseDao.findById(courseId);
        if (courseBase == null) {
            return null;
        }
        String pname = courseBase.getName();
        List<Teachplan> list = teachplanDao.findTeachListByCourseIdAndParentId(courseId, "0");
        Teachplan teachplan1 = list.get(0);
        if (list == null || list.size() < 1) {
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setPname(pname);
            teachplan.setCourseid(courseId);
            teachplan.setStatus("0");
            teachplan.setId(UUID.randomUUID().toString().replace("-", ""));
            teachplanDao.insert(teachplan);
            teachplan1.setId(teachplan.getId());
        }
        return teachplan1.getId();
    }
}
