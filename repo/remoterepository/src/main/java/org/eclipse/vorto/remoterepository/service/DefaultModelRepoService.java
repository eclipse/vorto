/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.remoterepository.service;

import org.eclipse.vorto.remoterepository.builder.IModelQueryBuilder;
import org.eclipse.vorto.remoterepository.dao.IModelDAO;
import org.eclipse.vorto.remoterepository.model.ModelAuthor;
import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.model.ModelId;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.eclipse.vorto.remoterepository.service.governance.IGovernanceService;
import org.eclipse.vorto.remoterepository.service.indexing.IIndexService;
import org.eclipse.vorto.remoterepository.service.search.IModelQuery;
import org.eclipse.vorto.remoterepository.service.search.IModelQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultModelRepoService implements IModelRepoService {

	@Autowired
	private IModelDAO modelDao;
	
	@Autowired
	private IIndexService indexService;	
	
	@Autowired
	private IModelQueryBuilder modelQueryBuilder;
	
	@Autowired
	private IModelQueryFactory modelQueryFactory;
	
	@Autowired
	private IGovernanceService gService;
	
	@Override
	public IModelQuery newQuery() {
		return modelQueryFactory.newModelQuery();
	}
	
	@Override
	public IModelQuery newQueryFromExpression(String expression) {
		return modelQueryBuilder.buildFromExpression(newQuery(), expression);
	}
	
	@Override
	public ModelContent getModelContent(ModelId modelId) {
		return modelDao.getModelById(modelId);
	}

	@Override
	public ModelView saveModel(ModelContent modelContent) {	
		gService.start(modelContent, new ModelAuthor());
		ModelView mv = modelDao.saveModel(modelContent);		
		indexService.indexModel(mv);
		return mv;
	}
}
