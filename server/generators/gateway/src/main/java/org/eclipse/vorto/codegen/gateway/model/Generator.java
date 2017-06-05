package org.eclipse.vorto.codegen.gateway.model;

import java.util.Objects;

import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.gateway.exception.GeneratorCreationException;
import org.eclipse.vorto.codegen.gateway.utils.GatewayUtils;
import org.eclipse.vorto.server.commons.DefaultConfigTemplate;
import org.eclipse.vorto.server.commons.IGeneratorConfigUITemplate;

public class Generator {
	private GeneratorServiceInfo info;
	private IVortoCodeGenerator instance;
	private IGeneratorConfigUITemplate configUi = new DefaultConfigTemplate();
	
	public static Generator create(String configFile, Class<? extends IVortoCodeGenerator> generatorClass) {
		Objects.requireNonNull(configFile);
		Objects.requireNonNull(generatorClass);
		
		try {
			IVortoCodeGenerator instance = generatorClass.newInstance(); 
			return new Generator(GatewayUtils.generatorInfoFromFile(configFile, instance), instance);
		} catch (Exception e) {
			throw new GeneratorCreationException("Error in instantiating Generator", e);
		}
	}
	
	private Generator(GeneratorServiceInfo info, IVortoCodeGenerator instance) {
		this.info = Objects.requireNonNull(info);
		this.instance = Objects.requireNonNull(instance);
		this.info.setConfigTemplate(configUi.getContent(info));
		this.info.setConfigKeys(configUi.getKeys());
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
}
