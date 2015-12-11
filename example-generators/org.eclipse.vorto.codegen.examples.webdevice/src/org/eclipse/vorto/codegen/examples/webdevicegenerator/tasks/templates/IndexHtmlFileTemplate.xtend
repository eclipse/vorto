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
 package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates

import org.eclipse.vorto.codegen.api.tasks.ITemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class IndexHtmlFileTemplate implements ITemplate<InformationModel> {
			
	override getContent(InformationModel informationModel) {
		return '''
		<html ng-app="deviceApp">
		<head>
		  <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		  <link rel="stylesheet" href="css/angular.css">
		  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.4/angular.js" type="text/javascript"></script>
		  <script src="script/app.js" type="text/javascript"></script>
		  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
		  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.4/angular-route.js"></script>
		  <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.1/js/bootstrap.min.js"></script>
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
		                <div  ng-controller="mainController" ng-include="'pages/deviceinfo.html'">
		                </div>
		            </div>
		          </div>
		        </div>
		
		        <!-- Function Block Details -->
				«getFunctionBlockTabs(informationModel)»
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
	
	def getFunctionBlockTabs(InformationModel model) {
		return '''
		<ul class="nav nav-pills">
		«var i=1»
			«FOR functionBlockProperty : model.properties»
			«IF i == 1»
			<li ng-class="{active:isActiveTab('/')}">
			«ELSE»
			<li ng-class="{active:isActiveTab('/«functionBlockProperty.name»')}">
			«ENDIF»
				«getTabContent(functionBlockProperty.name, i++)»
			</li>
			
        	«ENDFOR»
		</ul>
		'''
	}
	
	def getTabContent(String fbName, int i) {
		if(i==1) 
			return '''<a href="#/">{{"«fbName»" | titlecase}}</a>'''
		else
			return '''<a href="#/«fbName»">{{"«fbName»" | titlecase}}</a>'''
	}
}
