package org.eclipse.vorto.plugin.generator;

import org.apache.commons.lang3.StringUtils;

public final class TestUtils {  
  
  /**
   * The normalizeEOL method replaces all end-of-line characters to \n in order to compare files consistently across platforms.
   * @param text
   * @return text with all line endings changed to \n
   */
  public static String normalizeEOL(String text) {
    return StringUtils
        .deleteWhitespace(StringUtils.normalizeSpace(text.replaceAll("\\\\r\\\\n", "\\\\n")));
  }
}
