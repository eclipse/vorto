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
package org.eclipse.vorto.repository.server.config.config;

import org.eclipse.vorto.editor.web.datatype.DatatypeServlet;
import org.eclipse.vorto.editor.web.functionblock.FunctionblockServlet;
import org.eclipse.vorto.editor.web.infomodel.InfomodelServlet;
import org.eclipse.vorto.editor.web.mapping.MappingServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebEditorConfiguration {
	

  @Bean
  public ServletRegistrationBean datatypeXtextServlet() {
    return new ServletRegistrationBean(new DatatypeServlet(), "/datatype/xtext-service/*");
  }
  
  @Bean
  public ServletRegistrationBean functionblockXtextServlet() {
    return new ServletRegistrationBean(new FunctionblockServlet(), "/functionblock/xtext-service/*");
  }
  
  @Bean
  public ServletRegistrationBean infomodelXtextServlet() {
    return new ServletRegistrationBean(new InfomodelServlet(), "/infomodel/xtext-service/*");
  }
  
  @Bean
  public ServletRegistrationBean mappingXtextServlet() {
    return new ServletRegistrationBean(new MappingServlet(), "/mapping/xtext-service/*");
  }
}
