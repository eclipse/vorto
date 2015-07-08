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
package org.eclipse.vorto.remoterepository.internal.lucene;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.eclipse.vorto.remoterepository.Constants;
import org.eclipse.vorto.remoterepository.dao.IModelDAO;
import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.model.ModelId;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.eclipse.vorto.remoterepository.service.converter.IModelConverterService;
import org.eclipse.vorto.remoterepository.service.indexing.IIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * The task of this class is to 1. Index a model 2. Reindex all models in
 * repository
 * 
 *
 */
@Service
public class LuceneIndexService implements IIndexService {

	private static final Logger log = LoggerFactory
			.getLogger(LuceneIndexService.class);

	@Autowired
	private IModelDAO modelDao;

	@Autowired
	private IModelConverterService modelConverterService;

	@Autowired
	private LuceneHelper luceneHelper;

	@Value("${vorto.rebuild.index}")
	private String rebuildOnBoot;

	/**
	 * TODO : Should this be here? This is not the task of the indexing service
	 */
	@PostConstruct
	public void init() {
		if (Boolean.parseBoolean(rebuildOnBoot)) {
			try {

				luceneHelper.cleanupIndexDirectory();
				for (ModelType modelType : ModelType.values()) {
					indexModelType(modelType);
				}
			} catch (IOException e) {
				log.error(
						"IOException from cleaning index directory & re-index ",
						e);
			}
		}
	}

	/**
	 * Index a single model
	 */
	@Override
	public void indexModel(ModelView modelView) {
		log.debug("Create index for model : "
				+ modelView.getModelId().toString());
		IndexWriter indexWriter = luceneHelper.getIndexWriter();
		try {
			if (indexWriter != null) {
				log.info(" Indexing Model :"
						+ modelView.getModelId().toString());
				Document doc = generateIndexDocument(modelView);

				indexWriter.addDocument(doc);
				indexWriter.commit();

			} else {
				log.error("Error while getting indexWriter. Index might not have been created yet.");
			}
		} catch (IOException e) {
			log.error(" IOException duing indexing ModelView : "
					+ modelView.getModelId().toString(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Index all models in the repository
	 */
	@Override
	public void indexAll() {
		try {
			luceneHelper.getIndexWriter().deleteAll();
			for (ModelType modelType : ModelType.values()) {
				indexModelType(modelType);
			}
		} catch (IOException e) {
			log.error("IO Exception while doing re-index", e);
		}

	}

	private Document generateIndexDocument(ModelView modelView) {

		Document document = new Document();
		ModelId mId = modelView.getModelId();

		Field modelnameField = new StringField(
				Constants.INDEX_FIELD_MODEL_NAME, mId.getName(),
				Field.Store.YES);

		Field modelnamespaceField = new StringField(
				Constants.INDEX_FIELD_MODEL_NAMESPACE, mId.getNamespace(),// infoModel.getName(),
				Field.Store.YES);

		Field modelversionField = new StringField(
				Constants.INDEX_FIELD_MODEL_VERSION, mId.getVersion(),
				Field.Store.YES);

		Field modeltypeField = new StringField(
				Constants.INDEX_FIELD_MODEL_TYPE,
				mId.getModelType().toString(), Field.Store.YES);

		Field modeldescField = new StringField(
				Constants.INDEX_FIELD_DESCRIPTION, modelView.getDescription(),
				Field.Store.YES);

		document.add(modelnameField);
		document.add(modelnamespaceField);
		document.add(modelversionField);
		document.add(modeltypeField);
		document.add(modeldescField);

		return document;
	}

	private void indexModelType(ModelType modelType) throws IOException {
		Collection<ModelContent> allContents = modelDao.getAllModels(modelType);

		for (ModelContent modelContent : allContents) {
			ModelView modelView = modelConverterService.convert(modelContent);
			log.info("Indexing model :" + modelView.getModelId().toString());
			indexModel(modelView);
		}
	}
}