/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.web.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RestController @RequestMapping(value = "/api") public class ApidocController {

    @Value("${server.contextPath}") private String contextPath;

    @GetMapping(value = "/context", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getContext() {

        if (contextPath == "/")
            contextPath = "";

        if (contextPath.startsWith("/"))
            contextPath = contextPath.substring(1);

        return contextPath;
    }

}
