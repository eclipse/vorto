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
package org.eclipse.vorto.codegen.api;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Please use the Plugin SDK API instead
 */
@Deprecated
class EclipseProjectParserHandler extends DefaultHandler {

  private final String[] path;
  private int location = -1;
  private String value;

  EclipseProjectParserHandler(String[] path) {
    this.path = path;
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    if (location < path.length - 1 && path[location + 1].equals(qName)) {
      location++;
    }
  }

  public void characters(char[] ch, int start, int length) throws SAXException {
    if (value == null && location == path.length - 1) {
      value = new String(ch, start, length).trim();
    }
  }

  public String getValue() {
    return value;
  }

  public void reset() {
    value = null;
    location = -1;
  }

}
