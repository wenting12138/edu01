package com;

import com.xc.filesystem.FileSystemMain;
import com.xc.filesystem.config.DfsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = FileSystemMain.class)
@RunWith(SpringRunner.class)
public class T {

    @Autowired
    private DfsConfig config;

    @Test
    public void test(){
        System.out.println(config.trackerServers);
    }

}
