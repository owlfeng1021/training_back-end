package com.xuecheng.filesystem.service;


import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileSystemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemService.class);
    @Value("${owl.fastdfs.tracker_servers}")
    String tracker_servers;
    @Value("${owl.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;
    @Value("${owl.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;
    @Value("${owl.fastdfs.charset}")
    String charset;
    @Autowired
    FileSystemRepository fileSystemRepository;

    //加载fdfs的配置
    private void initFdfsConfig() {
        try {
            // 初始化服务器ip
            ClientGlobal.initByTrackers(tracker_servers);
            // 设置连接超时时间
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            // 设置网络超时时间
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
            // 设置字符集
            ClientGlobal.setG_charset(charset);
        } catch (Exception e) {
            e.printStackTrace();
            //初始化文件系统出错
            ExceptionCast.cast(FileSystemCode.FS_CONFIG_NOTEXISTS);
        }
    }

    // 上传文件
    public UploadFileResult upload(MultipartFile multipartFile,
                                   String filetag,
                                   String businesskey,
                                   String metadata) {
        // 判断数据是否异常
        if (multipartFile == null) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        // 上传文件到fdfs 返回文件id
        String fileId = upload_fdfs(multipartFile);
        // 创建文件信息
        FileSystem fileSystem = new FileSystem();
        // 文件id
        fileSystem.setFileId(fileId);
        // 文件路径
        fileSystem.setFilePath(fileId);
        // 文件在文件系统里面的路径
        fileSystem.setBusinesskey(businesskey);
        // 标签
        fileSystem.setFiletag(filetag);
        // 元信息
        if (StringUtils.isNotEmpty(metadata)){
            Map map = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }
        // 名称
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        // 大小
        fileSystem.setFileSize(multipartFile.getSize());
        // 文件类型
        fileSystem.setFileType(multipartFile.getContentType());
        fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);
    }

    // 上传文件 返回文件id
    public String upload_fdfs(MultipartFile file) {
        initFdfsConfig();
        //创建tracker client
        TrackerClient trackerClient = new TrackerClient();
        //获取trackerServer
        try {
            TrackerServer connection = trackerClient.getConnection();
            //获取trackerServer
            TrackerServer trackerServer = trackerClient.getConnection();
            // 获取tracker 分配的storage
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            //获取storage client
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storeStorage);
            // 上传文件
            // 文件字节
            byte[] bytes = file.getBytes();
            // 文件原始名称
            String originalFilename = file.getOriginalFilename();
            // 文件扩展名
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            // 文件id
            /**
             *  参数含义
             */
            String returnFile = storageClient1.upload_file1(bytes, extName, null);
            return returnFile;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return null;
    }
}
