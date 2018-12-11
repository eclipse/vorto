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
package org.eclipse.vorto.mapping.engine.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.jxpath.JXPathContext;

public class DynamicBean {

  private Map<String, Object> properties = new HashMap<String, Object>();

  private JxPathFactory jxpathFactory;

  public DynamicBean(JxPathFactory jxpathFactory) {
    this.jxpathFactory = jxpathFactory;
  }

  public DynamicBean() {
    this(defaultContext());
  }

  private static JxPathFactory defaultContext() {
    JxPathFactory factory = new JxPathFactory();
    factory.setLenient(true);
    return factory;
  }

  public void setProperty(String path, Object value) {
    setProperty(properties, path, value, path);
  }

  @SuppressWarnings("unchecked")
  private void setProperty(Map<String, Object> bean, String currentPath, Object value,
      String path) {
    StringTokenizer tokenizer = new StringTokenizer(currentPath, "/");
    while (tokenizer.hasMoreTokens()) {
      final String pathElement = tokenizer.nextToken();
      if (tokenizer.hasMoreTokens()) {
        Map<String, Object> newBean = null;
        if (isArray(pathElement)) {
          String arrayName = getArrayName(pathElement);
          if (!bean.containsKey(arrayName)) {
            List<Map<String, Object>> array = new ArrayList<>();
            bean.put(arrayName, array);
            newBean = new HashMap<String, Object>();
            array.add(newBean);
          } else {
            Object existingNode = getValue(bean, pathElement);

            if (existingNode != null) {
              newBean = (Map<String, Object>) existingNode;
            } else {
              List<Map<String, Object>> array = (List<Map<String, Object>>) bean.get(arrayName);
              newBean = new HashMap<String, Object>();
              array.add(newBean);
            }
          }

        } else {
          if (bean.containsKey(pathElement)) {
            newBean = (Map<String, Object>) bean.get(pathElement);
          } else {
            newBean = new HashMap<String, Object>();
            bean.put(pathElement, newBean);
          }
        }
        setProperty(newBean, currentPath.substring(currentPath.indexOf("/") + 1), value, path);
        break;
      } else {
        bean.put(pathElement, value);
      }
    }
  }

  private boolean isArray(final String pathElement) {
    return pathElement.endsWith("]");
  }

  private String getArrayName(final String pathElement) {
    return pathElement.substring(0, pathElement.indexOf("["));
  }

  private Object getValue(Object ctx, String path) {
    JXPathContext jxpathCtx = jxpathFactory.newContext(ctx);
    return jxpathCtx.getValue(path);
  }

  public Object getProperty(String name) {
    return properties.get(name);
  }

  public String toString() {
    return properties.toString();
  }

  public Map<String, Object> asMap() {
    return properties;
  }
}
