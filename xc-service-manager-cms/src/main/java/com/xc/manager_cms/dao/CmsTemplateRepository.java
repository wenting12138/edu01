package com.xc.manager_cms.dao;

import com.xc.model.cms.CmsConfig;
import com.xc.model.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsTemplateRepository extends MongoRepository<CmsTemplate, String> {
}
