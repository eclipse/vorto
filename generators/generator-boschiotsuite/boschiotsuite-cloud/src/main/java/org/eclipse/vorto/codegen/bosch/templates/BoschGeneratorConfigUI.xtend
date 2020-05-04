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
package org.eclipse.vorto.codegen.bosch.templates

class BoschGeneratorConfigUI {
	
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
										      <li class="desc">Download source code that integrates the {{model.id.name}} and its functionality with the Bosch IoT Suite.</li>
										         <li class="doc"><small><a ng-href="{{generator.documentationUrl}}"> <i class="fa fa-fw fa-book"></i>Documentation</a></small></li>
										  </ul>
								</div>
							</div>
							<div class="form-group">
										<div class="row">
											<div class="col-sm-4">
												<div class="box box-primary">
													<div class="box-header with-border">
														<h3 class="box-title">Connect device with Java</h3>
													</div><!-- /.box-header -->
													<div class="box-body">
														<p>Java code template that connects the device to Bosch IoT Hub using MQTT (Paho).</p>
														<a href="https://github.com/eclipse/vorto/blob/master/docs/tutorials/connect_javadevice.md" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-book"></i> Tutorial</a>
														<a href="./api/v1/generators/eclipsehono/models/{{model.id.prettyFormat}}?language=java" class="btn btn-primary btn-sm pull-right"><i class="fa fa-download"></i> Source Code</a>
													</div><!-- /.box-body -->
												</div><!-- /.box -->
											</div>
											<div class="col-sm-4">
												<div class="box box-primary">
													<div class="box-header with-border">
														<h3 class="box-title">Connect device with Arduino/C</h3>
													</div><!-- /.box-header -->
													<div class="box-body">
														<p>Arduino code templates that sends device data to Bosch IoT Hub using MQTT.</p>
														<a href="https://github.com/eclipse/vorto/blob/master/docs/tutorials/connect_esp8266.md" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-book"></i> Tutorial</a>
														<a href="./api/v1/generators/eclipsehono/models/{{model.id.prettyFormat}}?language=arduino" class="btn btn-primary btn-sm pull-right"><i class="fa fa-download"></i> Source Code</a>
													</div><!-- /.box-body -->
												</div><!-- /.box -->
											</div>
											<div class="col-sm-4">
												<div class="box box-primary">
													<div class="box-header with-border">
														<h3 class="box-title">Connect device with Python</h3>
													</div><!-- /.box-header -->
													<div class="box-body">
														<p>Python code template that connects the device to Bosch IoT Hub with MQTT.</p>
														 <a href="https://github.com/eclipse/vorto/blob/master/docs/tutorials/mqtt-python.md" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-book"></i> Tutorial</a>
														 <a href="./api/v1/generators/eclipsehono/models/{{model.id.prettyFormat}}?language=python" class="btn btn-primary btn-sm pull-right"><i class="fa fa-download"></i> Source Code</a>
													</div><!-- /.box-body -->
												</div><!-- /.box -->
											</div>
										</div>
										</br/>
										<div class="row">
											<div class="col-sm-4">
												<div class="box box-primary">
													<div class="box-header with-border">
														<h3 class="box-title">Provision device</h3>
													</div><!-- /.box-header -->
													<div class="box-body">
														<p>
															Script(s) that provision the device in the Bosch IoT Suite.
															<br/>
															<i><span><i class="fa fa-exclamation-triangle"></i></span> Requires <a href="https://www.getpostman.com/downloads/" target="_blank">Postman</a></i>
														</p>
														<a href="https://github.com/eclipse/vorto/blob/master/docs/tutorials/create_thing.md" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-book"></i> Tutorial</a>
														<a href="./api/v1/generators/boschiotsuite/models/{{model.id.prettyFormat}}?provision=true" class="btn btn-primary btn-sm pull-right"><i class="fa fa-download"></i> Source Code</a>
													</div><!-- /.box-body -->
												</div><!-- /.box -->
											</div>
											<div class="col-sm-4">
												<div class="box box-primary">
													<div class="box-header with-border">
														<h3 class="box-title">Template for Developer Console</h3>
													</div><!-- /.box-header -->
													<div class="box-body">
														<p>
															Template file for the provisioning wizard of the
															developer console of the Bosch IoT Suite.
															<br/>
														</p>
														<a
																href="./api/v1/generators/boschiotsuite/models/{{model.id.prettyFormat}}?provision=true&requestBodyOnly=true" class="btn btn-primary btn-sm pull-right"><i class="fa fa-download"></i> Download</a>
													</div><!-- /.box-body -->
												</div><!-- /.box -->
											</div>
											<div class="col-sm-4">
												<div class="box box-primary">
													<div class="box-header with-border">
														<h3 class="box-title">Device Digital Twin API</h3>
													</div><!-- /.box-header -->
													<div class="box-body">
														<p>
															Creates a device specific OpenAPI spec for Bosch IoT Things. You can use 
															<a href="https://editor.swagger.io">Swagger</a> tools for generating client libraries.
														</p>										
														<a href="https://github.com/eclipse/vorto/blob/master/docs/tutorials/create_openapi.md" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-book"></i> Tutorial</a>
														<a href="./api/v1/generators/openapi/models/{{model.id.prettyFormat}}" class="btn btn-primary btn-sm pull-right"><i class="fa fa-download"></i> Download</a>
													</div><!-- /.box-body -->
												</div><!-- /.box -->
											</div>
											<div class="col-sm-4">
												<div class="box box-primary">
													<div class="box-header with-border">
														<h3 class="box-title">Visualize device data</h3>
													</div><!-- /.box-header -->
													<div class="box-body">
														<p>
															
															Visualizes device data from Bosch IoT Things in a simple nodeJS/React based web app.
															<br/>
															<i><span><i class="fa fa-exclamation-triangle"></i></span> Requires <a href="https://nodejs.org/en/download/" target="_blank">Node.js</a></i>
															<br/>
															<img src="webjars/repository-web/dist/images/bosch_iot_suite_webapp2.png" width="45%" />
														</p>
														<a href="https://github.com/eclipse/vorto/blob/master/docs/tutorials/create_webapp_dashboard.md" target="_blank" class="btn btn-default btn-sm"><i class="fa fa-book"></i> Tutorial</a>
														<a href="https://www.npmjs.com/package/vorto-dashboard" class="btn btn-primary btn-sm pull-right"><i class="fa fa-download"></i> Download</a>
													</div><!-- /.box-body -->
												</div><!-- /.box -->
											</div>
										</div>
										
									</div>
						</div>
			</div>
			<div class="modal-footer">
				<div class="align-right">
					<button  class="btn btn-default" type="button" ng-click="cancel()">Close</button>
				</div>
			</div>
		'''
	}	
}
