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
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.resources.js.DetailsControllerTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.model.ModelId
import org.eclipse.vorto.core.api.model.model.ModelType
import org.eclipse.vorto.codegen.webui.templates.resources.ui.UIComponentFactory

class DetailsTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''details.html'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static/partials'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>
				«element.name» Details
			</h1>
			<ol class="breadcrumb">
		    	<li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		    	<li class="active">«element.name» Details</li>
			</ol>
		</section>
		     
		<!-- Main content -->
		<section class="content">
			<div class="row">
		            <div class="col-md-3 col-sm-6 col-xs-12">
		              <div class="info-box">
		                <span class="info-box-icon" style="background-color:white"><img ng-src="http://vortorepo.apps.bosch-iot-cloud.com/rest/model/image/{{thing.thingType.namespace}}/{{thing.thingType.name}}/{{thing.thingType.version}}"/></span>
		                <div class="info-box-content">
		                  <span class="info-box-text">{{thing.name}}</span>
		                  <div class="tiny-box-number">
		                  	<span href="#" data-toggle="tooltip" title=""><p class="breakeWordWithDots ng-binding" style="width: 130px">{{thing.thingId}}</p></span>
		                  </div>
		                  <a href="http://vortorepo.apps.bosch-iot-cloud.com/#/details/{{thing.thingType.namespace}}/{{thing.thingType.name}}/{{thing.thingType.version}}" target="_blank" class="small-box-footer">More info <i class="fa fa-arrow-circle-right"></i></a>
		                </div><!-- /.info-box-content -->
		              </div><!-- /.info-box -->
		            </div><!-- /.col -->
		           </div>
							<!-- Small boxes (Stat box) -->
							<!-- Main row -->
							<div class="row">
								«FOR fbProperty : element.properties»
									«var template = UIComponentFactory.getByModelId(fbProperty.type,context)»
									<section class="col-lg-4 connectedSortable ui-sortable">
										<div class="box box-solid">
											<div class="box-header">
												<h3>«fbProperty.name.toFirstUpper»</h3>
											</div>
											<div class="box-body">
												«template.renderHtml(fbProperty,context)»
											</div>
										</div>
									</section><!-- /.Left col -->
								«ENDFOR»
							</div><!-- /.row (main row) -->
		
						</section>
		<!-- /.content -->
		'''
	}
	
}