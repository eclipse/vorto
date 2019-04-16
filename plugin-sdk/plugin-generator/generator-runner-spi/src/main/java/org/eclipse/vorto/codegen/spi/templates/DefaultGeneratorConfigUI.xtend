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
package org.eclipse.vorto.codegen.spi.templates

import java.util.stream.Collectors
import org.eclipse.vorto.codegen.api.GeneratorInfo
import org.eclipse.vorto.codegen.api.GeneratorInfo.BinaryConfigurationItem
import org.eclipse.vorto.codegen.api.GeneratorInfo.ChoiceConfigurationItem
import org.eclipse.vorto.codegen.api.GeneratorInfo.TextConfigurationItem
import org.eclipse.vorto.codegen.api.GeneratorServiceInfo

class DefaultGeneratorConfigUI implements IGeneratorConfigUITemplate{
	
	private GeneratorInfo generatorInfo;
	
	new(GeneratorInfo generatorInfo) {
		this.generatorInfo = generatorInfo;
	}
	
	override getContent(GeneratorServiceInfo serviceInfo) {
		'''
		<div class="form-group">
		«FOR item : generatorInfo.configurationItems»
			<div class="row">
				<div class="col-sm-6">
					<div class="box box-primary">
						<div class="box-header with-border">
							<h3 class="box-title">«item.label»</h3>
							<span class="label label-primary pull-right"><i class="fa fa-cloud"></i></span>
						</div><!-- /.box-header -->
						<div class="box-body">
							«IF item instanceof ChoiceConfigurationItem»
								«FOR choice : item.choices»
									<p><input type="radio" ng-model="configParams.«item.key»" value="«choice.value»">&nbsp;«choice.label»</p>
								«ENDFOR»
								«ELSEIF item instanceof BinaryConfigurationItem»
									<input type="checkbox" ng-model="configParams.«item.key»"><i>&nbsp;Include</i>
								«ELSEIF item instanceof TextConfigurationItem»
									<input type="input" ng-model="configParams.«item.key»" «IF (item as TextConfigurationItem).defaultValue.isPresent»value="«(item as TextConfigurationItem).defaultValue.get»"«ENDIF»>
							«ENDIF»
						</div><!-- /.box-body -->
					</div><!-- /.box -->
				</div>
				«IF serviceInfo.key == "boschiotsuite"»
				<div class="col-sm-6">
					<div class="box box-primary">
						<div class="box-header with-border">
							<h3 class="box-title">Web Application</h3>
							<span class="label label-primary pull-right"><i class="fa fa-dashboard"></i></span>
						</div><!-- /.box-header -->
						<div class="box-body">
							<p>Visualizes device data for Vorto - described devices in a tiny Web UI (requires NodeJS runtime). <a href="https://download.eclipse.org/vorto/downloads/vorto-webui.zip">Download App</a></p>
							<img src="webjars/repository-web/dist/images/bosch_iot_suite_webapp.png" width="50%" />
						</div><!-- /.box -->
					</div>
				</div>
				«ENDIF»
			</div>
		«ENDFOR»
		'''
	}
	
	override getKeys() {
		return generatorInfo.configurationItems.stream.map[it.key].collect(Collectors.toSet)
	}
	
}