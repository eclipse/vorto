package org.eclipse.vorto.repository.server.config.config;

import org.eclipse.vorto.editor.web.datatype.DatatypeServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebEditorConfiguration {

  @Bean
  public ServletRegistrationBean datatyepXtextServlet() {
    return new ServletRegistrationBean(new DatatypeServlet(), "/datatype/xtext-service/*");
  }
}
