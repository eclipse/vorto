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

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class EclipseClasspathTemplate implements IFileTemplate<InformationModel> {
		
    override getFileName(InformationModel context) {
        ".classpath"
    }
    
    override getPath(InformationModel context) {
        return null;
    }
    
    override getContent(InformationModel context,InvocationContext invocationContext) {
        '''
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
    <classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/J2SE-1.5"/>
    <classpathentry kind="con" path="org.eclipse.pde.core.requiredPlugins"/>
    <classpathentry kind="src" path="src"/>
    <classpathentry kind="output" path="bin"/>
</classpath>
        '''
    }
	
}