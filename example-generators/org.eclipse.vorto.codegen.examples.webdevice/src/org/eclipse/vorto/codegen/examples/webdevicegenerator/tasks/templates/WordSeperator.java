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
package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates;

import org.apache.commons.lang3.StringUtils;


public class WordSeperator {

	/**
	 * Seperate camel style string into words e.g. "isOn"can to separated into
	 * "Is On"
	 * 
	 * @param camelString
	 * @return
	 */
	public static String splitIntoWords(String camelString) {
		if (camelString == null || camelString.length() == 0) {
			return camelString;
		}

		StringUtils.trim(camelString);

		char[] characters = camelString.toCharArray();
		String resultWords = "";

		for (int i = 0; i < characters.length; i++) {
			if (i == 0) {
				resultWords = String.valueOf(Character
						.toUpperCase(characters[0]));
			} else {
				if (Character.isUpperCase(characters[i])) {
					resultWords += " ";
				}
				resultWords += characters[i];
			}
		}
		return resultWords;
	}
}
