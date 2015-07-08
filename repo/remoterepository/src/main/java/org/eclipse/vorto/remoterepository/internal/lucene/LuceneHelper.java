package org.eclipse.vorto.remoterepository.internal.lucene;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NativeFSLockFactory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * The task of this class is to help any lucene class with miscellaneous task
 * that is Lucene-specific (initialization, cleaning up of directories, creating
 * writers, searchers and index directories), so the other classes can
 * concentrate on their actual task.
 * 
 */
@Service
public class LuceneHelper {

	private static final Logger logger = Logger.getLogger(LuceneHelper.class
			.getName());

	@Value("${vorto.index.dir}")
	private String indexLocation;

	private Directory indexDirectory;

	private DirectoryReader indexDirectoryReader;

	private IndexWriter indexWriter;

	private IndexSearcher indexSearcher;

	/**
	 * Returns the Lucene index writer
	 * 
	 * @return IndexWriter
	 */
	public IndexWriter getIndexWriter() {
		if (indexWriter == null) {
			indexWriter = getIndexWriter(getIndexDirectory(indexLocation));
		}

		return indexWriter;
	}

	/**
	 * Returns the Lucene index reader
	 * 
	 * There are three possibilities here -- 1. No indexDirectoryReader -- in
	 * which case we create a new DirectoryReader and new IndexSearcher 2. There
	 * is an indexDirectoryReader, but no changes in the index -- in which case
	 * we just return the old instance of indexSearcher 3. There is an
	 * indexDirectoryReader and changes in the index -- in which case we refresh
	 * the indexSearcher with the new DirectoryReader
	 * 
	 * @return IndexSearcher
	 */
	public IndexSearcher getIndexSearcher() {
		return getIndexSearcher(getIndexDirectory(indexLocation));
	}

	public void cleanupIndexDirectory() throws IOException {
		File location = new File(indexLocation);

		if (location.exists()) {
			logger.info("Deleting index directory : "
					+ location.getAbsolutePath());
			FileUtils.deleteDirectory(location);
		}
	}

	private Directory getIndexDirectory(String indexLocation) {
		if (indexDirectory == null) {
			File location = new File(indexLocation);

			if (!location.exists() || !location.canRead()) {
				logger.info("Creating index directory: '"
						+ location.getAbsolutePath() + "'");
				location.mkdirs();
			}

			try {
				indexDirectory = FSDirectory.open(location,
						new NativeFSLockFactory());
			} catch (IOException e) {
				throw new RuntimeException(
						"IOException while opening indexing folder", e);
			}
		}

		return indexDirectory;
	}

	private IndexWriter getIndexWriter(Directory indexDirectory) {
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_1,
				new StandardAnalyzer());
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		// Optional: for better indexing performance, if you are indexing many
		// documents, increase the RAM
		// buffer. But if you do this, increase the max heap size to the JVM (eg
		// add -Xmx512m or -Xmx1g):
		// iwc.setRAMBufferSizeMB(256.0);

		// NOTE: if you want to maximize search performance, you can optionally
		// call forceMerge here. This can be
		// a terribly costly operation, so generally it's only worth it when
		// your index is relatively static (ie
		// you're done adding documents to it):
		// writer.forceMerge(1);

		try {
			return new IndexWriter(indexDirectory, iwc);
		} catch (IOException e) {
			throw new RuntimeException(
					"Error while create Lucene Index Writer", e);
		}
	}

	private IndexSearcher getIndexSearcher(Directory indexDirectory) {
		try {
			if (indexDirectoryReader != null) {
				DirectoryReader newDirectoryReader = DirectoryReader
						.openIfChanged(indexDirectoryReader);
				if (newDirectoryReader != null) {
					indexDirectoryReader = newDirectoryReader;
					indexSearcher = new IndexSearcher(indexDirectoryReader);
				}
			} else {
				indexDirectoryReader = DirectoryReader.open(indexDirectory);
				indexSearcher = new IndexSearcher(indexDirectoryReader);
			}

			return indexSearcher;
		} catch (IOException e) {
			throw new RuntimeException(
					"Error creating a directory reader. Probably the index isn't initialized yet.",
					e);
		}
	}
}
