/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.codegen.ui.filewrite;

/**
 * Factory for getting a file writer strategy for a code generator
 * 
 */
public final class FileWritingStrategyFactory {

	private static FileWritingStrategyFactory instance = null;

	private FileWritingStrategyFactory() {

	}

	public static FileWritingStrategyFactory getInstance() {
		if (instance == null) {
			instance = new FileWritingStrategyFactory();
		}
		return instance;
	}

	/**
	 * @return strategy which overwrites a previously generated file.
	 */
	public IFileWritingStrategy getOverwriteStrategy() {
		return new OverWritingStrategy();
	}

	/**
	 * @return strategy which creates a *.gen file which the latest content. The
	 *         user must then do a manual merge.
	 */
	public IFileWritingStrategy getGenFileStrategy() {
		return new WriteGenOverWriteStrategy();
	}

	/**
	 * @return strategy which merges delta content with existing content by
	 *         appending it in a specific placeholder region
	 */
	public IFileWritingStrategy getPlaceHolderMergeAppendingStrategy() {
		return new RegionMergeStrategy(new FileRegionAppender());
	}

	/**
	 * @return strategy which merges delta content with existing content by
	 *         replacing it in a specific placeholder region
	 */
	public IFileWritingStrategy getPlaceHolderMergeReplacementStrategy() {
		return new RegionMergeStrategy(new FileRegionReplacer());
	}
}
