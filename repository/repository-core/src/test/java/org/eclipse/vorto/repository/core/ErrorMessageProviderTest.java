package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import org.eclipse.vorto.repository.core.impl.parser.ErrorMessageProvider;
import org.junit.Test;

public class ErrorMessageProviderTest {

  @Test
  public void testErrorMessageProvider() {
    ErrorMessageProvider provider = new ErrorMessageProvider();
    assertEquals("", provider.convertError(""));
    assertEquals("test", provider.convertError("test"));
    assertEquals("Symbol '-' is not a valid character for property name.",
        provider.convertError("mismatched input '-' expecting 'as'"));
    assertEquals("Malformed property.",
        provider.convertError("extraneous input 'has' expecting 'as'"));
  }

}
