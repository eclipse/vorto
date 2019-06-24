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
package org.eclipse.vorto.repository.core.indexing.impl.basic;

import org.eclipse.vorto.repository.core.indexing.impl.IIndexFieldCreator;
import org.eclipse.vorto.repository.core.indexing.impl.IIndexFieldExtractor;
import org.eclipse.vorto.repository.core.indexing.impl.IIndexFieldSupplier;
import org.springframework.stereotype.Component;

@Component
public class BasicIndexFieldSupplier implements IIndexFieldSupplier {

  public static final String VISIBILITY = "visibility";

  public static final String STATE = "state";

  public static final String DESCRIPTION = "description";

  public static final String DISPLAY_NAME = "displayName";

  public static final String AUTHOR = "author";

  public static final String MODEL_TYPE = "modelType";

  public static final String MODEL_ID = "modelId";
  
  @Override
  public IIndexFieldCreator creator() {
    return new BasicIndexFieldCreator();
  }

  @Override
  public IIndexFieldExtractor extractor() {
    return new BasicIndexFieldExtractor();
  }
}
