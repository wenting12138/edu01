package com.wen.test;

import com.xc.test.TestFastFdsMain;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@SpringBootTest(classes = TestFastFdsMain.class)
@RunWith(SpringRunner.class)
public class TestFastFds {

    // 上传测试
    @Test
    public void upload() {
        try {
            // 加载配置类
            ClientGlobal.initByProperties("fastdfs.properties");
            // 创建tracker的客户端.进行请求trackerServer
            TrackerClient trackerClient = new TrackerClient();
            // 连接trackerServer
            TrackerServer trackerServer = trackerClient.getConnection();
            // 获取storage
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            // 创建storageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
            String filepath = "D:/002.jpg";
            // file id
            String fileId = storageClient1.upload_file1(filepath, "jpg", null);
            System.out.println("*******************上传成功*****************");
            System.out.println("上传路径: " + "http://192.168.73.128:8080/" + fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 下载测试
    @Test
    public void download() {
        OutputStream os = null;
        try {
            // 加载配置类
            ClientGlobal.initByProperties("fastdfs.properties");
            // 创建tracker的客户端.进行请求trackerServer
            TrackerClient trackerClient = new TrackerClient();
            // 连接trackerServer
            TrackerServer trackerServer = trackerClient.getConnection();
            // 获取storage
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            // 创建storageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
            byte[] bytes = storageClient1.download_file1("group1/M00/00/00/wKhJgF6Jf1WAYgA5AABUrQdiRQk828.jpg");
            os = new FileOutputStream(new File("d:/wen.jpg"));
            os.write(bytes);
            System.out.println("*******************下载成功*****************");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 删除
    @Test
    public void delete() {
        try {
            // 加载配置类
            ClientGlobal.initByProperties("fastdfs.properties");
            // 创建tracker的客户端.进行请求trackerServer
            TrackerClient trackerClient = new TrackerClient();
            // 连接trackerServer
            TrackerServer trackerServer = trackerClient.getConnection();
            // 获取storage
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            // 创建storageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
            int i = storageClient1.delete_file1("group1/M00/00/00/wKhJgF6Jf1WAYgA5AABUrQdiRQk828.jpg");
            System.out.println(i);
            System.out.println("*******************删除成功*****************");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
