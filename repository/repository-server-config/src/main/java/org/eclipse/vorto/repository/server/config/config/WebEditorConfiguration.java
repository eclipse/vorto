package org.eclipse.vorto.repository.server.config.config;

import javax.annotation.PostConstruct;
import org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl;
import org.eclipse.vorto.editor.datatype.DatatypeRuntimeModule;
import org.eclipse.vorto.editor.web.datatype.DatatypeServlet;
import org.eclipse.vorto.editor.web.datatype.DatatypeWebModule;
import org.eclipse.vorto.editor.web.datatype.DatatypeWebSetup;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import com.google.inject.Guice;
import com.google.inject.Injector;

@Configuration
@Order(7)
public class WebEditorConfiguration {

  @Bean
  public Injector getInjectorBean() {
    return Guice.createInjector(new DatatypeRuntimeModule(), new DatatypeWebModule());
  }

  @PostConstruct
  public void setUpReferencedResourceDirectory() {
    registerEditorEMFModels();
    initEditorWebModules();
  }

  private void registerEditorEMFModels() {
    DatatypePackageImpl.init();
  }
  
  private void initEditorWebModules() {
    new DatatypeWebSetup().createInjectorAndDoEMFRegistration();
}

  @Bean
  public ServletRegistrationBean datatyepXtextServlet() {
    return new ServletRegistrationBean(new DatatypeServlet(), "/datatype/xtext-service/*");
  }
}
