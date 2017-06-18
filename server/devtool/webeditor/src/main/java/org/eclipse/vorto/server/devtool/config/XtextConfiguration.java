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

import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;

import org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelPackageImpl;
import org.eclipse.vorto.devtool.projectrepository.IProjectRepositoryService;
import org.eclipse.vorto.devtool.projectrepository.ResourceAlreadyExistsError;
import org.eclipse.vorto.devtool.projectrepository.file.ProjectRepositoryServiceFS;
import org.eclipse.vorto.editor.datatype.DatatypeRuntimeModule;
import org.eclipse.vorto.editor.datatype.web.DatatypeServlet;
import org.eclipse.vorto.editor.datatype.web.DatatypeWebModule;
import org.eclipse.vorto.editor.datatype.web.DatatypeWebSetup;
import org.eclipse.vorto.editor.functionblock.web.FunctionblockServlet;
import org.eclipse.vorto.editor.functionblock.web.FunctionblockWebSetup;
import org.eclipse.vorto.editor.infomodel.web.InformationModelServlet;
import org.eclipse.vorto.editor.infomodel.web.InformationModelWebSetup;
import org.eclipse.vorto.server.devtool.service.IProjectService;
import org.eclipse.vorto.server.devtool.utils.ExecutorServiceProviderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.util.Modules;

@Configuration
@Order(7)
public class XtextConfiguration {

	@Autowired
	private IProjectService projectService;

	@Value("${reference.repository.author}")
	private String referenceRepositoryAuthor;

	@Value("${reference.repository}")
	private String referenceRepository;

	@Autowired
	private IProjectRepositoryService projectRepositoryService;
	
	@Bean
	public Injector getInjectorBean() {
		Provider<ExecutorService> executorServiceProvider = ExecutorServiceProviderFactory.getExecutorServiceProvider();
		return Guice.createInjector(
				Modules.override(new DatatypeRuntimeModule(),new AbstractModule() {
					
					@Override
					protected void configure() {
						bind(IProjectRepositoryService.class).toInstance(projectRepositoryService);
					}
				}).with(new DatatypeWebModule(executorServiceProvider)));
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

	@PostConstruct
	public void setUpReferencedResourceDirectory() {
		registerEditorEMFModels();
		initEditorWebModules();
		try {
			projectService.createProject(referenceRepository, referenceRepositoryAuthor);
		} catch (ResourceAlreadyExistsError resourceAlreadyExistsError) {

		}
	}
	
	private void registerEditorEMFModels() {
		InformationModelPackageImpl.init();
	}

	private void initEditorWebModules() {
		Provider<ExecutorService> executorServiceProvider = ExecutorServiceProviderFactory.getExecutorServiceProvider();
		new InformationModelWebSetup(executorServiceProvider,projectRepositoryService).createInjectorAndDoEMFRegistration();
		new FunctionblockWebSetup(executorServiceProvider,projectRepositoryService).createInjectorAndDoEMFRegistration();
		new DatatypeWebSetup(executorServiceProvider,projectRepositoryService).createInjectorAndDoEMFRegistration();
	}

}
