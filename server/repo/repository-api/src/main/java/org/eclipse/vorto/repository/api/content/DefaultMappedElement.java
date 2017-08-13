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
package org.eclipse.vorto.repository.api.content;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultMappedElement implements IMappedElement {

	protected String targetPlatformKey;
	
	protected String stereotype;
	
	protected Map<String, String> mappedAttributes = new HashMap<String, String>();
	
	@Override
	public String getStereotype() {
		return stereotype;
	}

	@Override
	public Map<String, String> getMappedAttributes() {
		return Collections.unmodifiableMap(mappedAttributes);
	}

	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}

	public void setMappedAttributes(Map<String, String> mappedAttributes) {
		this.mappedAttributes = mappedAttributes;
	}

	public String getTargetPlatformKey() {
		return targetPlatformKey;
	}

	public void setTargetPlatformKey(String targetPlatformKey) {
		this.targetPlatformKey = targetPlatformKey;
	}
	
}
