package org.eclipse.vorto.editor.web.datatype;

import org.eclipse.vorto.editor.datatype.ide.contentassist.antlr.DatatypeParser;
import org.eclipse.vorto.editor.datatype.ide.contentassist.antlr.internal.InternalDatatypeLexer;
import org.eclipse.xtext.ide.LexerIdeBindings;
import org.eclipse.xtext.ide.editor.contentassist.antlr.IContentAssistParser;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.Lexer;
import org.eclipse.xtext.web.server.DefaultWebModule;
import com.google.inject.Binder;
import com.google.inject.name.Names;

public class DatatypeWebModule extends DefaultWebModule {

  // contributed by org.eclipse.xtext.xtext.generator.web.WebIntegrationFragment
  public void configureContentAssistLexer(Binder binder) {
      binder.bind(Lexer.class).annotatedWith(Names.named(LexerIdeBindings.CONTENT_ASSIST)).to(InternalDatatypeLexer.class);
  }
  
  // contributed by org.eclipse.xtext.xtext.generator.web.WebIntegrationFragment
  public Class<? extends IContentAssistParser> bindIContentAssistParser() {
      return DatatypeParser.class;
  }
}
