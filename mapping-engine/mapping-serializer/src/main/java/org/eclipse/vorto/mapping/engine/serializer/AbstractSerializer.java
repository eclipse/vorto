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
package org.eclipse.vorto.mapping.engine.serializer;

import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;

public abstract class AbstractSerializer implements IMappingSerializer {

  protected IMappingSpecification specification;
  protected String targetPlatform;

  public AbstractSerializer(IMappingSpecification spec, String targetPlatform) {
    this.specification = spec;
    this.targetPlatform = targetPlatform;
  }
}
