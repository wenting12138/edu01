package com.xc.manager_cms.dao;

import com.mongodb.Mongo;
import com.xc.model.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {

}
