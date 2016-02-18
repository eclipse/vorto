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
package org.eclipse.vorto.service.generator;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.io.FileUtils;
import org.eclipse.vorto.codegen.api.Generated;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.IMappingContext;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class CopyResourcesTask<Context> implements ICodeGeneratorTask<Context> {

	private String basePath;
	private String targetPath;

	public CopyResourcesTask(String basePath, String targetPath) {
		this.basePath = basePath;
		this.targetPath = targetPath;
	}

	@Override
	public void generate(Context model, IMappingContext mappingContext, final IGeneratedWriter writer) {
		
		try {
			Path start = Paths.get(new ClassPathResource(basePath).getFile().getPath());
			Files.walkFileTree(start, new FileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					writer.write(new Generated(file.getFileName().toFile().getName(), getOutputPath(file), FileUtils.readFileToByteArray((file.toFile()))));
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}
	}

	private String getOutputPath(Path file) {
		String parentPath = file.getParent().toString().replace("\\", "/");
		String outputPath = parentPath.substring(parentPath.lastIndexOf(this.basePath)+this.basePath.length());
		if (outputPath.startsWith("/")) {
			outputPath = outputPath.substring(1);
		}
		return outputPath.isEmpty()? this.targetPath: this.targetPath+"/"+outputPath;
	}
}
