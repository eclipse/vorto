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
package org.eclipse.vorto.codegen.tests.wizard;

import static org.junit.Assert.assertEquals;

import org.eclipse.vorto.codegen.api.tasks.eclipse.LocationWrapper;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class LocationWrapperTest {

	private static final String PATH = "testPath";
	private static final String NAME = "testProjectName";
	LocationWrapper wrapper = null;
	URI uri = null;

	@Before
	public void init() throws URISyntaxException {
		wrapper = new LocationWrapper(PATH, NAME);
		uri = new URI(PATH + "/" + NAME);
	}

	@Test
	public void testUri() {
		assertEquals(wrapper.getUri(), uri);
	}
}
