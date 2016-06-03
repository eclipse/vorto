/*******************************************************************************
 *  Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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

package org.eclipse.vorto.codegen.ui.tasks;

import org.eclipse.vorto.codegen.api.Generated;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;

/**
 * Generates a MANIFEST.MF file in the META-INF folder
 * 
 * 
 * 
 */
public class ManifestFileModule<ProjectMetaData> implements
		ICodeGeneratorTask<ProjectMetaData> {
	private static final String FOLDER_NAME = "META-INF";
	private static final String FILE_NAME = "MANIFEST.MF";
	private ITemplate<ProjectMetaData> template = null;

	public ManifestFileModule(ITemplate<ProjectMetaData> template) {
		this.template = template;
	}

	public void generate(ProjectMetaData metaData, InvocationContext invocationContext, IGeneratedWriter outputter) {
		Generated generatedPom = new Generated(FILE_NAME, FOLDER_NAME,
				template.getContent(metaData,invocationContext));
		outputter.write(generatedPom);
	}
}
