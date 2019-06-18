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
package org.eclipse.vorto.plugin.generator.config

import java.util.List
import java.util.Set
import java.util.stream.Collectors
import org.eclipse.vorto.plugin.generator.config.GeneratorConfigUIBuilder.BinaryConfigurationItem
import org.eclipse.vorto.plugin.generator.config.GeneratorConfigUIBuilder.ChoiceConfigurationItem
import org.eclipse.vorto.plugin.generator.config.GeneratorConfigUIBuilder.ConfigurationItem
import org.eclipse.vorto.plugin.generator.config.GeneratorConfigUIBuilder.TextConfigurationItem

class DefaultGeneratorConfigUI {
	
	List<ConfigurationItem> configurationItems;
	
	new(List<ConfigurationItem> configurationItems) {
		this.configurationItems = configurationItems;
	}
	
	def getContent() {
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
							«FOR item : configurationItems»
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
					<button class="btn btn-primary" type="submit" ng-disabled="!enableGeneratorButton()" ng-click="generate()"><i class="fa fa-download"></i> Generate</button>
					<button  class="btn btn-default" type="button" ng-click="cancel()">Cancel</button>
				</div>
			</div>
		'''
	}
	
	def Set<String> getKeys() {
		return configurationItems.stream.map[it.key].collect(Collectors.toSet)
	}
	
}
