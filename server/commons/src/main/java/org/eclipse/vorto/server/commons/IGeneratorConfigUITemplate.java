package org.eclipse.vorto.server.commons;

import java.util.Set;

import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;

public interface IGeneratorConfigUITemplate {

	String getContent(GeneratorServiceInfo serviceInfo);
	
	Set<String> getKeys();
}
