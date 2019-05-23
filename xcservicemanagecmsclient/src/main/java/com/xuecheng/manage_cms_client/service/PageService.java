package com.xuecheng.manage_cms_client.service;


import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service
public class PageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteRepository cmsSiteRepository;
    @Autowired
    GridFSBucket gridFSBucket;
    @Autowired
    GridFsTemplate gridFsTemplate;

    /**
     * 是从pageid里面找 siteid然后使用siteid找 真实的保存路径 然后pageid的路径进行保存就达到了找到这个文件的目的了
     * @param pageId
     */
    // 保存html页面到服务器的物理路径
    public void savePageToServerPath(String pageId) {
        // 得到html的文件id 从cmsPage中获取 htmlFileIdn内容
        CmsPage cmsPageById = findCmsPageById(pageId);
        //
        String htmlFileId = cmsPageById.getHtmlFileId();

        // 从gridFs中查询html文件
        InputStream inputStream = getFileById(htmlFileId);
        if (inputStream == null) {
            LOGGER.error("getFileById InputStream is null ,htmlFileId{}", htmlFileId);
            return;
        }
        String siteId = cmsPageById.getSiteId();
        // 根据站点id查询
        CmsSite cmsSiteById = findCmsSiteById(siteId);
        String sitePhysicalPath = cmsSiteById.getSitePhysicalPath();
        // 将html文件 保存到服务器的访问路径
        String pagePath = sitePhysicalPath + cmsPageById.getPagePhysicalPath() + cmsPageById.getPageName();
        // 使用apache 通用工具包
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(new File(pagePath));

            IOUtils.copy(inputStream, fileOutputStream);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    // 根据文件id从gridFS中查询文件内容
    public InputStream getFileById(String fileId) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        // 打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        // 定义gridfsresource
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }

    // 根据页面id查询
    public CmsPage findCmsPageById(String pageId) {
        Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    // 根据站点id查询站点
    public CmsSite findCmsSiteById(String siteId) {
        Optional<CmsSite> byId = cmsSiteRepository.findById(siteId);
        if (byId != null) {
            return byId.get();
        }
        return null;

    }


}


