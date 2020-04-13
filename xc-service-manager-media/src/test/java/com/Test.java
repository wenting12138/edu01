package com;

import com.xc.media_manager.MediaManagerMain;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SpringBootTest(classes = MediaManagerMain.class)
@RunWith(SpringRunner.class)
public class Test {

    /**
     *  文件分块
     */
    @org.junit.Test
    public void test() throws IOException {

        File file = new File("D:\\test\\lucene.avi");
        // 块文件目录
        String chunksFilePath = "D:\\test\\chunks\\";

        // 定义块文件的大小  1m
        long chunkSize = 1 * 1024 * 1024;

        // 块数
        long chunkNums = (long) Math.ceil(file.length() * 1.0 / chunkSize);

        // 读文件的对象
        RandomAccessFile rafRead = new RandomAccessFile(file, "r");

        byte[] bytes = new byte[1024];
        for (int i = 0; i < chunkNums; i ++) {
            File chunkFile = new File(chunksFilePath + i);
            int len = -1;
            RandomAccessFile rafWrite = new RandomAccessFile(chunkFile, "rw");
            while ((len = rafRead.read(bytes)) != -1) {
                rafWrite.write(bytes, 0, len);
                // 如果块文件的大小达到了 1m 开始写下一块
                if (chunkFile.length() >= chunkSize){
                    break;
                }
            }

            rafWrite.close();
        }
        rafRead.close();
    }

    /**
     *  文件的合并
     */
    @org.junit.Test
    public void fileMerge() throws IOException {

        String baseFile = "D:\\test\\chunks\\";

        File file = new File(baseFile);
        // 块文件的列表
        File[] files = file.listFiles();

        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                    return 1;
                }
                return -1;
            }
        });

        // 合并文件
        File sourceFile = new File("D:\\test\\luceneMerge.avi");
        // 创建新文件
        boolean newFile = sourceFile.createNewFile();
        RandomAccessFile rafWrite = new RandomAccessFile(sourceFile, "rw");

        for (File chunkFile : fileList) {
            RandomAccessFile rafRead = new RandomAccessFile(chunkFile, "r");
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = rafRead.read(bytes)) != -1) {
                rafWrite.write(bytes, 0, len);
            }
            rafRead.close();
        }
        rafWrite.close();
    }

    @org.junit.Test
    public void test1(){
        System.out.println(new File("D:\\test\\lucene.avi").length());
    }

    @org.junit.Test
    public void test01(){
        String s = "1a5dadafs1ada1";
        System.out.println(s.charAt(0));
        System.out.println(s.charAt(1));
    }
}
