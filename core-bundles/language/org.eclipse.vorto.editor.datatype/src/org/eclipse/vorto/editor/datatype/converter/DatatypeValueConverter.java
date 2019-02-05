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
package org.eclipse.vorto.editor.datatype.converter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.util.Strings;

public class DatatypeValueConverter extends DefaultTerminalConverters {
  @ValueConverter(rule = "EnumLiteralName")
  public IValueConverter<String> EnumLiteralName() {
    return new IValueConverter<String>() {
      public String toValue(String string, INode node) throws ValueConverterException {
        String[] namespaces = string.split("\\.");
        if (namespaces.length == 2) {
          Map<String, String> importedModules = getImportedModules(node.getRootNode());
          String fullName = importedModules.get(namespaces[0]) + "." + string;
          return fullName;
        } else {
          return string;
        }
      }

      public String toString(String value) throws ValueConverterException {
        String[] namespaces = value.split("\\.");
        if (namespaces.length > 1) {
          return namespaces[namespaces.length - 2] + "." + namespaces[namespaces.length - 1];
        }
        return value;
      }

      public Map<String, String> getImportedModules(ICompositeNode rootNode) {
        Map<String, String> importedModules = new HashMap<String, String>();

        rootNode.getAsTreeIterable().forEach(new Csmr(importedModules));

        return importedModules;
      }

      class Csmr implements Consumer<INode> {
        private Map<String, String> importedModules = new HashMap<String, String>();

        public Csmr(Map<String, String> importedModules) {
          this.importedModules = importedModules;
        }

        public void accept(INode t) {
          String textWithoutWhitespace = Strings.removeLeadingWhitespace(t.getText());
          if (textWithoutWhitespace != null && textWithoutWhitespace.startsWith("using")) {
            String[] components = textWithoutWhitespace.split("\\s+");
            if (components.length > 1) {
              String[] namespace = components[1].split("\\.");
              importedModules.put(namespace[namespace.length - 1],
                  String.join(".", Arrays.copyOfRange(namespace, 0, namespace.length - 1)));
            }
          }
        }
      }
    };
  }


}
