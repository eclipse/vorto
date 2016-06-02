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
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;

/**
 * Code Generator which creates specific folder (structure)
 * 
 */
public class FolderModule<Context> implements ICodeGeneratorTask<Context> {

	private String[] folders;

	public FolderModule(String... folders) {
		this.folders = folders;
	}

	@Override
	public void generate(Context ctx, InvocationContext invocationContext, IGeneratedWriter outputter) {
		if (folders != null) {
			for (String customFolder : folders) {
				outputter.write(new Generated(null, customFolder, new byte[0]));
			}
		}
	}

}
