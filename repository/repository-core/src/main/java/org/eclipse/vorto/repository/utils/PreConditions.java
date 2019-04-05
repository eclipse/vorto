package org.eclipse.vorto.repository.utils;

import java.util.Collection;
import com.google.common.base.Strings;

public class PreConditions {
  
  public static void notNullOrEmpty(String item, String name) {
    if (Strings.nullToEmpty(item).trim().isEmpty()) {
      throw new IllegalArgumentException(name + " shouldn't be null or empty"); 
    }
  }
  
  public static void notNull(Object o, String message) {
    if (o == null) {
      throw new IllegalArgumentException(message); 
    }
  }
  
  public static <K> void notNullOrEmpty(K[] k, String message) {
    if (k == null || k.length < 1) {
      throw new IllegalArgumentException(message); 
    }
  }
  
  public static <K> void notNullOrEmpty(Collection<K> k, String message) {
    if (k == null || k.size() < 1) {
      throw new IllegalArgumentException(message); 
    }
  }
}
