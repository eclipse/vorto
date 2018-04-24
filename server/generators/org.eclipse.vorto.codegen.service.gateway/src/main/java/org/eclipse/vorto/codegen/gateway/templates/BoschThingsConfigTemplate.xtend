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
import org.eclipse.vorto.server.commons.ui.IGeneratorConfigUITemplate

class BoschThingsConfigTemplate implements IGeneratorConfigUITemplate {
	
	private static final Set<String> KEYS = new HashSet(Arrays.asList("simulator","validation","alexa", "kura", "webui","thingId"));
	
	override getContent(GeneratorServiceInfo info) {
		'''
		<div class="form-group">
			<div class="row">
				<div class="col-sm-12">
					<p>Thing ID: <input type="text" size="50" ng-model="configParams.thingId" placeholder="Optional Thing ID, e.g com.mycompany:4711"></p>
				</div>
			</div>
			<div class="row">
			<div class="col-sm-4">
				<div class="box box-primary">
			    	<div class="box-header with-border">
			      		<h3 class="box-title">Device Simulator</h3>
			      		<span class="label label-primary pull-right"><i class="fa fa-play-circle"></i></span>
			    	</div><!-- /.box-header -->
					<div class="box-body">
						<p>Creates a thing client that sends {{model.id.name}} data to Bosch IoT Things via HTTP</p>
						<input type="checkbox" ng-model="configParams.simulator"><i>&nbsp;Yes, I need that</i>
					</div><!-- /.box-body -->
			  	</div><!-- /.box -->
			</div>
			<div class="col-sm-4">
				<div class="box box-primary">
			    	<div class="box-header with-border">
			      		<h3 class="box-title">Device Voice-Control</h3>
			      		<span class="label label-primary pull-right"><i class="fa fa-microphone"></i></span>
			    	</div><!-- /.box-header -->
					<div class="box-body">
						<p>Creates an Alexa Skillset that fetches {{model.id.name}} properties from Bosch IoT Things via voice commands.</p>
						<input type="checkbox" ng-model="configParams.alexa" ><i>&nbsp;Yes, I need that</i>
					</div><!-- /.box-body -->
			  	</div><!-- /.box -->
			</div>
			<div class="col-sm-4">
				<div class="box box-primary">
					<div class="box-header with-border">
						<h3 class="box-title">Device Validation</h3>
						<span class="label label-primary pull-right"><i class="fa fa-dashboard"></i></span>
					</div><!-- /.box-header -->
					<div class="box-body">
						<p>Creates JSON Schema files to validate {{model.id.name}} properties in Bosch IoT Things</p>
						<input type="checkbox" ng-model="configParams.validation" ><i>&nbsp;Yes, I need that</i>
					</div><!-- /.box-body -->
				</div><!-- /.box -->
			</div>
			</div>
			<div class="row">
			<div class="col-sm-4">
				<div class="box box-primary">
					<div class="box-header with-border">
						<h3 class="box-title">Device Gateway</h3>
						<span class="label label-primary pull-right"><i class="fa fa-dashboard"></i></span>
					</div><!-- /.box-header -->
					<div class="box-body">
						<p>Reads device data via BLE using Eclipse Kura and sends it to Bosch IoT Things</p>
						<input type="checkbox" ng-model="configParams.kura" ><i>&nbsp;Yes, I need that</i>
					</div><!-- /.box-body -->
				</div><!-- /.box -->
			</div>
			<div class="col-sm-4">
				<div class="box box-primary">
					<div class="box-header with-border">
						<h3 class="box-title">Device Visualization</h3>
						<span class="label label-primary pull-right"><i class="fa fa-dashboard"></i></span>
					</div><!-- /.box-header -->
					<div class="box-body">
						<p>Consumes {{model.id.name}} data from Bosch IoT Things and displays it in a Web UI</p>
						<input type="checkbox" ng-model="configParams.webui" ><i>&nbsp;Yes, I need that</i>
					</div><!-- /.box-body -->
				</div><!-- /.box -->
			</div>
			</div>
		</div>
		'''
	}
	
	override getKeys() {
		return KEYS;
	}
}