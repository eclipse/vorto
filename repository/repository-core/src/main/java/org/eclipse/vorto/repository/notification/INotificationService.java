/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.notification;

import org.springframework.scheduling.annotation.Async;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public interface INotificationService {

  /**
   * Sends the message synchronously and blocks until either sent or failed. <br/>
   * This is useful when the caller needs to ensure the message has been sent, or react accordingly
   * otherwise.
   *
   * @param message
   * @see INotificationService#sendNotificationAsync(IMessage) for asynchronous usage instead.
   */
  void sendNotification(IMessage message);

  /**
   * This will send the givne {@link IMessage} in "fire-and-forget" mode. <br/>
   * The advantage is that it will not block and the caller can just move on without caring much
   * about whether sending the message succeeded.<br/>
   * The disadvantage is that exceptions thrown (namely here, the {@link NotificationProblem}
   * wrapper) will not be propagated to the caller.
   *
   * @param message
   * @see INotificationService#sendNotification(IMessage) for synchronous usage instead.
   */
  @Async
  void sendNotificationAsync(IMessage message);

  class NotificationProblem extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotificationProblem(String msg, Throwable t) {
      super(msg, t);
    }
  }
}
