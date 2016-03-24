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
package org.eclipse.vorto.service.generator.web.utils;

import java.io.ByteArrayInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup;
import org.eclipse.vorto.editor.infomodel.InformationModelStandaloneSetup;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;
import org.springframework.util.StringUtils;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelZipFileExtractor extends AbstractZipFileExtractor {
	
	public ModelZipFileExtractor(byte[] zipFile) {
		super(zipFile);
	}
	
	public InformationModel extract(final ModelId modelId) {
		FunctionblockPackage.eINSTANCE.eClass();
		InformationModelPackage.eINSTANCE.eClass();

		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(this.zipFile));
		ZipEntry entry = null;

		FunctionblockStandaloneSetup.doSetup();
		Injector injector = new InformationModelStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);

		Resource infoModelResource = null;
		try {
			while ((entry = zis.getNextEntry()) != null) {
				if (StringUtils.stripFilenameExtension(entry.getName()).equals(modelId.getName())) {
					infoModelResource = resourceSet.createResource(URI.createURI("fake:/" + entry.getName()));
					infoModelResource.load(new ByteArrayInputStream(copyStream(zis, entry)),
							resourceSet.getLoadOptions());
				} else {
					Resource resource = resourceSet.createResource(URI.createURI("fake:/" + entry.getName()));
					resource.load(new ByteArrayInputStream(copyStream(zis, entry)), resourceSet.getLoadOptions());

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		EcoreUtil2.resolveAll(resourceSet);	
		return (InformationModel)infoModelResource.getContents().get(0);
	}
	
	
}
