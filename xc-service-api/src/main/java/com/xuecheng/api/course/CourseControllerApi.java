package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *  Created by Administrator
 */
@Api(value = "课程管理接口",description = "课程管理接口，提供页面的增、删、改、查")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanNodeList(String courseId);
    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) ;
}
