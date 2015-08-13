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
package org.eclipse.vorto.remoterepository.internal.dao;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.vorto.remoterepository.dao.IModelDAO;
import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.model.ModelFactory;
import org.eclipse.vorto.remoterepository.model.ModelId;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * The implementation of DAO that stores the models in a 3 layer directory
 * repository. The XMI files are stored in
 * {@code repositoryBaseDirectory/<namespace>/<name>/<version>/<filename> }
 * where {@code filename} is {@code <modelname>.<modeltype> } and where
 * {@code modeltype} is either {@literal .fbmodel} for a function block,
 * {@literal .infomodel } for an information model and {@literal .datatype} for
 * datatypes.
 * 
 * 
 */
@Service
public class FilesystemModelDAO implements IModelDAO {

	private static final String SUFFIX_DATATYPE = ".type_xmi";
	private static final String SUFFIX_INFOMODEL = ".infomodel_xmi";
	private static final String SUFFIX_FBMODEL = ".fbmodel_xmi";

	private Log log = LogFactory.getLog(FilesystemModelDAO.class);

	@Value("${vorto.repo.dir}")
	private String repositoryBaseDirectory;

	public String getRepositoryBaseDirectory() {
		return repositoryBaseDirectory;
	}

	public void setRepositoryBaseDirectory(String repositoryBaseDirectory) {
		this.repositoryBaseDirectory = repositoryBaseDirectory;
	}

	@PostConstruct
	public void initRepoBaseDir() throws IOException {
		Path baseDir = Paths.get(getRepositoryBaseDirectory());

		if (!Files.exists(baseDir)) {
			try {
				Files.createDirectories(baseDir);
			} catch (IOException e) {
				log.error("INIT Repo Base DIR " + getRepositoryBaseDirectory(),
						e);
				throw e;
			}
		}
	}

	@Override
	public ModelContent getModelById(ModelId id) {

		Objects.requireNonNull(id);
		Objects.requireNonNull(id.getModelType(),
				"Model type shouldn't be Null.");
		Objects.requireNonNull(id.getName(), "Model name shouldn't be Null.");
		Objects.requireNonNull(id.getNamespace(),
				"Namespace shouldn't be Null.");
		Objects.requireNonNull(id.getVersion(), "Version shouldn't be Null.");

		ModelContent mc = new ModelContent(id, id.getModelType(), null);

		Path pathToModel = resolvePathToModel(id);

		log.debug("pathToModel: " + pathToModel.toAbsolutePath().toString());
		if (pathToModel.toFile().exists()) {
			mc = this.getModelContent(pathToModel);
		}

		return mc;
	}

	private Path resolvePathToModel(ModelId id) {
		StringBuffer filename = new StringBuffer(id.getName());
		String fileExtension = getModelExtension(id.getModelType());
		filename.append(fileExtension);

		return resolveDirectoryPathToModel(id).resolve(filename.toString());
	}

	private Path resolveDirectoryPathToModel(ModelId id) {
		return Paths.get(getRepositoryBaseDirectory())
				.resolve(id.getNamespace()).resolve(id.getName())
				.resolve(id.getVersion());
	}

	@Override
	public Collection<ModelContent> getAllModels(ModelType modelType) {
		final Collection<ModelContent> allModels = new ArrayList<ModelContent>();
		final String fileExtension = getModelExtension(modelType);
		walkDirectoryTree(new ModelFoundHandler() {
			public void handle(Path file) {
				if (!file.toString().endsWith(fileExtension))
					return;
				allModels.add(getModelContent(file));
			}
		});

		return allModels;
	}

	private ModelContent getModelContent(Path file) {
		ModelType modelType = getModelType(file.toString());
		try {
			return new ModelContent(modelType, Files.readAllBytes(file));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private interface ModelFoundHandler {
		void handle(Path file);
	}

	private void walkDirectoryTree(final ModelFoundHandler handler) {
		try {
			Files.walkFileTree(Paths.get(getRepositoryBaseDirectory()),
					new SimpleFileVisitor<Path>() {
						public FileVisitResult visitFile(Path file,
								BasicFileAttributes attrs) throws IOException {
							handler.handle(file);
							return FileVisitResult.CONTINUE;
						}
					});
		} catch (IOException e) {
			throw new RuntimeException("An I/O error was thrown", e);
		}
	}

	@Override
	public ModelView saveModel(ModelContent modelContent) {

		ModelId mID = modelContent.getModelId();
		ModelView mv = ModelFactory
				.newModelView(mID, "Newly Added Model, ....");
		Path dirToSave = this.resolveDirectoryPathToModel(mID);
		Path fileToSave = this.resolvePathToModel(mID);

		try {
			if (!Files.exists(dirToSave)) {
				Files.createDirectories(dirToSave);
			}

			if (!Files.exists(fileToSave)) {
				Files.createFile(fileToSave);
			} else {
				Files.deleteIfExists(fileToSave);
			}

			Files.write(fileToSave, modelContent.getContent(),
					StandardOpenOption.TRUNCATE_EXISTING);
			return mv;
		} catch (IOException e) {
			throw new RuntimeException(
					"An I/O error was thrown while saving new model file: "
							+ mID.toString(), e);
		}
	}

	@Override
	public boolean exists(ModelId mID) {

		Path dirToSave = this.resolveDirectoryPathToModel(mID);
		Path fileToSave = this.resolvePathToModel(mID);

		if (Files.exists(dirToSave)) {
			if (Files.exists(fileToSave)) {
				return true;
			}
		}
		return false;
	}

	private String getModelExtension(ModelType modelType) {
		if (ModelType.INFORMATIONMODEL == modelType) {
			return SUFFIX_INFOMODEL;
		} else if (ModelType.FUNCTIONBLOCK == modelType) {
			return SUFFIX_FBMODEL;
		} else if (ModelType.DATATYPE == modelType) {
			return SUFFIX_DATATYPE;
		}
		return ".xmi";
	}

	private ModelType getModelType(String filePath) {
		if (filePath.endsWith(SUFFIX_INFOMODEL)) {
			return ModelType.INFORMATIONMODEL;
		} else if (filePath.endsWith(SUFFIX_FBMODEL)) {
			return ModelType.FUNCTIONBLOCK;
		} else if (filePath.endsWith(SUFFIX_DATATYPE)) {
			return ModelType.DATATYPE;
		}
		return null;
	}
}
