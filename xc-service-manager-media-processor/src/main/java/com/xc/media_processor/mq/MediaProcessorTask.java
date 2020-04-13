package com.xc.media_processor.mq;

import com.alibaba.fastjson.JSON;
import com.xc.media_processor.dao.MediaFileDao;
import com.xc.model.media.MediaFile;
import com.xc.model.media.MediaFileProcess_m3u8;
import com.xc.utils.HlsVideoUtil;
import com.xc.utils.Mp4VideoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class MediaProcessorTask {

    @Autowired
    private MediaFileDao mediaFileDao;
    @Value("${xc-service-manager-media.ffmpeg-path}")
    private String ffmpegPath;
    @Value("${xc-service-manager-media.video-location}")
    private String videoRootPath;

    @RabbitListener(queues = "${xc-service-manager-media.mq.queue-media-video-processor}")
    public void receiveMediaProcessorTask(String msg) {


        // 解析消息 获取mediaId
        Map map = JSON.parseObject(msg, Map.class);
        String mediaId = (String) map.get("mediaId");
        log.info("*********[接收消息:]*******, mediaId: {}",mediaId);
        Optional<MediaFile> optional = mediaFileDao.findById(mediaId);
        if (!optional.isPresent()) {
            return;
        }
        MediaFile mediaFile = optional.get();
        if (!mediaFile.getFileType().equals("avi")) {
            // 更新状态为无需处理
            mediaFile.setProcessStatus("303004");
            mediaFileDao.save(mediaFile);
            return;
        }else {
            // 需要处理
            mediaFile.setProcessStatus("303001"); // 处理中
            mediaFileDao.save(mediaFile);
        }
        // 调用工具类 转化为mp4
        String videoPath = videoRootPath + mediaFile.getFilePath() + mediaFile.getFileName();
        String mp4Name = mediaFile.getFileId() + ".mp4";
        String mp4Path = videoRootPath + mediaFile.getFilePath();
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpegPath, videoPath, mp4Name, mp4Path);
        String result = mp4VideoUtil.generateMp4();
        if (result == null || !result.equals("success")) {
            // 处理失败
            mediaFile.setProcessStatus("303003");
            MediaFileProcess_m3u8 m3u8 = new MediaFileProcess_m3u8();
            m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(m3u8);
            mediaFileDao.save(mediaFile);
            return;
        }

        // 生成m3u8
        String mp4_video_path = videoRootPath + mediaFile.getFilePath() + mp4Name;
        String m3u8_video_name = mediaFile.getFileId() + ".m3u8";
        String m3u8_video_path = videoRootPath + mediaFile.getFilePath() + "hls/";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpegPath,mp4_video_path, m3u8_video_name, m3u8_video_path);
        String res = hlsVideoUtil.generateM3u8();
        if (res == null || !res.equals("success")) {
            // 处理失败
            mediaFile.setProcessStatus("303003");
            MediaFileProcess_m3u8 m3u8 = new MediaFileProcess_m3u8();
            m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(m3u8);
            mediaFileDao.save(mediaFile);
            return;
        }

        // 处理成功
        mediaFile.setProcessStatus("303002");
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        MediaFileProcess_m3u8 m3u8 = new MediaFileProcess_m3u8();
        m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(m3u8);
        // 保存fileUrl  视频播放的相对路径
        String fileUrl = mediaFile.getFilePath() + "hls/" + m3u8_video_name;
        mediaFile.setFileUrl(fileUrl);
        mediaFileDao.save(mediaFile);

    }

}
