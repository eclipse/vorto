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
package org.eclipse.vorto.repository.importer;

import com.google.common.base.Strings;
import java.util.Arrays;

public class StatusMessage {

  private String message;
  private MessageSeverity severity;

  public StatusMessage(String message, MessageSeverity severity) {
    super();
    this.message = message;
    this.severity = severity;
  }

  /**
   * Derives the message from the given {@link Exception} by using its message as main message, but
   * also aggregating messages from suppressed {@link Exception}s is any.
   *
   * @param exception
   * @param severity
   */
  public StatusMessage(Exception exception, MessageSeverity severity) {
    super();
    this.severity = severity;
    if (exception.getSuppressed().length == 0) {
      this.setMessage(exception.getMessage());
    } else {
      String newLine = System.getProperty("line.separator");
      StringBuilder message = new StringBuilder(exception.getMessage());
      message.append(newLine).append("[Details:").append(newLine);
      Arrays.stream(exception.getSuppressed())
          .forEach(
              s -> {
                String suppressedMessage = s.getMessage();
                if (!Strings.isNullOrEmpty(suppressedMessage)) {
                  message.append(suppressedMessage).append("; ").append(newLine);
                }
              }
          );
      // last "; "
      int lastIndexOfSuppressedSeparator = message.lastIndexOf("; ");
      if (lastIndexOfSuppressedSeparator > 0) {
        message.delete(lastIndexOfSuppressedSeparator, message.length());
      }
      message.append("]");
      this.setMessage(message.toString());
    }
  }

  protected StatusMessage() {
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public MessageSeverity getSeverity() {
    return severity;
  }

  public void setSeverity(MessageSeverity severity) {
    this.severity = severity;
  }

  @Override
  public String toString() {
    return "StatusMessage [message=" + message + ", severity=" + severity + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((message == null) ? 0 : message.hashCode());
    result = prime * result + ((severity == null) ? 0 : severity.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    StatusMessage other = (StatusMessage) obj;
    if (message == null) {
      if (other.message != null) {
        return false;
      }
    } else if (!message.equals(other.message)) {
      return false;
    }
    if (severity != other.severity) {
      return false;
    }
    return true;
  }


}
