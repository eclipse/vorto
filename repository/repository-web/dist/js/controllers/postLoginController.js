/*
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
define(["../init/appController"], function (repositoryControllers) {

    repositoryControllers.controller('PostLoginController',
        ['$scope', '$rootScope', '$http', '$location',
            function ($scope, $rootScope, $http, $location) {
                if ($rootScope.postLoginRedirectUri) {
                    $location.href = $rootScope.postLoginRedirectUri;
                }
                $location.href = "/";
            }]
    );

})
