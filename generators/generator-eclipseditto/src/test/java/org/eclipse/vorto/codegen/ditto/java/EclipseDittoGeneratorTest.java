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
package org.eclipse.vorto.codegen.ditto.java;

import org.eclipse.vorto.codegen.ditto.EclipseDittoGenerator;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.plugin.AbstractGeneratorTest;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class EclipseDittoGeneratorTest extends AbstractGeneratorTest {

	@BeforeClass
	public static void initParser() {
		ModelWorkspaceReader.init();
	}

	/** Test case for checking whether the returned file is a Zip file */
	@Test
	public void checkResultZipFileDitto() throws Exception {
		InformationModel model = modelProvider("SuperInfomodel.infomodel","SuperSuperFb.fbmodel","");
		EclipseDittoGenerator eclipseDittoGenerator = new EclipseDittoGenerator();
		checkResultZipFile(eclipseDittoGenerator,model);
	}

}
