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
package org.eclipse.vorto.codegen.template.runner

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class DockerComposeTemplate implements IFileTemplate<InformationModel> {

    override getFileName(InformationModel context) {
        'docker-compose.yml'
    }

    override getPath(InformationModel context) {
        'generator-parent/generator-runner'
    }

    override getContent(InformationModel element, InvocationContext context) {
        '''
version: '3'
services:
  repository:
    image: eclipsevorto/vorto-repo
    ports:
      - "8080:8080"
    volumes:
      - "./docker:/code/config"
    environment:
      - USE_PROXY=0
    networks:
      - backend
  generators:
    depends_on: ["repository"]
    build:
      context: .
      args:
        JAR_FILE: generator-runner-0.0.1-SNAPSHOT-exec.jar
      volumes:
        - "./docker:/gen/config"
      networks:
        - backend
networks:
  backend:
    driver: bridge
'''
    }

}
