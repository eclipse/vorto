/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.repository.model;

import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * 
 * @author Alexander Edelmann
 *
 */
public class ModelId {
	private String name;
	private String namespace;
	private String version;
	
	private static final String PATH_DELIMITER = "/";

	public ModelId(String name, String namespace, String version) {
		super();
		this.name = name;
		this.namespace = namespace.toLowerCase();
		this.version = version;
	}
	
	public static ModelId fromReference(String qualifiedName, String version) {
		String name = qualifiedName.substring(qualifiedName.lastIndexOf(".")+1);
		String namespace = qualifiedName.substring(0,qualifiedName.lastIndexOf("."));
		return new ModelId(name,namespace,version);
	}
	
	/**
	 * Example path: /org/eclipse/vorto/color/1.0.0/
	 * @param path
	 * @return
	 */
	public static ModelId fromPath(String path) {
		String[] pathFragments = path.substring(1).split("/");
		return new ModelId(pathFragments[pathFragments.length-2],convertToNamespace(pathFragments),pathFragments[pathFragments.length-1]);
	}
		
	private static String convertToNamespace(String[] fragments) {
		StringBuilder namespaceBuilder = new StringBuilder();
		for (int i = 0; i < fragments.length-2;i++) {
			namespaceBuilder.append(fragments[i]);
			if (i < fragments.length-3) {
				namespaceBuilder.append(".");
			}
		}
		return namespaceBuilder.toString();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getFullPath() {
		StringBuilder path = new StringBuilder(getNamespacePath());
		path.append(PATH_DELIMITER);
		path.append(name);
		path.append(PATH_DELIMITER);
		path.append(version);
		return path.toString();
	}

	public String getNamespacePath() {
		StringBuilder path = new StringBuilder(PATH_DELIMITER);
		StringTokenizer tokenizer = new StringTokenizer(namespace, ".");
		while(tokenizer.hasMoreTokens()) {
			path.append(tokenizer.nextToken());
			if (tokenizer.hasMoreTokens()) {
				path.append(PATH_DELIMITER);
			}
		}
		return path.toString();
	}
	
	public Iterator<String> iterator() {
		return new StringTokenizerIterator(getFullPath());
	}
	
	private static class StringTokenizerIterator implements Iterator<String> {

		private StringTokenizer enumeration;

		public StringTokenizerIterator(String fullPath) {
			this.enumeration = new StringTokenizer(fullPath.substring(1),"/");
		}

		public boolean hasNext() {
			return this.enumeration.hasMoreElements();
		}

		public String next() {
			return this.enumeration.nextToken();
		}

		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public String toString() {
		return "ModelId [name=" + name + ", namespace=" + namespace + ", version=" + version + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelId other = (ModelId) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	public String getPrettyFormat() {	
		return namespace + "." + name + ":" +version;
	}
	
	
	
}
