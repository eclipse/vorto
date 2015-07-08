/*******************************************************************************
/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/

package org.eclipse.vorto.remoterepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NativeFSLockFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

public class VortoTestApplication {
	private static final Logger log = Logger
			.getLogger(VortoTestApplication.class.getName());

	@Bean(name = "analyzer")
	public Analyzer getAnalyzer() {
		return new StandardAnalyzer();
	}

	@Bean(name = "fsDirectory")
	@DependsOn("analyzer")
	public FSDirectory getFSDirectory() {
		FSDirectory fsd = null;

		Path indexLocation = Paths.get("src/test/resources/temp/lucent");

		File location = indexLocation.toFile();

		if (!location.exists() || !location.canRead()) {
			log.info("Creating index directory: '" + location.getAbsolutePath()
					+ "'");
			location.mkdirs();
		}

		try {
			fsd = FSDirectory.open(location, new NativeFSLockFactory());
		} catch (IOException e) {
			log.log(Level.SEVERE, " IOException for creating Indexing folder",
					e);
		}

		return fsd;
	}

	@Bean(name = "indexWriter")
	@DependsOn("fsDirectory")
	public IndexWriter getIndexWriter() {
		return null;
	}

	@Bean(name = "indexSearcher")
	@DependsOn("indexWriter")
	public IndexSearcher getIndexSearcher() throws IOException {
		return null;
		// return new IndexSearcher(DirectoryReader.open(getFSDirectory()));
	}
}
