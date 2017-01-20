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
package org.eclipse.vorto.server.devtool.config;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelPackageImpl;
import org.eclipse.vorto.editor.datatype.DatatypeStandaloneSetup;
import org.eclipse.vorto.editor.datatype.web.DatatypeServlet;
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup;
import org.eclipse.vorto.editor.functionblock.web.FunctionblockServlet;
import org.eclipse.vorto.editor.infomodel.InformationModelRuntimeModule;
import org.eclipse.vorto.editor.infomodel.web.InformationModelServlet;
import org.eclipse.vorto.editor.infomodel.web.InformationModelWebModule;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.util.Modules;

@Configuration
public class XtextConfiguration {

	private final List<ExecutorService> executorServices = CollectionLiterals.<ExecutorService> newArrayList();

	@Bean
	public Injector getInjectorBean() {
		final Provider<ExecutorService> _function = new Provider<ExecutorService>() {
			@Override
			public ExecutorService get() {
				ExecutorService _newCachedThreadPool = Executors.newCachedThreadPool();
				final Procedure1<ExecutorService> _function = new Procedure1<ExecutorService>() {
					@Override
					public void apply(final ExecutorService it) {
						XtextConfiguration.this.executorServices.add(it);
					}
				};
				return ObjectExtensions.<ExecutorService> operator_doubleArrow(_newCachedThreadPool, _function);
			}
		};
		final Provider<ExecutorService> executorServiceProvider = _function;

		DatatypeStandaloneSetup.doSetup();
		FunctionblockStandaloneSetup.doSetup();		
		InformationModelPackageImpl.init();
		

		return Guice.createInjector(
				Modules.override(new InformationModelRuntimeModule())
						.with(new InformationModelWebModule(executorServiceProvider)));
	}

	@Bean
	public ServletRegistrationBean datatyepXtextServlet() {
		return new ServletRegistrationBean(new DatatypeServlet(), "/datatype/xtext-service/*");
	}
	
	@Bean
	public ServletRegistrationBean functionBlockXtextServlet() {
		return new ServletRegistrationBean(new FunctionblockServlet(), "/functionblock/xtext-service/*");
	}

	@Bean
	public ServletRegistrationBean informationModelXtextServlet() {
		return new ServletRegistrationBean(new InformationModelServlet(), "/infomodel/xtext-service/*");
	}

}
