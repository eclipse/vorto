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
package org.eclipse.vorto.codegen.webui.templates.resources.partials

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class LocatorTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''locator.html'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static/partials'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		
		<!-- Content Header (Page header) -->
		     <section class="content-header">
		       <h1>
		         «element.name» Locator
		         <small>Locate your connected «element.name» devices</small>
		       </h1>
		       <ol class="breadcrumb">
		         <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		         <li class="active">«element.name» Locator</li>
		       </ol>
		     </section>
		     
			<!-- Main content -->
			<section class="content">
				<div ng-show="isLoading" class="box box-default">
					<div  class="box-header with-border">
						<h3><i class="fa fa-refresh fa-spin"></i>&nbsp;&nbsp; Locating devices </h3>
					</div>
				</div>
				<div ng-hide="isLoading" class="box box-default">			
					<div class="box-header with-border">
						<h3 ng-show="things.length-defaultThings!=1" class="box-title">Located {{things.length - defaultThings}} Things</h3> 
						<h3 ng-show="things.length-defaultThings==1" class="box-title">Located {{things.length - defaultThings}} Thing</h3> 
					</div>
					<i>Hint: Click on the marker for details</i>
					<leaflet markers="things" lf-center="singapore" width="100%" height="800px"></leaflet>
				</div>
					
			</section>
		'''
	}
	
}