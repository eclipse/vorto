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
package org.eclipse.vorto.server.commons.reader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

public class ModelWorkspaceReader {
	
	private WorkspaceFileReader fileReader;
	
	private List<WorkspaceZipReader> zipReaders = new ArrayList<ModelWorkspaceReader.WorkspaceZipReader>();
	
	public ModelWorkspaceReader() {
		this.fileReader = new WorkspaceFileReader();
	}


	public ModelWorkspaceReader addFile(InputStream input, ModelType type) {
		fileReader.addFile(input, type);
		return this;
	}
	
	public ModelWorkspaceReader addZip(ZipInputStream zis) {
		zipReaders.add(new WorkspaceZipReader(zis));
		return this;
	}


	public IModelWorkspace read() {
		DefaultModelWorkspace workspace = new DefaultModelWorkspace();
		workspace.addModels(fileReader.read());
		zipReaders.stream().forEach(x -> workspace.addModels(x.read()));
		return workspace;
	}
	
	private static class WorkspaceZipReader {
		private ZipInputStream zis;
		
		public WorkspaceZipReader(ZipInputStream zis) {
			this.zis = zis;
			FunctionblockPackage.eINSTANCE.eClass();
			InformationModelPackage.eINSTANCE.eClass();
			MappingPackage.eINSTANCE.eClass();
			
			FunctionblockStandaloneSetup.doSetup();
			InformationModelStandaloneSetup.doSetup();
		}
		
		public List<Model> read() {
			ZipEntry entry = null;

			Injector injector = new MappingStandaloneSetup().createInjectorAndDoEMFRegistration();
			XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
			resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);

			List<Resource> infoModelResources = new ArrayList<>();
			try {
				while ((entry = zis.getNextEntry()) != null) {
					Resource resource = resourceSet.createResource(URI.createURI("fake:/" + entry.getName()));
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(zis, baos);
					resource.load(new ByteArrayInputStream(baos.toByteArray()), resourceSet.getLoadOptions());
					infoModelResources.add(resource);
				}
			} catch (Exception ex) {
				throw new RuntimeException("Problem reading zip file",ex);
			}

			EcoreUtil2.resolveAll(resourceSet);	
			return infoModelResources.stream()
	                .map(r -> (Model)r.getContents().get(0))
	                .collect(Collectors.toList());
		}
	}
	
	private static class WorkspaceFileReader {
		private ByteArrayOutputStream baos;
		private ZipOutputStream zaos;

		private int counter = 0;
		
		public WorkspaceFileReader() {
			baos = new ByteArrayOutputStream();
			zaos = new ZipOutputStream(baos);
		}
		
		public void addFile(InputStream input, ModelType type) {
			try {
				ZipEntry zipEntry = new ZipEntry(getNextFileName(type));
				zaos.putNextEntry(zipEntry);
				byte[] entrycontent = IOUtils.toByteArray(input);
				zaos.write(entrycontent);
				zaos.closeEntry();
			} catch (Exception ex) {
				throw new IllegalArgumentException("Could not add model", ex);
			}
		}
		
		private String getNextFileName(ModelType type) {
			return "model" + counter+++type.getExtension();
		}
		
		public List<Model> read() {
			if (baos.size() > 0) {
				WorkspaceZipReader reader =  new WorkspaceZipReader(new ZipInputStream(new ByteArrayInputStream(baos.toByteArray())));
				return reader.read();
			}
			return Collections.emptyList();
		}
	}

}
