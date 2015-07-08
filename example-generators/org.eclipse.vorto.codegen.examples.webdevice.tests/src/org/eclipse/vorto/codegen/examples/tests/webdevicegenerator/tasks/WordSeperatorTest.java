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
package org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.tasks;

import static org.junit.Assert.assertTrue;

import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.WordSeperator;
import org.junit.Test;

public class WordSeperatorTest {

	@Test
	public void testSeperateNull(){
		String words = WordSeperator.splitIntoWords(null);
		assertTrue(words==null);	
	}
	
	@Test
	public void testSeperateUpper(){
		String words = WordSeperator.splitIntoWords("isOn");
		assertTrue(words.equals("Is On"));
	}
}
