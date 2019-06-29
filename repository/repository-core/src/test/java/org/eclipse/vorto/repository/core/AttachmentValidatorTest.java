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
package org.eclipse.vorto.repository.core;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.utils.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class, AttachmentValidator.class},
    loader = AnnotationConfigContextLoader.class)
public class AttachmentValidatorTest {

  private static int ONE_KB = 1024;

  @Autowired
  private AttachmentValidator attachmentValidator;

  private FileContent file;

  @Before
  public void setUp() {
    file = mock(FileContent.class);
  }

  private static final ModelId MODEL_ID = new ModelId("MyModel", "org.eclipse.vorto", "1.0.0");

  @Test
  public void should_allow_attachment() throws AttachmentException {
    when(file.getFileName()).thenReturn("test.doc");
    when(file.getSize()).thenReturn((long) (2360 * ONE_KB * ONE_KB));
    attachmentValidator.validateAttachment(file, MODEL_ID);
  }

  @Test(expected = AttachmentException.class)
  public void should_not_allow_attachment_exceed_filesize() throws Exception {
    when(file.getFileName()).thenReturn("test.doc");
    when(file.getSize()).thenReturn((long) (5360 * ONE_KB * ONE_KB));
    attachmentValidator.validateAttachment(file, MODEL_ID);
  }

  @Test(expected = AttachmentException.class)
  public void should_not_allow_attachment_illegal_file_type() throws Exception {
    when(file.getFileName()).thenReturn("test.hack");
    when(file.getSize()).thenReturn((long) (2360 * ONE_KB * ONE_KB));
    attachmentValidator.validateAttachment(file, MODEL_ID);
  }

  @Test(expected = AttachmentException.class)
  public void should_size_more_then_100() throws Exception {
    when(file.getFileName()).thenReturn(
        "thisisbigfilename_thisisbigfilename_thisisbigfilename_thisisbigfilename_thisisbigfilename_thisisbigfilename_.txt");
    attachmentValidator.validateAttachment(file, MODEL_ID);
  }


}
