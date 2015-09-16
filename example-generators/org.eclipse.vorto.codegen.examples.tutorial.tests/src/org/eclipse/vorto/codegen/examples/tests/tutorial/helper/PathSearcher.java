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
package org.eclipse.vorto.codegen.examples.tests.tutorial.helper;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class PathSearcher {

	/**
	 * Retrieve all paths with given file extension under root path
	 * 
	 * @param rootPath
	 *            : Root path to start search
	 * @param fileExtention
	 *            : extension name of file
	 * @return List of Files satisfy condition
	 * @throws IOException
	 */
	public List<Path> findPathsWithExtension(final Path rootPath, final String fileExtension) throws IOException {

		final List<Path> resultPaths = new ArrayList<Path>();

		if (rootPath == null) {
			return resultPaths;
		}

		Files.walkFileTree(rootPath, new FileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes atts) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes mainAtts) throws IOException {
				if (matchesExtension(path, fileExtension)) {
					resultPaths.add(path);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path path, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path path, IOException exc) throws IOException {
				return path.equals(rootPath) ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
			}
		});
		return resultPaths;
	}

	final private boolean matchesExtension(Path path, String fileExtension) {
		if (path.toFile().isDirectory()) {
			return false;
		}

		if (fileExtension.endsWith("*")) {
			return true;
		}

		return path.toString().equals(fileExtension);
	}
}
