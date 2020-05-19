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
package org.eclipse.vorto.repository.notification.impl;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.notification.IMessage;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service
public class EmailNotificationService implements INotificationService {

  private static Logger logger = Logger.getLogger(EmailNotificationService.class);

  @Value("${mail.from}")
  private String mailFrom;

  @Value("${mail.smtp.host}")
  private String smtpHost;

  @Value("${mail.smtp.port}")
  private String smtpPort;

  @Value("${mail.smtp.auth}")
  private String needsAuth = "true";

  @Value("${mail.login.username}")
  private String mailUser;

  @Value("${mail.login.password}")
  private String mailPassword;

  public void sendNotification(IMessage message) {
    doSendEmail(message);
  }

  private void doSendEmail(IMessage message) {

    if (!message.getRecipient().hasEmailAddress()) {
      return;
    }
    Transport transport = null;
    try {
      final Session emailSession = newSession();
      transport = emailSession.getTransport();
      if (Boolean.valueOf(needsAuth)) {
        transport.connect(smtpHost, mailUser, mailPassword);
      } else {
        transport.connect();
      }
      MimeMessage mimeMessage = new MimeMessage(emailSession);
      mimeMessage.setFrom(new InternetAddress(this.mailFrom));
      mimeMessage.setRecipients(Message.RecipientType.TO,
          InternetAddress.parse(message.getRecipient().getEmailAddress()));
      mimeMessage.setHeader("Content-Type", "text/html");
      mimeMessage.setSubject(message.getSubject());
      mimeMessage.setContent(message.getContent(), "text/html");
      transport.sendMessage(mimeMessage,
          InternetAddress.parse(message.getRecipient().getEmailAddress()));
      transport.close();
    } catch (MessagingException me) {
      logger.error(me.getMessage(), me);
      throw new NotificationProblem("Problem sending email", me);
    } finally {
      if (transport != null) {
        try {
          transport.close();
        } catch (MessagingException mme) {
          logger.error(mme.getMessage(), mme);
        }
      }
    }

  }

  private Session newSession() {
    Properties props = new Properties();
    props.setProperty("mail.smtp.host", this.smtpHost);
    props.setProperty("mail.smtp.port", this.smtpPort);
    props.setProperty("mail.transport.protocol", "smtp");
    if (Boolean.valueOf(needsAuth)) {
      props.setProperty("mail.smtp.starttls.enable", "true");
    }
    props.setProperty("mail.smtp.auth", this.needsAuth);

    return Session.getDefaultInstance(props);
  }

}
