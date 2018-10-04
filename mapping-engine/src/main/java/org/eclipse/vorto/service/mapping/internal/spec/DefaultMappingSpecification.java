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
package org.eclipse.vorto.service.mapping.internal.spec;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.Functions;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;

public class DefaultMappingSpecification implements IMappingSpecification {

	private Map<String, FunctionblockModel> fbs = new HashMap<String, FunctionblockModel>();
	
	private FunctionLibrary library = new FunctionLibrary();
	
	private Infomodel infomodel;
	
	@Override
	public Infomodel getInfoModel() {
		return this.infomodel;
	}

	@Override
	public FunctionblockModel getFunctionBlock(String fbPropertyName) {
		return fbs.get(fbPropertyName);
	}

	@Override
	public Optional<Functions> getCustomFunctions() {
		return Optional.ofNullable(this.library);
	}

	public Map<String, FunctionblockModel> getFbs() {
		return fbs;
	}

	public void setFbs(Map<String, FunctionblockModel> fbs) {
		this.fbs = fbs;
	}

	public FunctionLibrary getLibrary() {
		return library;
	}

	public void setLibrary(FunctionLibrary library) {
		this.library = library;
	}

	public Infomodel getInfomodel() {
		return infomodel;
	}

	public void setInfomodel(Infomodel infomodel) {
		this.infomodel = infomodel;
	}
}
