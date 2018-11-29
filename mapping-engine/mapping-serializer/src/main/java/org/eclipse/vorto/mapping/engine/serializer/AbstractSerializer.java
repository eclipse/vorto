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
