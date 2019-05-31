package com.xuecheng.api.course;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator
 */
@Api(value = "课程管理接口", description = "课程管理接口，提供页面的增、删、改、查")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanNodeList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("添加课程图片")
    public ResponseResult addCoursePic(String courseId, String pic);

    @ApiOperation("获取课程基础信息")
    public CoursePic findCoursePic(String courseId);

    @ApiOperation("删除课程图片")
    public ResponseResult deleteCoursePic(String courseId);

    @ApiOperation("获取课程的展示")
    public CourseView getCourseView(String Id);

    @ApiOperation("预览课程")
    public CoursePublishResult preview(String id);

    @ApiOperation("课程发布")
    public CoursePublishResult publish(String id);

}
