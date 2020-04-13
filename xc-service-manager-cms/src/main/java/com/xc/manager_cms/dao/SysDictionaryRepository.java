package com.xc.manager_cms.dao;

import com.xc.model.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysDictionaryRepository extends MongoRepository<SysDictionary, String> {

    SysDictionary findByDType(String dType);
}
