package org.eclipse.vorto.core.service;

import org.eclipse.vorto.core.internal.service.DefaultModelTransformerService;

public class ModelTransformerServiceFactory {
	
	private static final IModelTransformerService SERVICE = new DefaultModelTransformerService();
	
	public static IModelTransformerService getDefault() {
		return SERVICE;
	}
}
