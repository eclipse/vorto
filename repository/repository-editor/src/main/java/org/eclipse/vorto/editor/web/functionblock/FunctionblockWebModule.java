package org.eclipse.vorto.editor.web.functionblock;

import org.eclipse.vorto.editor.web.persistence.WebEditorResourceHandler;
import org.eclipse.vorto.editor.web.persistence.WebEditorResourceSetProvider;
import org.eclipse.xtext.web.server.DefaultWebModule;
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider;
import org.eclipse.xtext.web.server.persistence.IServerResourceHandler;

public class FunctionblockWebModule extends DefaultWebModule {

  public Class<? extends IServerResourceHandler> bindIServerResourceHandler() {
    return WebEditorResourceHandler.class;
  }

  public Class<? extends IWebResourceSetProvider> bindIWebResourceSetProvider() {
    return WebEditorResourceSetProvider.class;
  }
}
