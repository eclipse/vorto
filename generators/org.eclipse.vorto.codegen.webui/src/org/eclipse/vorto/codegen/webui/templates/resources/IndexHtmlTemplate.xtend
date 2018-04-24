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
package org.eclipse.vorto.codegen.webui.templates.resources

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class IndexHtmlTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''index.html'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		<!DOCTYPE html>
		<html class="full" lang="en" ng-app='«element.name.toLowerCase»App'>
		<head>
		
		    <meta charset="utf-8">
		    <meta http-equiv="X-UA-Compatible" content="IE=edge">
		    <meta name="viewport" content="width=device-width, initial-scale=1">
		    <meta name="description" content="">
		    <meta name="author" content="">
		
		    <title>«element.name» Solution</title>
		    
		    <link rel="stylesheet" href="webjars/bootstrap/css/bootstrap.min.css" type="text/css">
		    <link rel="stylesheet" href="webjars/font-awesome/css/font-awesome.min.css" type="text/css">
		    <link rel="stylesheet" href="webjars/adminlte/dist/css/skins/_all-skins.min.css">
		    <link rel="stylesheet" href="webjars/adminlte/dist/css/AdminLTE.min.css">
		    <link rel="stylesheet" href="css/style.css">
		    		    
		    «IF context.configurationProperties.getOrDefault("swagger","true").equalsIgnoreCase("true")»
		    <link rel="stylesheet" href="webjars/angular-swagger-ui/0.2.7/dist/css/swagger-ui.min.css">
		    «ENDIF»
		    
		    <script src="webjars/jquery/jquery.min.js"></script>
		    <script src="webjars/bootstrap/js/bootstrap.min.js"></script>
		    <script src="webjars/angularjs/angular.min.js"></script>
		    <script src="webjars/angular-ui-bootstrap/ui-bootstrap-tpls.min.js"></script>
		    <script src="webjars/angular-ui-router/release/angular-ui-router.min.js"></script>
		    
		    <!-- OpenStreetMap UI component stuff -->
			<link rel="stylesheet" href="webjars/leaflet/leaflet.css" />
			<script src="webjars/leaflet/leaflet.js"></script>
			<script src="webjars/angular-leaflet-directive/angular-leaflet-directive.js"></script>
			
			<!-- Barchart UI component stuff -->
			<script src="webjars/d3js/d3.min.js"></script>
			<script src="webjars/nvd3/nv.d3.min.js"></script>
			<script src="webjars/angular-nvd3/angular-nvd3.min.js"></script>
			<link rel="stylesheet" href="webjars/nvd3/nv.d3.css">
			
			<!-- Gauge UI component stuff -->
			<script src="webjars/justgage/justgage.js"></script>
			<script src="webjars/raphaeljs/raphael-min.js"></script>
			<script src="dist/js/angular-gage.min.js"></script>
			
			
			<!-- SockJS for websockets  -->
			<script src="webjars/sockjs-client/sockjs.min.js"></script>
			<script src="webjars/stomp-websocket/stomp.min.js"></script>
			
			«IF context.configurationProperties.getOrDefault("swagger","true").equalsIgnoreCase("true")»
			<script type="text/javascript" src="webjars/angular-swagger-ui/0.2.7/dist/scripts/swagger-ui.min.js"></script>
			«ENDIF»
		    
		    <script src="js/app.js"></script>
		    <script src="js/browser-controller.js"></script>
		    <script src="js/location-controller.js"></script>
		    <script src="js/login-controller.js"></script>
		    <script src="js/details-controller.js"></script>
		    «IF context.configurationProperties.getOrDefault("swagger","true").equalsIgnoreCase("true")»
		    <script src="js/api-controller.js"></script>
		    «ENDIF»
		
		</head>
		
		<body class="skin-blue">
		 <div class="wrapper">
		   <!-- Main Header -->
		   <header class="main-header">
		     <!-- Logo -->
		     <a href="#" class="logo">
		       <!-- mini logo for sidebar mini 50x50 pixels -->
		       <span class="logo-mini"><b>IoT</b></span>
		       <!-- logo for regular state and mobile devices -->
		       <span class="logo-lg"><b>«element.name»</b>App</span>
		     </a>
		     <!-- Header Navbar -->
		     <nav class="navbar navbar-static-top" role="navigation">
		       <!-- Sidebar toggle button-->
		       <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
		         <span class="sr-only">Toggle navigation</span>
		       </a>
		       <!-- Navbar Right Menu -->
		       <div class="navbar-custom-menu">
		         <ul class="nav navbar-nav">
		         </ul>
		       </div>
		     </nav>
		   </header>
		
		   <!-- Left side column. contains the logo and sidebar -->
		   <aside class="main-sidebar">
		     <section class="sidebar">
		       <ul class="sidebar-menu user-panel" ng-show="authenticated">
					            <li class="user-menu treeview image open active">
					                <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">
					                  <span class="hidden-xs"><strong>{{currentUser}}</strong></span>
					                  <i class="fa fa-angle-left pull-right"></i>
					                </a>
					                <ul class="treeview-menu menu-open" style="display: block;">
					                    <li><a href="#"><strong>Subject: </strong></a><span style="color: white">{{ currentUserSub }}</span></li>
										<li><a href="#" ng-click="logout();"><i class="fa fa-sign-out"></i> Sign out</a></li>
									</ul>
				              </li>
				          </ul>
		       <!-- Sidebar Menu -->
		       <ul class="sidebar-menu">
		         <li class="header">Home</li>
		         <li><a href="#/browser"><i class="fa fa-th"></i> <span>«element.name» Browser</span></a></li>
		         <li><a href="#/api"><i class="fa fa-th"></i> <span>Swagger</span></a></li>
		       </ul><!-- /.sidebar-menu -->
		     </section>
		     <!-- /.sidebar -->
		   </aside>
		   <!-- Content Wrapper. Contains page content -->
		   <div class="content-wrapper" style="min-height:988px;" ui-view> </div>
		   <!-- Main Footer -->
		   <footer class="main-footer">
		     <!-- To the right -->
		     <div class="pull-right hidden-xs">
		       Generated by Eclipse Vorto
		     </div>
		   </footer>
		
		 </div><!-- ./wrapper -->
		 </body>

		
		</html>
		'''
	}
	
}
