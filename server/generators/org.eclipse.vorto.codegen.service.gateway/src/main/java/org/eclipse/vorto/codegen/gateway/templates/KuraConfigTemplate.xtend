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

class KuraConfigTemplate implements IGeneratorConfigUITemplate {
	
	private static final Set<String> KEYS = new HashSet(Arrays.asList("bluetooth","boschcloud","boschhub"));
	
	override getContent(GeneratorServiceInfo info) {
		'''
		<div class="form-group">
			<div class="row">
				<div class="col-sm-6">
					<div class="box box-primary">
				    	<div class="box-header with-border">
				      		<h3 class="box-title">Choose driver</h3>
				      		<span class="label label-primary pull-right"><i class="fa fa-plug"></i></span>
				    	</div><!-- /.box-header -->
						<div class="box-body">
							<p><input type="checkbox" ng-model="configParams.bluetooth">&nbsp;Bluetooth LE</p>
							<p><input type="checkbox" disabled="disabled">&nbsp;<i style="color:#999">GPIO (Coming soon)</i></p>
						</div><!-- /.box-body -->
				  	</div><!-- /.box -->
				</div>
				<div class="col-sm-6">
					<div class="box box-primary">
				    	<div class="box-header with-border">
				      		<h3 class="box-title">Choose Cloud Platform Connector</h3>
				      		<span class="label label-primary pull-right"><i class="fa fa-cloud"></i></span>
				    	</div><!-- /.box-header -->
						<div class="box-body">
							<p><input type="checkbox" ng-model="configParams.boschcloud">&nbsp;Bosch IoT Suite</p>
							<p><input type="checkbox" ng-model="configParams.boschhub">&nbsp;Eclipse Hono (MQTT)</p>
							<p><input type="checkbox" checked disabled="disabled">&nbsp;<i style="color:#999">Built-in Cloud Services</i></p>
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