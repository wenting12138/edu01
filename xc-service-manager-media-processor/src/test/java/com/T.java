package com;

import com.xc.media_processor.MediaProcessorMain;
import com.xc.utils.Mp4VideoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@SpringBootTest(classes = MediaProcessorMain.class)
@RunWith(SpringRunner.class)
public class T {

    @Test
    public void test() throws IOException {

        // 使用Processor Build 调用第三方应用程序
        ProcessBuilder processBuilder = new ProcessBuilder();
        // 设置第三方应用程序的命令
        processBuilder.command("ping", new String("www.baidu.com".getBytes(), "gbk"));

        // 将标准输入流和 错误流合并
        processBuilder.redirectErrorStream(true);

        // 通过标准的输入流, 拿到正常的和错误 信息
        Process start = processBuilder.start();
        InputStream is = start.getInputStream();

        // 转成字符流
        InputStreamReader isr = new InputStreamReader(is, "gbk");

        int len = -1;
        char[] chars = new char[1024];
        while ((len = isr.read(chars)) != -1) {
            String s = new String(chars, 0, len);
            System.out.println(s);
        }
        isr.close();
        is.close();
    }


    @Test
    public void test1(){
        String ffmpeg = "E:/develop/ffmpeg/ffmpeg-20180227-fa0c9d6-win64-static/bin/ffmpeg.exe";
//        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg, );

    }
}
