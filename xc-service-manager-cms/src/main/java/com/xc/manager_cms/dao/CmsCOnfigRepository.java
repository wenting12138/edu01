package com.xc.manager_cms.dao;

import com.xc.model.cms.CmsConfig;
import com.xc.model.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsCOnfigRepository extends MongoRepository<CmsConfig, String> {



}
