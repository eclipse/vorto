/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
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
