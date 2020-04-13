package com.xc.media_manager.serivce.impl;

import com.alibaba.fastjson.JSON;
import com.xc.media_manager.config.RabbitMqConfig;
import com.xc.media_manager.constant.MediaStatusParams;
import com.xc.media_manager.dao.MediaFileDao;
import com.xc.media_manager.serivce.MediaUploadService;
import com.xc.model.media.MediaFile;
import com.xc.model.media.response.CheckChunkResult;
import com.xc.model.media.response.MediaCode;
import com.xc.ommon.exception.ExceptionCast;
import com.xc.ommon.model.response.CommonCode;
import com.xc.ommon.model.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
@Slf4j
public class MediaUploadServiceImpl implements MediaUploadService {

    // 文件基础路径
    @Value("${xc-service-manager-media.upload-location}")
    private String fileUploadPath;

    @Autowired
    private MediaFileDao mediaFileDao;

    // 得到文件所属路径
    private String getFileFolderPath(String fileMd5) {
        return fileUploadPath + fileMd5.charAt(0) + "/"+ fileMd5.charAt(1) + "/"+ fileMd5 + "/";
    }
    // 文件路径
    private String getFilePath(String fileMd5, String fileExt) {
        return getFileFolderPath(fileMd5) + fileMd5 + "." + fileExt;
    }
    // 得到块文件所属路径
    private String getChunkFileFolderPath(String fileMd5) {
        return fileUploadPath + fileMd5.charAt(0) + "/"+ fileMd5.charAt(1) + "/"+ fileMd5 + "/" + "chunk/";
    }
    /**
     * 根据文件的md5得到文件路径
     * 一级目录: md5的第一个字符
     * 二级目录: md5的第二个字符
     * 三级目录: md5码
     * 文件名: md5 + 文件扩展名
     */

    @Override
    public ResponseResult register(String fileMd5, String fileName, long fileSize, String mimetype, String fileExt) {
        // 文件所属目录的路径
        String fileFolderPath = getFileFolderPath(fileMd5);
        // 文件路径
        String filePath = this.getFilePath(fileMd5, fileExt);
        // 检查文件在磁盘是否存在
        File file = new File(filePath);
        boolean exists = file.exists();
        // 在mongodb是否存在
        Optional<MediaFile> fileOptional = mediaFileDao.findById(fileMd5);
        if (exists && fileOptional.isPresent()) {
            // 文件存在
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        // 做一些准备工作, 检查文件所在目录是否存在, 如果不存在就创建
        File file1 = new File(fileFolderPath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     *
     * @param fileMd5
     * @param chunk: 块的下标
     * @param chunkSize: 块的大小
     * @return
     */
    @Override
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        // 检查分块文件是否存在
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File file = new File(chunkFileFolderPath + chunk);
        if (file.exists()) {
            // 存在
            return new CheckChunkResult(CommonCode.SUCCESS, true);
        }else {
            // 不存在
            return new CheckChunkResult(CommonCode.SUCCESS, false);
        }
    }

    @Override
    public ResponseResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
        // 检查分块目录是否存在
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        File file1 = new File(chunkFileFolderPath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        // 得到上传文件的输入流
        InputStream is = null;
        OutputStream os = null;
        try {
            is = file.getInputStream();
            os = new FileOutputStream(chunkFileFolderPath + chunk);
            // 写入
            IOUtils.copy(is, os);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                assert is != null;
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert os != null;
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public ResponseResult mergechunks(String fileMd5, String fileName, long fileSize, String mimetype, String fileExt) {
        // 合并文件
        // 1. 得到块文件目录的路径
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        // 2. 创建file
        File file = new File(chunkFileFolderPath);
        // 3. 分块文件列表
        File[] files = file.listFiles();
        // 4. 创建合并文件
        String filePath = this.getFilePath(fileMd5, fileExt);
        File mergeFile = new File(filePath);
        // 5. 合并文件
        mergeFile = this.mergerFile1(files, mergeFile);
        if (mergeFile == null) {
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }
        // 校验文件的MD5值
        boolean b = check(mergeFile, fileMd5);
        if (!b) {
            // 校验文件失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }
        // 保存入数据库
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileName(fileMd5 + "." + fileExt);
        mediaFile.setFileOriginalName(fileName);
        // 文件的相对路径
        String path = fileMd5.charAt(0) + "/"+ fileMd5.charAt(1) + "/"+ fileMd5 + "/";
        mediaFile.setFilePath(path);
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        // 状态为上传成功
        mediaFile.setFileStatus(MediaStatusParams.FILE_UPLOAD_SUCCESS);
        mediaFileDao.save(mediaFile);
        sendProcessVideoMsg(mediaFile.getFileId());
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${xc-service-manager-media.mq.routingkey-media-video}")
    public String routingKey;
    // 上传成功后发送消息通知mediaProcessor 进行视频的编码
    public ResponseResult sendProcessVideoMsg(String mediaId) {
        Map<String, String> map = new HashMap<>();
        map.put("mediaId", mediaId);
        String msg = JSON.toJSONString(map);
        Optional<MediaFile> optional = mediaFileDao.findById(mediaId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID);
        }
        log.info("*********[发送消息:]*******, mediaId: {}", mediaId);
        try {
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, routingKey, msg);
        }catch (Exception e) {
            log.error("[发送消息失败], exchange: {}, rountingKey: {}, mediaId: {}", RabbitMqConfig.EXCHANGE, routingKey, mediaId);
            e.printStackTrace();
            return new ResponseResult(CommonCode.FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    // 校验文件
    private boolean check(File mergeFile, String fileMd5) {
        // 得到文件的md5
        try {
            FileInputStream fis = new FileInputStream(mergeFile);
            String md5Hex = DigestUtils.md5Hex(fis);

            if (fileMd5.equalsIgnoreCase(md5Hex)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private File mergerFile1(File[] chunkFiles, File mergeFile){
        try {
            // 如果合并的文件存在, 就删除
            if (mergeFile.exists()) {
                mergeFile.delete();
            }else {
                // 否则创建
                mergeFile.createNewFile();
            }
            List<File> fileList = Arrays.asList(chunkFiles);
            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                        return 1;
                    }
                    return -1;
                }
            });
            RandomAccessFile rafWrite = new RandomAccessFile(mergeFile, "rw");
            byte[] bytes = new byte[1024];
            for (File file : fileList) {
                RandomAccessFile rafRead = new RandomAccessFile(file, "r");
                int len = -1;
                while ((len = rafRead.read(bytes)) != -1) {
                    rafWrite.write(bytes, 0, len);
                }
                rafRead.close();
            }
            rafWrite.close();
            return mergeFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
