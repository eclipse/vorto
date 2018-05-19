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
package org.eclipse.vorto.codegen.gateway.model;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.gateway.exception.GeneratorCreationException;
import org.eclipse.vorto.codegen.gateway.templates.DefaultGeneratorConfigUI;
import org.eclipse.vorto.codegen.gateway.utils.GatewayUtils;
import org.eclipse.vorto.server.commons.ui.IGeneratorConfigUITemplate;

public class Generator {
	private GeneratorServiceInfo info;
	private IVortoCodeGenerator instance;
	private IGeneratorConfigUITemplate configUi = null;
	
	private static final IGeneratorConfigUITemplate EMPTY_TEMPLATE = new IGeneratorConfigUITemplate() {
		
		@Override
		public Set<String> getKeys() {
			return Collections.emptySet();
		}
		
		@Override
		public String getContent(GeneratorServiceInfo serviceInfo) {
			return "";
		}
	};
	
	public static Generator create(String configFile, Class<? extends IVortoCodeGenerator> generatorClass) {
		Objects.requireNonNull(configFile);
		Objects.requireNonNull(generatorClass);
		
		try {
			IVortoCodeGenerator instance = generatorClass.newInstance(); 
			return new Generator(GatewayUtils.generatorInfoFromFile(configFile, instance), instance, createConfigUI(instance));
		} catch (Exception e) {
			throw new GeneratorCreationException("Error in instantiating Generator", e);
		}
	}
	
	private static IGeneratorConfigUITemplate createConfigUI(IVortoCodeGenerator generator) {
		if (generator.getInfo().isConfigurable()) {
			return new DefaultGeneratorConfigUI(generator.getInfo());
		} else {
			return EMPTY_TEMPLATE;
		}
	}

	private Generator(GeneratorServiceInfo info, IVortoCodeGenerator instance, IGeneratorConfigUITemplate configTemplate) {
		this.info = Objects.requireNonNull(info);
		this.instance = Objects.requireNonNull(instance);
		this.configUi = Objects.requireNonNull(configTemplate);
	}
	
	public GeneratorServiceInfo getInfo() {
		return info;
	}

	public void setInfo(GeneratorServiceInfo info) {
		this.info = info;
	}

	public IVortoCodeGenerator getInstance() {
		return instance;
	}

	public void setInstance(IVortoCodeGenerator instance) {
		this.instance = instance;
	}

	public GeneratorServiceInfo getFullInfo() {
		this.info.setConfigTemplate(configUi.getContent(info));
		this.info.setConfigKeys(configUi.getKeys());
		return this.info;
	}
}
