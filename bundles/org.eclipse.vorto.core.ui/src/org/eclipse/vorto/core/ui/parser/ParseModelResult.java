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
