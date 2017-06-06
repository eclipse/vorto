package com.bosch.iotsuite.console.generator.application.templates.resources.partials

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class BrowserTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''browser.html'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static/partials'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>
				«element.name» Browser
				<small>Browse your connected devices</small>
			</h1>
			<ol class="breadcrumb">
		    	<li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		    	<li class="active">«element.name» Browser</li>
			</ol>
		</section>
		     
			<!-- Main content -->
			<section class="content">
				<div class="box box-default">
					<div class="box-header with-border">
						<div class="row">
					 		<div class="col-xs-6">			
								<h3 ng-show="isLoading" class="box-title"><i class="fa fa-refresh fa-spin"></i>&nbsp;&nbsp; Searching for devices </h3>
								<h3 ng-show="!isLoading" class="box-title">{{things.length}} of {{thingsCount}} devices displayed</h3>						
							</div>
						</div>			
					</div>			
					<div class="box-body" ng-show="things.length == 0">
						You have no connected devices yet.
					</div>
					<div class="box-body" ng-show="things.length > 0">
						<div>
							<ul class="pagination pagination-sm no-margin pull-right">
						       	<li><a href="" ng-show="!listView" ng-click="toggleView()"><span class="fa fa-list-ul"></span></a></li>
						       	<li><a href="" ng-show="listView" ng-click="toggleView()"><span class="fa fa-th"></span></a></li>
						    </ul>		
							<div ng-show="!listView">
					 			<div class="row" ng-repeat="row in thingsMatrix">
					 	 			<div class="col-md-4" ng-repeat="thing in row">
					 	 				<!--box-->
				                		<a ui-sref="details({ thingId: thing.thingId })">
					            			<div class="tiny-box" id="tinyBox:{{thing.thingId}}" ng-click="toggleBox(thing.thingId)">
						                		<span ng-show="thing.thingType" class="tiny-box-icon"><img width="64px" height="64px" ng-src="http://vortorepo.apps.bosch-iot-cloud.com/rest/model/image/{{thing.thingType.namespace}}/{{thing.thingType.name}}/{{thing.thingType.version}}"/></span>
						               			<span ng-show="!thing.thingType" class="tiny-box-icon"><img width="64px" height="64px" ng-src="img/noImageIcon.png"/></span>
						               			<div class="tiny-box-name"><span>{{thing.name}}</span></div>	
					                 		</div>
					                	</a>
				            		<!--box end-->
				            		</div>
					 	 		</div>                        
				       		</div>		   			   	
					   		<div ng-show="listView">			   				   		
						 		<div ng-repeat="row in thingsMatrix">
						 	 		<div ng-repeat="thing in row">
						 	 			<div class="row">				 	 			
						 	 				<div class="col-xs-1">
						 	 					<span ng-show="thing.thingType"><img width="16px" height="16px" ng-src="http://vortorepo.apps.bosch-iot-cloud.com/rest/model/image/{{thing.thingType.namespace}}/{{thing.thingType.name}}/{{thing.thingType.version}}"/></span>
						               			<span ng-show="!thing.thingType"><img width="16px" height="16px" ng-src="img/noImageIcon.png"/></span>
						               		</div>			 	 			
							 	 			<div class="col-xs-8">
						            			<a ui-sref="details({ thingId: thing.thingId })"><p class="breakWord">{{thing.name}}</p></a>
						            		</div>
						            	</div>
					            	</div>
					            </div>
					      	</div>	
					 	</div>
					</div>
					<!-- /.box-body -->
				</div>
				<!-- /.box -->
			</section>
			<!-- /.content -->
		'''
	}
	
}