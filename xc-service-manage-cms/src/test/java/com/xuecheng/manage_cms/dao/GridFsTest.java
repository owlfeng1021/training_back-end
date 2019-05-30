package com.xuecheng.manage_cms.dao;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTest {
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
    @Test
    public void testStore() throws FileNotFoundException{
        File file=new File("d:/course.ftl");
        FileInputStream fileInputStream=new FileInputStream(file);
        ObjectId store = gridFsTemplate.store(fileInputStream, "course.ftl");
        System.out.println(store);//5cef6f4af09c2f300847775f
    }
    @Test
    public void queryFile() throws IOException {
        // 根据文件id查询文件
        GridFSFile id = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5ce3f8e0f09c2f462cb233d4")));
        // 打开一个下载流对象
        GridFSDownloadStream gridFSDownloadStream =  gridFSBucket.openDownloadStream(id.getObjectId());
        // 创建gridfsresource
        GridFsResource gridFsResource = new GridFsResource(id, gridFSDownloadStream);

        IOUtils.toString(gridFsResource.getInputStream(), "utf-8");


    }

}
