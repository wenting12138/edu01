package com.xc.filesystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.xc.filesystem.config.DfsConfig;
import com.xc.filesystem.dao.FileSystemDao;
import com.xc.filesystem.service.FileService;
import com.xc.model.filesystem.FileSystem;
import com.xc.model.filesystem.response.FileSystemCode;
import com.xc.model.filesystem.response.UploadFileResult;
import com.xc.ommon.exception.ExceptionCast;
import com.xc.ommon.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private DfsConfig dfsConfig;
    @Autowired
    private FileSystemDao fileSystemDao;

    @Override
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businessKey, String metadata) {

        // 将文件存入fastdfs
        String fileId = uploadFile(multipartFile);
        if (fileId == null) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        // 存入mongodb
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setBusinesskey(businessKey);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFileType(multipartFile.getContentType());
        if (StringUtils.isNotEmpty(metadata)) {
            Map map = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }
        fileSystemDao.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS, fileSystem);
    }

    private String uploadFile(MultipartFile multipartFile) {
        try {
            // 初始化dfs环境
            initDfs();
            TrackerClient trackerClient = new TrackerClient();
            // 连接trackerserver
            TrackerServer trackerServer = trackerClient.getConnection();
            // 得到storageServer
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            // 获取storageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
            byte[] bytes = multipartFile.getBytes();
            String originalFilename = multipartFile.getOriginalFilename();
            String extFileName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileId = storageClient1.upload_file1(bytes, extFileName, null);
            return fileId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initDfs(){
        try {
            ClientGlobal.initByTrackers(dfsConfig.trackerServers);
            ClientGlobal.setG_connect_timeout(dfsConfig.connectTimeoutInSeconds);
            ClientGlobal.setG_network_timeout(dfsConfig.networkTimeoutInSeconds);
            ClientGlobal.setG_charset(dfsConfig.charset);
        } catch (Exception e) {
            ExceptionCast.cast(FileSystemCode.FS_INITFASTDFS_ERROR);
        }

    }
}
