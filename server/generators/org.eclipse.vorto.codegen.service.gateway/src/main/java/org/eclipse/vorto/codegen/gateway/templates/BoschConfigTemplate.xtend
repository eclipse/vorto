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

class BoschConfigTemplate implements IGeneratorConfigUITemplate {
	
	private static final Set<String> KEYS = new HashSet(Arrays.asList("language", "gateway"));
	
	override getContent(GeneratorServiceInfo info) {
		'''
		<div class="form-group">
					<div class="row">
						<div class="col-sm-6">
							<div class="box box-primary">
						    	<div class="box-header with-border">
						      		<h3 class="box-title">Choose device platform:</h3>
						      		<span class="label label-primary pull-right"><i class="fa fa-cloud"></i></span>
						    	</div><!-- /.box-header -->
								<div class="box-body">
									<p><input type="radio" ng-model="configParams.language" value="arduino">&nbsp;Arduino</p>
									<p><input type="radio" ng-model="configParams.language" value="python">&nbsp;Python</p>
									<p><input type="radio" ng-model="configParams.language" value="java">&nbsp;Java</p>
								</div><!-- /.box-body -->
						  	</div><!-- /.box -->
						</div>
					</div>
					<div class="row">
								<div class="col-sm-6">
									<div class="box box-primary">
								    	<div class="box-header with-border">
								      		<h3 class="box-title">Bosch IoT Suite Gateway</h3>
								      		<span class="label label-primary pull-right"><i class="fa fa-play-circle"></i></span>
								    	</div><!-- /.box-header -->
										<div class="box-body">
											<p>Generates an OSGI bundle that can run on the Bosch IoT Suite Gateway.</p>
											<input type="checkbox" ng-model="configParams.gateway"><i>&nbsp;Include</i>
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