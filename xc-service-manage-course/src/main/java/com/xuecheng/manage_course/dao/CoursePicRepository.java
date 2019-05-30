package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator.
 */
public interface CoursePicRepository extends JpaRepository<CoursePic,String> {
    //删除成功返回1否则返回0
    @Modifying
    @Transactional
    long deleteByCourseid(String courseid);
}
