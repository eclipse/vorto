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
package org.eclipse.vorto.repository.notification.message;

import java.io.StringWriter;
import java.util.Map;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateRenderer {
  private static Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);

  static {
    configuration.setClassForTemplateLoading(TemplateRenderer.class, "/emailtemplates");
  }

  private String templateName;

  public TemplateRenderer(String templateName) {
    this.templateName = templateName;
  }

  public String render(Map<String, Object> vars) throws Exception {
    Template template = configuration.getTemplate(templateName);
    StringWriter sw = new StringWriter();
    template.process(vars, sw);
    return sw.toString();
  }
}
