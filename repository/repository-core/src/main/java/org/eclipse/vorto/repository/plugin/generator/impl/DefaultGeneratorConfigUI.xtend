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
package org.eclipse.vorto.repository.plugin.generator.impl

class DefaultGeneratorConfigUI {
	
	
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
	
}
