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
package org.eclipse.vorto.codegen.internal.filewrite;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.vorto.codegen.api.filewrite.FileWriteContext;

public class OverWritingStrategy extends AbstractFileWriteStrategy {

	@Override
	public void writeFile(FileWriteContext generated, IFile existingFile)
			throws CoreException, IOException {
		if (existingFile.exists())
			existingFile.delete(true, null);

		InputStream content = IOUtils.toInputStream(generated.getContent());

		existingFile.create(content, false, null);
		content.close();
	}

}
