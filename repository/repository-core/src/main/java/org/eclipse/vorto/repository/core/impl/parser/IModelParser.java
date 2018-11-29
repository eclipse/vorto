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
}
