package com.bosch.iotsuite.console.generator.application.templates.resources.partials

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.model.ModelId
import org.eclipse.vorto.core.api.model.model.ModelType
import com.bosch.iotsuite.console.generator.application.templates.resources.js.DetailsControllerTemplate

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
		                <span class="info-box-icon bg-white"><img width="64px" height="64px" ng-src="http://vortorepo.apps.bosch-iot-cloud.com/rest/model/image/{{thing.thingType.namespace}}/{{thing.thingType.name}}/{{thing.thingType.version}}"/></span>
		                <div class="info-box-content">
		                  <span class="info-box-text">{{thing.name}}</span>
		                  <a href="http://vortorepo.apps.bosch-iot-cloud.com/#/details/{{thing.thingType.namespace}}/{{thing.thingType.name}}/{{thing.thingType.version}}" target="_blank" class="small-box-footer">More info <i class="fa fa-arrow-circle-right"></i></a>
		                </div><!-- /.info-box-content -->
		              </div><!-- /.info-box -->
		            </div><!-- /.col -->
		           </div>
							<!-- Small boxes (Stat box) -->
							<!-- Main row -->
							<div class="row">
								«FOR fbProperty : element.properties»
									«var modelId = new ModelId(ModelType.Functionblock,fbProperty.type.name,fbProperty.type.namespace,fbProperty.type.version)»
									«var template = DetailsControllerTemplate.UI_COMPONENTS.getOrDefault(modelId,DetailsControllerTemplate.DEFAULT_UI_TEMPLATE)»
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