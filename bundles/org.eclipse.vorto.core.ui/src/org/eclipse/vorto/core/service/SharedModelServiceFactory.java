package org.eclipse.vorto.core.service;

import org.eclipse.vorto.core.internal.service.DefaultSharedModelService;

public class SharedModelServiceFactory {
	private static final ISharedModelService SERVICE = new DefaultSharedModelService();

	public static ISharedModelService getDefault() {
		return SERVICE;
	}
}
