package org.eclipse.vorto.repository.core.impl.utils;

import java.util.Iterator;
import java.util.StringTokenizer;
import org.eclipse.vorto.model.ModelId;

public class ModelIdHelper {

  private ModelId modelId;

  private static final String PATH_DELIMITER = "/";

  public ModelIdHelper(ModelId modelId) {
    this.modelId = modelId;
  }

  public String getFullPath() {
    StringBuilder path = new StringBuilder(getNamespacePath());
    path.append(PATH_DELIMITER);
    path.append(modelId.getName());
    path.append(PATH_DELIMITER);
    path.append(modelId.getVersion());
    return path.toString();
  }

  public String getNamespacePath() {
    StringBuilder path = new StringBuilder(PATH_DELIMITER);
    StringTokenizer tokenizer = new StringTokenizer(modelId.getNamespace(), ".");
    while (tokenizer.hasMoreTokens()) {
      path.append(tokenizer.nextToken());
      if (tokenizer.hasMoreTokens()) {
        path.append(PATH_DELIMITER);
      }
    }
    return path.toString();
  }

  public String getFileName(final String path) {
    return path.substring(path.lastIndexOf("/") + 1);
  }

  /**
   * Example path: /org/eclipse/vorto/color/1.0.0/
   * 
   * @param path
   * @return
   */
  public static ModelId fromPath(String path) {
    String[] pathFragments = path.substring(1).split("/");
    return new ModelId(pathFragments[pathFragments.length - 2], convertToNamespace(pathFragments),
        pathFragments[pathFragments.length - 1]);
  }

  private static String convertToNamespace(String[] fragments) {
    StringBuilder namespaceBuilder = new StringBuilder();
    for (int i = 0; i < fragments.length - 2; i++) {
      namespaceBuilder.append(fragments[i]);
      if (i < fragments.length - 3) {
        namespaceBuilder.append(".");
      }
    }
    return namespaceBuilder.toString();
  }

  public Iterator<String> iterator() {
    return new StringTokenizerIterator(getFullPath());
  }

  private static class StringTokenizerIterator implements Iterator<String> {

    private StringTokenizer enumeration;

    public StringTokenizerIterator(String fullPath) {
      this.enumeration = new StringTokenizer(fullPath.substring(1), "/");
    }

    public boolean hasNext() {
      return this.enumeration.hasMoreElements();
    }

    public String next() {
      return this.enumeration.nextToken();
    }

    public void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }
  }

}
