package com.xc.media_processor.dao;

import com.xc.model.media.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MediaFileDao extends MongoRepository<MediaFile, String> {

}
