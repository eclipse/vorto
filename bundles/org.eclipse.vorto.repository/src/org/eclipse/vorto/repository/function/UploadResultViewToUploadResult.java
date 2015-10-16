package org.eclipse.vorto.repository.function;

import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.api.repository.UploadResult;
import org.eclipse.vorto.repository.model.ModelView;
import org.eclipse.vorto.repository.model.UploadResultView;

import com.google.common.base.Function;

public class UploadResultViewToUploadResult implements Function<UploadResultView, UploadResult> {

	private Function<ModelView, ModelResource> converter;
	
	public UploadResultViewToUploadResult(Function<ModelView, ModelResource> converter) {
		this.converter = converter;
	}

	@Override
	public UploadResult apply(UploadResultView input) {
		UploadResult result = new UploadResult();
		result.setHandleId(input.getHandleId());
		result.setErrorMessage(input.getErrorMessage());
		result.setValid(input.isValid());
		result.setModelResource(converter.apply(input.getModelResource()));
		result.setUnresolvedReferences(input.getUnresolvedReferences());
		return result;
	}

}
