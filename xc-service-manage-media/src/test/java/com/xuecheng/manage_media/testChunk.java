package com.xuecheng.manage_media;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;


public class testChunk {
    @Test
    public void testChunk() {
//      源文件  D:\webStorm\learn\media_test
        File file = new File("D:/webStorm/learn/media_test/lucene.avi");
//      放置文件目录  D:\webStorm\learn\media_test\hsl
        String chunkPath = "D:/webStorm/learn/media_test/hsl/";
        File dir = new File("D:/webStorm/learn/media_test/hsl/");
        // 定义块文件大小
        long chunkSize = 1024 * 1024 * 1;
        // 定义缓冲区大小
        byte[] b = new byte[1024];
        // 分块数量 总大小/分块大小 然后取整
        long chunkNum = (long) Math.ceil(file.length() * 1.0 / chunkSize);
        // 如果分块大小小于等于0
        if (chunkNum <= 0) {
            chunkNum = 1;
        }
        try {
            RandomAccessFile random_read = new RandomAccessFile(file, "r");
            // 分块
            for (int i = 0; i < chunkNum; i++) {
                File chunk = new File(chunkPath + i);
                boolean newFile = chunk.createNewFile();
                if (newFile) {
                    // 向块文件内写入数据
                    RandomAccessFile random_write = new RandomAccessFile(chunk, "rw");
                    int len = -1;
                    while ((len = random_read.read(b)) != -1) {
                        random_write.write(b, 0, len);
                        if (chunk.length() >= chunkSize) {
                            break;
                        }
                    }
                    random_write.close();
                }

            }
            random_read.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // 合并文件
    @Test
    public void testMergeFile() throws IOException {
        //      放置文件目录  D:\webStorm\learn\media_test\hsl
        String chunkPath = "D:/webStorm/learn/media_test/hsl/";
        File chunkFile = new File(chunkPath);
        // 合并文件
        File mergeFile = new File("D:/webStorm/learn/media_test/lucene_merge.avi");
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        mergeFile.createNewFile();
        // 写文件
        RandomAccessFile random_write = new RandomAccessFile(mergeFile, "rw");
        // 指针指向顶端
        random_write.seek(0);
        // 缓冲区
        byte[] b = new byte[1024];
        // 分块区域
        File[] filelist = chunkFile.listFiles();
        ArrayList<File> fileArrayList = new ArrayList<>(Arrays.asList(filelist));

        // 排序
        Collections.sort(fileArrayList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
                    return -1;
                }
                return 1;
            }
        });
        // 合并文件
        for (File file : fileArrayList) {
            RandomAccessFile random_read = new RandomAccessFile(file, "rw");
            int len =-1;
            while ((len=random_read.read(b))!=-1){
                random_write.write(b,0,len);
            }
            random_read.close();
        }
        random_write.close();


    }
    @Test
    public void testMergeFile02() throws IOException {
        //块文件目录
        File chunkFolder = new File("D:/webStorm/learn/media_test/hsl/");
//合并文件
        File mergeFile = new File("D:/webStorm/learn/media_test/lucene.mp4");
        if(mergeFile.exists()){
            mergeFile.delete();
        }
        //创建新的合并文件
        mergeFile.createNewFile();
//用于写文件
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
//指针指向文件顶端
        raf_write.seek(0);
//缓冲区
        byte[] b = new byte[1024];
//分块列表
        File[] fileArray = chunkFolder.listFiles();
// 转成集合，便于排序
        List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));
// 从小到大排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
                    return -1;
                }
                return 1;
            }
        });
//合并文件
        for(File chunkFile:fileList){
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile,"rw");
            int len = -1;
            while((len=raf_read.read(b))!=-1){
                raf_write.write(b,0,len);
            }
            raf_read.close();
        }
        raf_write.close();
    }


}
