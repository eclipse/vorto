/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.codegen.ui.filewrite;

public class JavaRegionMarker implements IRegionMarker {

	private static final String PATTERN = "//@GeneratedByVorto" + "#%s";

	private static final String PATTERN_BEGIN = PATTERN + " Begin";
	private static final String PATTERN_END = PATTERN + " End";

	private String key;

	public JavaRegionMarker(String key) {
		this.key = key;
	}

	public static boolean isValidRegionMarker(String key, String content) {
		return content.contains(String.format(PATTERN_BEGIN, key));
	}

	@Override
	public String getRegionBegin() {
		return String.format(PATTERN_BEGIN, key);
	}

	@Override
	public String getRegionEnd() {
		return String.format(PATTERN_END, key);
	}

}
