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
package org.eclipse.vorto.repository.core.impl.parser;

import java.io.InputStream;
import java.util.Collection;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.ModelInfo;


/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public interface IModelParser {

  /**
   * builds a model resource from the given inputstream
   * 
   * @param is the actual content containing model related meta data
   * @return
   */
  ModelInfo parse(InputStream is);

  /**
   * Sets the files that needs to be referenced by the file being parsed (i.e its dependencies)
   * 
   * @param fileReferences
   */
  void setReferences(Collection<FileContent> fileReferences);

  /**
   * instructs the parser to perform validation or not during parsing. Default is 'enabled' = true
   * @param validate
   */
  void setValidate(boolean enable);
}
