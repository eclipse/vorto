/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
 
package org.eclipse.vorto.codegen.prosystfi.templates

import java.util.Set
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class FIManifestFileTemplate implements IFileTemplate<InformationModel> {
    
    var Set<String> exports
    
    new(Set<String> exports) {
        this.exports = exports
    } 
		
	public override String getContent(InformationModel context, InvocationContext invocationContext) {
		return '''
			Manifest-Version: 1.0
			Bundle-ManifestVersion: 2
			Bundle-Name: «context.displayname»
			Bundle-SymbolicName: «context.name»;singleton:=true
			Bundle-Version: «context.version»
			Import-Package: com.prosyst.mbs.services.fim,
			 com.prosyst.mbs.services.fim.annotations,
			 com.prosyst.mbs.services.fim.spi,
			 com.prosyst.util.security
			Bundle-RequiredExecutionEnvironment: JavaSE-1.5
			Export-Package: ''' + printExports
	}
	
	override getFileName(InformationModel context) {
		return "MANIFEST.MF";
	}
	
	override getPath(InformationModel context) {
		return "META-INF"
	}
	
	def String printExports() {
	    var StringBuilder res = new StringBuilder();
	    res.append(exports.get(0));
	    for (var i = 1; i < exports.size; i++) {
	        res.append(',').append("\n ").append(exports.get(i))
	    }
	    res.append('\n')
	    return res.toString
	}
	
}