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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.eclipse.vorto.remoterepository.Constants;
import org.eclipse.vorto.remoterepository.model.ModelFactory;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.model.ModelView;

public class ModelQueryTestBase {

	protected void indexSampleDocuments(RAMDirectory indexDirectory)
			throws IOException {

		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_1,
				analyzer);
		IndexWriter iwriter = new IndexWriter(indexDirectory, config);

		Document freezer = createModelDocument("Freezer", "com.bosch.fridge",
				"2.1.2", ModelType.FUNCTIONBLOCK.name(),
				"Freezer functionblock");
		Document freezingFreezer = createModelDocument("FreezerFreezing",
				"com.bosch.freezing", "1.1.1", ModelType.FUNCTIONBLOCK.name(),
				"Freezing functionblock");
		Document chiller = createModelDocument("Chiller", "com.bosch.fridge",
				"2.1.2", ModelType.FUNCTIONBLOCK.name(),
				"Chiller functionblock");
		Document light = createModelDocument("Light", "com.light.common",
				"7.234.2", ModelType.FUNCTIONBLOCK.name(),
				"Light functionblock");
		Document switcher = createModelDocument("Switcher", "org.vorto.common",
				"4.1.2", ModelType.FUNCTIONBLOCK.name(),
				"Simple Switch  functionblock");
		Document lightDevice = createModelDocument("SomeIM",
				"com.mynamespace.light", "3.4.2",
				ModelType.INFORMATIONMODEL.name(), "SomeIM device");
		Document robot = createModelDocument("Robot", "com.robot.irobot",
				"1.1.22", ModelType.INFORMATIONMODEL.name(),
				"iRobot Scooba Device");
		Document lgRobot = createModelDocument("Robot", "com.robot.lg",
				"1.1.22", ModelType.INFORMATIONMODEL.name(),
				"iRobot Scooba Device");

		iwriter.addDocument(freezer);
		iwriter.addDocument(freezingFreezer);
		iwriter.addDocument(chiller);
		iwriter.addDocument(light);
		iwriter.addDocument(lightDevice);
		iwriter.addDocument(switcher);
		iwriter.addDocument(robot);
		iwriter.addDocument(lgRobot);
		iwriter.close();
	}

	protected Document createModelDocument(String modelName, String namespace,
			String version, String modelType, String description) {
		Document document = new Document();

		Store stored = Field.Store.YES;
		document.add(new StringField(Constants.INDEX_FIELD_MODEL_NAME,
				modelName, stored));
		document.add(new StringField(Constants.INDEX_FIELD_MODEL_NAMESPACE,
				namespace, stored));
		document.add(new StringField(Constants.INDEX_FIELD_MODEL_VERSION,
				version, stored));
		document.add(new StringField(Constants.INDEX_FIELD_MODEL_TYPE,
				modelType, stored));
		document.add(new StringField(Constants.INDEX_FIELD_DESCRIPTION,
				description, stored));
		return document;
	}

	protected void indexSampleModelViews(RAMDirectory indexDirectory)
			throws IOException {
		Collection<ModelView> modelViews = getTestModelViews();

		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_1,
				analyzer);
		IndexWriter iwriter = new IndexWriter(indexDirectory, config);

		for (ModelView view : modelViews) {
			Document doc = createModelDocument(view.getModelId().getName(),
					view.getModelId().getNamespace(), view.getModelId()
							.getVersion(), view.getModelId().getModelType()
							.name(), view.getDescription());
			iwriter.addDocument(doc);
		}

		iwriter.close();
	}

	protected Collection<ModelView> getTestModelViews() {
		Collection<ModelView> modelView = new ArrayList<ModelView>();

		modelView.add(ModelFactory.newFunctionBlockView("com.erle", "1.0.0",
				"light", "A light for the world"));
		modelView.add(ModelFactory.newFunctionBlockView("com.erle", "1.0.1",
				"light", "A light for the world"));
		modelView.add(ModelFactory.newFunctionBlockView("com.erle", "1.0.2",
				"light", "A light for the world"));
		modelView.add(ModelFactory.newFunctionBlockView("com.erle", "1.0.1",
				"spi", "SPI for communication"));
		modelView.add(ModelFactory.newFunctionBlockView("org.erle", "1.0.1",
				"rf", "A light for the world"));
		modelView.add(ModelFactory.newFunctionBlockView("org.czar", "1.0.1",
				"i2c", "A light for the world"));
		modelView.add(ModelFactory.newFunctionBlockView("com.czar", "1.0.1",
				"switch", "A light for the world"));
		modelView.add(ModelFactory.newFunctionBlockView("com.mantos", "1.0.1",
				"radio", "A light for the world"));
		modelView.add(ModelFactory.newFunctionBlockView("com.mantos", "1.0.1",
				"spi", "SPI shit"));

		return modelView;
	}

}
