/**
 * Copyright (c) 2015-2017 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.server.devtool.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.inject.Provider;

public class ExecutorServiceProviderFactory {
	
	private static Provider<ExecutorService> executorServiceProvider;
	
	public static Provider<ExecutorService> getExecutorServiceProvider(){
		if(executorServiceProvider == null){
			initExecutorServiceProvider();
			return executorServiceProvider;
		}else{
			return executorServiceProvider;
		}
	}
	
	private static void initExecutorServiceProvider(){
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		Provider<ExecutorService> executorServiceProvider = new Provider<ExecutorService>() {
			@Override
			public ExecutorService get() {
				return cachedThreadPool;
			}
		};
		ExecutorServiceProviderFactory.executorServiceProvider = executorServiceProvider;
	}		
}
