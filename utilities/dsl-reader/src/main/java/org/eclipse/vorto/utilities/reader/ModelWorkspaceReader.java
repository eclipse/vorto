/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.utilities.reader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
import org.eclipse.vorto.model.ModelType;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import com.google.inject.Injector;

public class ModelWorkspaceReader {

  private WorkspaceFileReader fileReader;

  private List<WorkspaceZipReader> zipReaders =
      new ArrayList<ModelWorkspaceReader.WorkspaceZipReader>();

  public ModelWorkspaceReader() {
    this.fileReader = new WorkspaceFileReader();
  }

  public static void init() {
    FunctionblockPackage.eINSTANCE.eClass();
    InformationModelPackage.eINSTANCE.eClass();
    MappingPackage.eINSTANCE.eClass();

    FunctionblockStandaloneSetup.doSetup();
    InformationModelStandaloneSetup.doSetup();
    MappingStandaloneSetup.doSetup();
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
    }

    public List<Model> read() {
      ZipEntry entry = null;

      Injector injector = new MappingStandaloneSetup().createInjectorAndDoEMFRegistration();
      XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
      resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
      resourceSet.addLoadOption(XtextResource.OPTION_ENCODING, "UTF-8");

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
        throw new RuntimeException("Problem reading zip file", ex);
      }

      EcoreUtil2.resolveAll(resourceSet);
      return infoModelResources.stream().map(r -> (Model) r.getContents().get(0))
          .collect(Collectors.toList());
    }
  }

  private static class WorkspaceFileReader {

    private List<ModelFile> files = new ArrayList<>();

    public void addFile(InputStream input, ModelType type) {
      files.add(new ModelFile(input, type));
    }

    public List<Model> read() {
      Injector injector = new MappingStandaloneSetup().createInjectorAndDoEMFRegistration();
      XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
      resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
      resourceSet.addLoadOption(XtextResource.OPTION_ENCODING, "UTF-8");

      List<Resource> infoModelResources = new ArrayList<>();

      try {
        for (ModelFile modelFile : files) {
          Resource resource = resourceSet.createResource(URI.createURI(
              "fake:/" + UUID.randomUUID().toString() + modelFile.getType().getExtension()));
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          IOUtils.copy(modelFile.getIs(), baos);
          resource.load(new ByteArrayInputStream(baos.toByteArray()), resourceSet.getLoadOptions());
          infoModelResources.add(resource);
        }
      } catch (IOException ex) {
        throw new RuntimeException("Problem reading zip file", ex);
      }

      EcoreUtil2.resolveAll(resourceSet);
      return infoModelResources.stream().map(r -> (Model) r.getContents().get(0))
          .collect(Collectors.toList());
    }
  }

  private static class ModelFile {
    private InputStream is;
    private ModelType type;

    public ModelFile(InputStream is, ModelType type) {
      super();
      this.is = is;
      this.type = type;
    }

    public InputStream getIs() {
      return is;
    }

    public ModelType getType() {
      return type;
    }
  }

}
