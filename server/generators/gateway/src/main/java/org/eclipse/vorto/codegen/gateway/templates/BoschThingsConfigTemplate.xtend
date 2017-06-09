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
package org.eclipse.vorto.codegen.gateway.templates

import java.util.Arrays
import java.util.HashSet
import java.util.Set
import org.eclipse.vorto.codegen.api.GeneratorServiceInfo
import org.eclipse.vorto.server.commons.IGeneratorConfigUITemplate

class BoschThingsConfigTemplate implements IGeneratorConfigUITemplate {
	
	private static final Set<String> KEYS = new HashSet(Arrays.asList("simulator","validation","alexa"));
	
	override getContent(GeneratorServiceInfo info) {
		'''
		<div class="form-group">
			<div class="col-sm-4">
				<div class="box box-primary">
			    	<div class="box-header with-border">
			      		<h3 class="box-title">Simulate the {{model.id.name}}</h3>
			      		<span class="label label-primary pull-right"><i class="fa fa-dashboard"></i></span>
			    	</div><!-- /.box-header -->
					<div class="box-body">
						<p>Creates a thing client that sends {{model.id.name}} data to Bosch IoT Things via HTTP</p>
						<input type="checkbox" ng-model="configParams.simulator">
					</div><!-- /.box-body -->
			  	</div><!-- /.box -->
			</div>
			<div class="col-sm-4">
				<div class="box box-primary">
			    	<div class="box-header with-border">
			      		<h3 class="box-title">Voice-Control the {{model.id.name}}</h3>
			      		<span class="label label-primary pull-right"><i class="fa fa-dashboard"></i></span>
			    	</div><!-- /.box-header -->
					<div class="box-body">
						<p>Creates an Alexa Skillset that allows you to fetch {{model.id.name}} properties from Bosch IoT Things</p>
						<input type="checkbox" ng-model="configParams.alexa" >
					</div><!-- /.box-body -->
			  	</div><!-- /.box -->
			</div>
			<div class="col-sm-4">
				<div class="box box-primary">
					<div class="box-header with-border">
						<h3 class="box-title">Validate the {{model.id.name}}</h3>
						<span class="label label-primary pull-right"><i class="fa fa-dashboard"></i></span>
					</div><!-- /.box-header -->
					<div class="box-body">
						<p>Creates JSON Schema files to validate {{model.id.name}} Bosch IoT Thing properties</p>
						<input type="checkbox" ng-model="configParams.validation" >
					</div><!-- /.box-body -->
				</div><!-- /.box -->
			</div>
		</div>
		'''
	}
	
	override getKeys() {
		return KEYS;
	}
}