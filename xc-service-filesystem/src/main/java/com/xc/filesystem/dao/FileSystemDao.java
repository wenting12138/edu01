package com.xc.filesystem.dao;

import com.xc.model.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileSystemDao extends MongoRepository<FileSystem, String> {
}
