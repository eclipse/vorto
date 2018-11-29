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
package org.eclipse.vorto.repository.notification.message;

import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.notification.IMessage;

public abstract class AbstractMessage implements IMessage {

  protected User recipient;

  public AbstractMessage(User recipient) {
    this.recipient = recipient;
  }

  @Override
  public User getRecipient() {
    return recipient;
  }

}
