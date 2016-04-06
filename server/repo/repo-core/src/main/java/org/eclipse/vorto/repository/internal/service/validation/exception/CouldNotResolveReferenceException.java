package org.eclipse.vorto.repository.internal.service.validation.exception;

import java.util.List;
import java.util.Objects;

import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.validation.ValidationException;

public class CouldNotResolveReferenceException extends ValidationException {
	private static final long serialVersionUID = -6078848052990402848L;
	private List<ModelId> missingReferences;
	
	public CouldNotResolveReferenceException(ModelResource resource, List<ModelId> missingReferences) {
		super(createErrorMessage(resource, missingReferences), resource);
		this.missingReferences = Objects.requireNonNull(missingReferences);
		if (missingReferences.size() <= 0) {
			throw new IllegalArgumentException("Trying to create a CouldNotResolveReferenceException with empty missingReferences."); 
		}
	}

	public List<ModelId> getMissingReferences() {
		return missingReferences;
	}
	
	private static String createErrorMessage(ModelResource resource,
			List<ModelId> missingReferences) {
		if (missingReferences.size() > 1) {
			return "Cannot resolve multiple references.";
		} else {
			return String.format("Cannot resolve reference %s", missingReferences.get(0).getPrettyFormat());
		}
	}
}
