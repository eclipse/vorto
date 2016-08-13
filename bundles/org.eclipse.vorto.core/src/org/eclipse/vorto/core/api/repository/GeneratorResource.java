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
package org.eclipse.vorto.core.api.repository;

/**
 * A lightweight view of the resources in the Repository
 */
public class GeneratorResource {
	private String key;
	private String name;
	private String description;
	private String creator;
	private String documentationUrl;
	private String image32x32;
	private String image144x144;
	private String rating;
	
	public GeneratorResource(String key, 
							String name, 
							String description, 
							String creator, 
							String documentationUrl, 
							String image32x32, 
							String image144x144, 
							String rating) {
		this.key = key;
		this.name = name;
		this.description = description;
		this.creator = creator;
		this.documentationUrl = documentationUrl;
		this.image32x32 = image32x32;
		this.image144x144 = image144x144;
		this.rating = rating;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getCreator() {
		return creator;
	}

	public String getDocumentationUrl() {
		return documentationUrl;
	}

	public String getImage32x32() {
		return image32x32;
	}

	public String getImage144x144() {
		return image144x144;
	}

	public String getRating() {
		return rating;
	}

}