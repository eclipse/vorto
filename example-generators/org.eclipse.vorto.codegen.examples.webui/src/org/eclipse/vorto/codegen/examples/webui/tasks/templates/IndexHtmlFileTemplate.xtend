/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
 package org.eclipse.vorto.codegen.examples.webui.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class IndexHtmlFileTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		return "index.html";
	}
	
	override getPath(InformationModel context) {
		return "webdevice.example/src/main/resources/static";
	}
			
	override getContent(InformationModel informationModel,InvocationContext invocationContext) {
		return '''
		<html ng-app="deviceApp">
		<head>
		  <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		  <link rel="stylesheet" href="css/style.css">
		  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.4/angular.js" type="text/javascript"></script>
		  <script src="script/app.js" type="text/javascript"></script>
		  <script src="script/controllers.js" type="text/javascript"></script>
		  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
		  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.4/angular-route.js"></script>
		  <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.1/js/bootstrap.min.js"></script>
		  <script type="text/javascript" src="//cdn.jsdelivr.net/sockjs/0.3.4/sockjs.min.js"></script>
		  <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
		  
		  <title>{{"«informationModel.name»" | titlecase}}</title>
		  <meta http-equiv="cache-control" content="max-age=0" />
		  <meta http-equiv="cache-control" content="no-cache" />
		  <meta http-equiv="expires" content="0" />
		  <meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
		  <meta http-equiv="pragma" content="no-cache" />
		</head>
		<body>
		  <div class="container">
		    <div class="row">
		      <div class="col-md-12">
		        <nav class="navbar navbar-default" role="navigation">
		   				<div class="navbar-header">
		   					<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
		   						 <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
		   					</button>
		             <a class="navbar-brand" href="#/">{{"«informationModel.name»" | titlecase}}</a>
		   				</div>
		        </nav>
		
		          <div class="row">
		            <div class="col-md-12">
		     			<div class="panel panel-primary">
		     				<div class="panel-heading">
		                   		<h3 class="panel-title">Device Information</h3>
		                	</div>
		                	<div>
							  <fieldset class="scheduler-border">
							    <div class="col-md-6">
							      <div class="control-group">
							        <label class="control-label input-label" style="width:40%;">Name</label>
							           {{deviceInfo.displayName}}
							      </div>
							      <div class="control-group">
							        <label class="control-label input-label" style="width:40%;">Namespace</label>
							           {{deviceInfo.namespace}}
							      </div>
							      <div class="control-group">
							        <label class="control-label input-label" style="width:40%;">Version</label>
							           {{deviceInfo.version}}
							      </div>
							      <div class="control-group">
							        <label class="control-label input-label" style="width:40%;">Description</label>
							           {{deviceInfo.description}}
							      </div>
							    </div>
							  </fieldset>
							</div>
		                </div>
		            </div>
		          </div>
		        </div>
		
		        <!-- Function Block Details -->
				<ul class="nav nav-pills">
					«FOR fbm : informationModel.properties»
					<li ng-class="{active:isActiveTab('/«fbm.type.name.toLowerCase»')}">
						<a href="#/«fbm.type.name.toLowerCase»">{{"«fbm.type.name»" | titlecase}}</a>
					</li>
					«ENDFOR»		
				</ul>
		        <div id="main" class="panel panel-primary">
		          <div ng-view></div>
		        </div>
		      </div>
		    </div>
		  <div class="row" ng-show="responseMessage">
		  	<div class="col-md-12">
		  		<div class="panel panel-success">
		  			<div class="panel-heading">
		  				<h3 class="panel-title">Information Panel </h3>
		  			</div>
		  			<div class="panel-body">
		  				{{responseMessage}}
		  			</div>
		  		</div>
		  	</div>
		  </div>
		 </div>
		</body>
		</html>
'''
	}	
}
