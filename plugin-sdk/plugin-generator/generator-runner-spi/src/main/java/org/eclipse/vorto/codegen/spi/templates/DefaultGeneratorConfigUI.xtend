/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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
		<div class="modal-body">
			<div>
				<div class="row">
					<div class="col-sm-2">
						<img src="data:image/png;base64,{{generator.image144x144}}" width="144" height="144">
					</div>
					<div class="col-sm-10 generator">
						<ul>	
							<li><h4><b>{{generator.name}}</b></h4></li>
					        <li><small>{{generator.creator}}</small></li>
					        <li class="desc">{{generator.description}}</li>
				            <li class="doc"><small><a ng-href="{{generator.documentationUrl}}"> <i class="fa fa-fw fa-book"></i>Documentation</a></small></li>
					    </ul>
					</div>
				</div>
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
							</div>
						«ENDFOR»
						</div>
			</div>
		</div>
		<div class="modal-footer">
			<div class="align-right">
				<button class="btn btn-primary" type="submit" ng-disabled="!enableGeneratorButton()" ng-click="generate()"><i class="fa fa-download"></i> Generate code</button>
				<button  class="btn btn-default" type="button" ng-click="cancel()">Cancel</button>
			</div>
		</div>
		'''
	}
	
	override getKeys() {
		return generatorInfo.configurationItems.stream.map[it.key].collect(Collectors.toSet)
	}
	
}