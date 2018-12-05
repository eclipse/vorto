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
package org.eclipse.vorto.repository.backup;

public interface IModelBackupService {

  /**
   * creates a complete backup of the repository content
   * 
   * @return
   * @throws Exception
   */
  byte[] backup() throws Exception;

  /**
   * restores the given backup. Previous data will be lost!!
   * 
   * @param inputStream
   * @throws Exception
   */
  void restore(byte[] backup) throws Exception;

}
