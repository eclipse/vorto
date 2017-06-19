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
package org.eclipse.vorto.codegen.webui.templates.resources.partials

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class LoginTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''login.html'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static/partials'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		<section class="content">
		<div class="login-box">
			<!-- /.login-logo -->
			<div class="login-box-body">
				<div ng-show="error" class="alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">�</button>
					<h4>
						<i class="icon fa fa-ban"></i>Incorrect credentials
					</h4>
				</div>
		
				<p class="login-box-msg">Sign in to start your session</p>
				<a href="google/login">
					<i class="fa fa-google fa-2x" aria-hidden="true"></i> Login with Google
				</a>
				<form ng-submit="login()">
					<div class="form-group has-feedback">
						<input type="text" class="form-control" placeholder="Username" id="username" ng-model="credentials.userId" />
					</div>
					<div class="form-group has-feedback">
						<input type="password" class="form-control" placeholder="Password" id="password" ng-model="credentials.password" />
					</div>
					<div class="row">
						<!-- /.col -->
						<div class="col-xs-4">
							<button type="submit" ng-disabled="isLoading" class="btn btn-primary" ng-click="login()">Sign In <i ng-show="isLoading" class="fa fa-refresh fa-spin"></i></button>
						</div>
						<!-- /.col -->
					</div>
				</form>
			</div>
		</div>
		</section>
		'''
	}
	
}