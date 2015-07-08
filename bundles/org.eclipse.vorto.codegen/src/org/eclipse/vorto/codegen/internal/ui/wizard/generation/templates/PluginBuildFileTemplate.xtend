/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
 
package org.eclipse.vorto.codegen.internal.ui.wizard.generation.templates

import java.util.ArrayList
import java.util.List
import org.eclipse.vorto.codegen.api.tasks.ITemplate

class PluginBuildFileTemplate<Context> implements ITemplate<Context> {
	
	val String SOURCE = "source.. = "; 
	val String OUTPUT = "output.. = ";
	val String BIN_INCLUDES = "bin.includes = "; 
	val String BIN_EXCLUDES = "bin.excludes = "; 
	val String SRC_INCLUDES = "src.includes = "; 
	val String SRC_EXCLUDES = "src.excludes = "; 

	List<String> sources = new ArrayList<String>();
	List<String> output = new ArrayList<String>();
	List<String> binIncludes = new ArrayList<String>();
	List<String> binExcludes = new ArrayList<String>();
	List<String> srcIncludes = new ArrayList<String>();
	List<String> srcExcludes = new ArrayList<String>();
	
	def org.eclipse.vorto.codegen.internal.ui.wizard.generation.templates.PluginBuildFileTemplate<Context> setSources(List<String> sources) {
		this.sources = sources;
		return this;
	}

	def org.eclipse.vorto.codegen.internal.ui.wizard.generation.templates.PluginBuildFileTemplate<Context> setOutput(List<String> output) {
		this.output = output;
		return this;
	}

	def org.eclipse.vorto.codegen.internal.ui.wizard.generation.templates.PluginBuildFileTemplate<Context> setBinIncludes(List<String> binIncludes) {
		this.binIncludes = binIncludes;
		return this;
	}
	
	def org.eclipse.vorto.codegen.internal.ui.wizard.generation.templates.PluginBuildFileTemplate<Context> setBinExcludes(List<String> binExcludes) {
		this.binExcludes = binExcludes;
		return this;
	}
	
	def org.eclipse.vorto.codegen.internal.ui.wizard.generation.templates.PluginBuildFileTemplate<Context> setSrcIncludes(List<String> srcIncludes) {
		this.srcIncludes = srcIncludes;
		return this;
	}
	
		def org.eclipse.vorto.codegen.internal.ui.wizard.generation.templates.PluginBuildFileTemplate<Context> setSrcExcludes(List<String> srcExcludes) {
		this.srcExcludes = srcExcludes;
		return this;
	}

	public override String getContent(Context context) {
		return '''
			«getList(SOURCE, sources)»
			«getList(OUTPUT, output)»
			«getList(BIN_INCLUDES, binIncludes)»
			«getList(BIN_EXCLUDES, binExcludes)»
			«getList(SRC_INCLUDES, srcIncludes)»
			«getList(SRC_EXCLUDES, srcExcludes)»
		'''
	}
	
	def getList(String startString, List<String> list) 
		'''«IF !list.isEmpty»
		«startString»«FOR iterator : list»«iterator.toString»«IF !list.indexOf(iterator).equals(list.size - 1)»,\
		«getSpace(startString)»«ELSE»
		«ENDIF»«ENDFOR»«ENDIF»'''
		
	def String getSpace(String startString) {
		var charArray = startString.toCharArray;
		for (i : 0 ..< startString.length) {
			charArray.set(i," ");
		}
		return new String(charArray);
	}
}
