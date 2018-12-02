package org.eclipse.vorto.repository.web.core.exceptions;

import org.eclipse.vorto.model.ModelId;

public class NotAuthorizedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ModelId secureResource = null;
	
	public NotAuthorizedException(ModelId modelId, Throwable t) {
		super(t);
		this.secureResource = modelId;
	}
	
	public ModelId getModelId() {
		return this.secureResource;
	}

}
