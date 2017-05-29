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
