/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.core.impl.parser;

public class ParsingException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -7547879180831064180L;

  public ParsingException(String arg0) {
    super(arg0);
  }

  public ParsingException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

}
