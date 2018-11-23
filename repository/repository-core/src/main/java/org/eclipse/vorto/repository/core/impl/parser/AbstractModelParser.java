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
package org.eclipse.vorto.repository.core.impl.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup;
import org.eclipse.vorto.editor.infomodel.InformationModelStandaloneSetup;
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.impl.validation.CouldNotResolveReferenceException;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Injector;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public abstract class AbstractModelParser implements IModelParser {

	static {
		FunctionblockPackage.eINSTANCE.eClass();
		InformationModelPackage.eINSTANCE.eClass();
		MappingPackage.eINSTANCE.eClass();
		
		FunctionblockStandaloneSetup.doSetup();
		InformationModelStandaloneSetup.doSetup();
		MappingStandaloneSetup.doSetup();
	}
	
	private String fileName;
	private IModelRepository repository;
	private Collection<FileContent> dependencies = Collections.emptyList();
	
	public AbstractModelParser(String fileName, IModelRepository repository) {
		this.fileName = fileName;
		this.repository = Objects.requireNonNull(repository);
	}

	@Override
	public ModelInfo parse(InputStream is) {
		Injector injector = getInjector();
		
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		resourceSet.addLoadOption(XtextResource.OPTION_ENCODING, "UTF-8");
		
		Collection<ModelId> importedDependencies = importExternallySpecifiedDependencies(dependencies, resourceSet);
		
		Resource resource = createResource(fileName, getContent(is), resourceSet).orElseThrow(() -> 
			new ValidationException("Xtext is not able to create a resource for this model. Check if you are using the correct parser.", getModelInfoFromFilename()));
		
		if (resource.getContents().size() <= 0) {
			throw new ValidationException("Xtext is not able to create a model out of this file. Check if the file you are using is correct.", getModelInfoFromFilename());
		}
		
		Model model = (Model) resource.getContents().get(0);
		
		/* Import the rest of the dependencies (those that were not loaded above) from the repository */
		importDependenciesFromRepository(resourceSet, importedDependencies, model);
		
		/* Execute validators */
		IResourceValidator validator = injector.getInstance(IResourceValidator.class);
		List<Issue> issues = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
		if (issues.size() > 0) {
			List<ModelId> missingReferences = getMissingReferences(model, issues);
			if (missingReferences.size() > 0) {
				throw new CouldNotResolveReferenceException(getModelInfo(model).orElse(getModelInfoFromFilename()), missingReferences);
			} else {
				throw new ValidationException(collate(convertIssues(issues)), getModelInfo(model).orElse(getModelInfoFromFilename()));
			}
		}
		
		if (!resource.getErrors().isEmpty()) {
			throw new ValidationException(resource.getErrors().get(0).getMessage(), 
					getModelInfo(model).orElse(getModelInfoFromFilename()));
		}
		
		return new ModelResource((Model) resource.getContents().get(0));
	}

	private Set<ModelIssue> convertIssues(List<Issue> issues) {
		return issues.stream().map(issue -> new ModelIssue(issue.getLineNumber(), issue.getMessage())).collect(Collectors.toSet());
	}

	private List<ModelId> getMissingReferences(Model model, List<Issue> issues) {
		return issues.stream().collect(ArrayList<ModelId>::new, (acc, issue) -> {
				if (issue.getCode() != null && issue.getCode().equals("org.eclipse.xtext.diagnostics.Diagnostic.Linking")) {
					getName(issue.getMessage()).flatMap(name -> getModelId(model, name)).ifPresent(modelId -> acc.add(modelId));
				}
			}, (list1, list2) -> {
				list1.addAll(list2);
			});
	}

	private Optional<ModelId> getModelId(Model model, String name) {
		return model.getReferences().stream()
				.map(modelRef -> ModelId.fromReference(modelRef.getImportedNamespace(), modelRef.getVersion()))
				.filter(modelId -> modelId.getName().equals(name))
				.findFirst();
	}

	private Optional<String> getName(String message) {
		String[] words = message.split("\\s+");
		if (words.length <= 0) 
			return Optional.empty();
		String dirtyName = words[words.length - 1];
		return Optional.ofNullable(dirtyName.replaceAll("'", "").replaceAll("\\.", ""));
	}

	private void importDependenciesFromRepository(XtextResourceSet resourceSet,
			Collection<ModelId> alreadyImportedDependencies, Model model) {
		Collection<ModelId> allReferences = getReferences(model); 
		allReferences.removeAll(alreadyImportedDependencies);
		allReferences.forEach(refModelId -> {
			repository.getFileContent(refModelId, Optional.empty()).ifPresent(refFile -> {
				createResource(refFile.getFileName(), refFile.getContent(), resourceSet);
			});
		});
	}

	private Collection<ModelId> importExternallySpecifiedDependencies(Collection<FileContent> dependencies, XtextResourceSet resourceSet) {
		return dependencies.stream().map(fileContent -> {
			Optional<Resource> maybeDependency = createResource(fileContent.getFileName(), fileContent.getContent(), resourceSet);
			return maybeDependency.map(dependency -> {
				Model dependencyModel = (Model) dependency.getContents().get(0); 
				return new ModelId(dependencyModel.getName(), dependencyModel.getNamespace(), dependencyModel.getVersion());
			}).orElse(null);
		}).collect(Collectors.toList());
	}
	
	@Override
	public void setReferences(Collection<FileContent> fileReferences) {
		this.dependencies = Objects.requireNonNull(fileReferences);
	}

	private Collection<ModelId> getReferences(Model model) {
		return model.getReferences().stream()
				.map(modelRef -> ModelId.fromReference(modelRef.getImportedNamespace(), modelRef.getVersion()))
				.collect(Collectors.toList());
	}
	
	private Optional<ModelInfo> getModelInfo(Model model) {
		if (model == null || model.getName() == null || model.getNamespace() == null || model.getVersion() == null) {
			return Optional.empty();
		}
		
		return Optional.of(new ModelInfo(new ModelId(model.getName(), model.getNamespace(), model.getVersion()), ModelType.fromFileName(fileName)));
	}
	
	private String collate(Set<ModelIssue> issues) {
		StringBuffer error = new StringBuffer();		
		for(ModelIssue issue : issues) {
			error.append(issue);
		}
		return error.toString();
	}

	private Optional<Resource> createResource(String fileName, byte[] fileContent, XtextResourceSet resourceSet) {
		Objects.requireNonNull(fileName);
		Objects.requireNonNull(fileContent);
		Objects.requireNonNull(resourceSet);
		
		String filename = "file-" + UUID.randomUUID().toString().replace("-", "") + "-" + fileName;
		
		Resource resource = resourceSet.createResource(URI.createURI("dummy:/" + filename));
		if (resource != null) {
			try {
				resource.load(new ByteArrayInputStream(fileContent), resourceSet.getLoadOptions());
				return Optional.of(resource);
			} catch (IOException e) {
				throw new ValidationException(e.getMessage(), null);
			}
		}
 		
		return Optional.empty();
	}

	private byte[] getContent(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			IOUtils.copy(is, baos);
		} catch (IOException e1) {
			throw new ParsingException("Error while converting stream to array", e1);
		}
		
		return baos.toByteArray();
	}
	
	private ModelInfo getModelInfoFromFilename() {
		return new ModelInfo(parseModelIdFromFileName(), ModelType.fromFileName(fileName));
	}
	
	private ModelId parseModelIdFromFileName() {
		String pureFileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf("."));
		ModelId modelId = new ModelId();
		try {
			modelId.setNamespace(pureFileName.substring(0, pureFileName.lastIndexOf(".")));
			modelId.setName(pureFileName.substring(pureFileName.lastIndexOf(".") + 1, pureFileName.indexOf("_")));

			String version = pureFileName.substring(pureFileName.indexOf("_") + 1);
			version = version.replaceAll("_", ".");
			modelId.setVersion(version.substring(0, 5));
		} catch (Throwable t) {
			return new ModelId(pureFileName, "", "0.0.0");
		}
		return modelId;
	}

	protected abstract Injector getInjector();

	
	private static class ModelIssue {
		private int lineNumber;
		private String msg;
		
		public ModelIssue(int lineNumber, String msg) {
			this.lineNumber = lineNumber;
			this.msg = msg;
		}
		
		public String toString() {
			return "On line number "+lineNumber+ " : "+msg;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + lineNumber;
			result = prime * result + ((msg == null) ? 0 : msg.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ModelIssue other = (ModelIssue) obj;
			if (lineNumber != other.lineNumber)
				return false;
			if (msg == null) {
				if (other.msg != null)
					return false;
			} else if (!msg.equals(other.msg))
				return false;
			return true;
		}
		
		
	}
}
