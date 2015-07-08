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
package org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.tests;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.codegen.api.filewrite.IFileWritingStrategy;
import org.eclipse.vorto.codegen.api.tasks.Generated;
import org.eclipse.vorto.codegen.api.tasks.IOutputter;
import org.eclipse.vorto.codegen.api.tasks.eclipse.PomFileModule;
import org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.modules.fbmodel.CXFCodeGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.modules.fbmodel.POMFbTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

public class GeneratorAdapter<Context> implements IGenerator {

	@Override
	public void doGenerate(Resource input, IFileSystemAccess fsa) {
		FileSystemAccessOutputter outputter = new FileSystemAccessOutputter(fsa);
		FunctionblockModel model = (FunctionblockModel) input.getContents()
				.get(0);
		new CXFCodeGeneratorTask().generate(model, outputter);
		new PomFileModule<FunctionblockModel>(new POMFbTemplate()).generate(
				model, outputter);
	}

	private static class FileSystemAccessOutputter implements IOutputter {

		private IFileSystemAccess fsa = null;

		public FileSystemAccessOutputter(IFileSystemAccess fsa) {
			this.fsa = fsa;
		}

		@Override
		public void output(Generated generated) {
			fsa.generateFile(generated.getFileName(), generated.getContent());
		}

		@Override
		public void setFileWritingStrategy(IFileWritingStrategy strategy) {
			// TODO Auto-generated method stub

		}

	}
}
