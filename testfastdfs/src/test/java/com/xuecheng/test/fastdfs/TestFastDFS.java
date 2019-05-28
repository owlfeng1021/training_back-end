package com.xuecheng.test.fastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastDFS {

    // 上传测试
    @Test
    public void testUpload() {
        // 定义一个trackerClient 用于请求trackerClient
        try {
            ClientGlobal.initByProperties("application.properties");

            // 定义trackerClient 用于请求trackerServer
            TrackerClient trackerClient = new TrackerClient();
            // 连接tracker
            TrackerServer trackerServer = trackerClient.getConnection();
            // 获取stroage
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            // 创建stroageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storeStorage);
            // 向stroage服务器上传文件
            // 本地文件的路径
            String filePath = "d:/picture.png";
            // 上传后拿到文件的id
            String fileId = storageClient1.upload_file1(filePath, "png", null);
            System.out.println(fileId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

    }

    // 下载测试
    @Test
    public void testDownload() {
        try {
            ClientGlobal.initByProperties("application.properties");

            // 定义trackerClient 用于请求trackerServer
            TrackerClient trackerClient = new TrackerClient();
            // 连接tracker
            TrackerServer trackerServer = trackerClient.getConnection();
            // 获取stroage
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            // 创建stroageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storeStorage);
            String fileId = "group1/M00/00/00/wKjpg1zrXuyAZtMoACUx7jaIW50438.png";
            byte[] bytes = storageClient1.download_file1(fileId);
            FileOutputStream fileOutputStream = new FileOutputStream(new File("d:/logo.png"));
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

    }
}