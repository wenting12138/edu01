package com.xc.cmsclient.dao;

import com.xc.model.cms.CmsPage;
import com.xc.model.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {
}
