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
package org.eclipse.vorto.core.ui.parser;

import java.util.Collection;

import org.eclipse.emf.ecore.resource.Resource.Diagnostic;

public class ParseModelResult<M> {
	private Collection<Diagnostic> errors;
	private M model;

	private ParseModelResult(Collection<Diagnostic> errors, M m) {
		this.errors = errors;
		this.model = m;
	}
	
	public static <M> ParseModelResult<M> newResult(Collection<Diagnostic> errors, M m) {
		return new ParseModelResult<M>(errors, m);
	}

	public Collection<Diagnostic> getErrors() {
		return errors;
	}

	public void setErrors(Collection<Diagnostic> errors) {
		this.errors = errors;
	}

	public M getModel() {
		return model;
	}

	public void setModel(M model) {
		this.model = model;
	}

}
