package com.bosch.iotsuite.console.generator.application.templates.resources.ui;

import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;

public interface IFunctionBlockUITemplate {

	String renderJavascript(FunctionblockProperty fbProperty,InvocationContext ctx);
	
	String renderHtml(FunctionblockProperty fbProperty,InvocationContext ctx);
}
