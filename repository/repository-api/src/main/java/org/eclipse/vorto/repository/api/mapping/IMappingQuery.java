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
package org.eclipse.vorto.repository.api.mapping;

import java.util.List;
import org.eclipse.vorto.repository.api.content.IMappedElement;


@Deprecated
public interface IMappingQuery<Result extends IMappedElement> {

  /**
   * adds the mapping stereotype criteria to the query
   * 
   * @param name
   * @return
   */
  IMappingQuery<Result> stereotype(String name);

  /**
   * adds the mapping attribute criteria to the query
   * 
   * @param key
   * @param value
   * @return
   */
  IMappingQuery<Result> attribute(String key, String value);

  /**
   * Executes the query
   * 
   * @return
   */
  List<Result> list();

}
