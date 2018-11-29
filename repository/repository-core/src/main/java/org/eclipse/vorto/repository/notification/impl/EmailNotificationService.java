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
package org.eclipse.vorto.repository.notification.impl;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.notification.IMessage;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.sun.mail.smtp.SMTPMessage;

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
  private String needsAuth = "false";

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

    try {
      final Session emailSession = newSession();
      Transport transport = emailSession.getTransport("smtp");
      try {
        transport.connect();
      } catch (Exception connectEx) {
        logger.error("Problem connecting to Email server", connectEx);
        return;
      }
      SMTPMessage smtpMessage = new SMTPMessage(emailSession);
      smtpMessage.setFrom(new InternetAddress(this.mailFrom));
      smtpMessage.setRecipients(Message.RecipientType.TO,
          InternetAddress.parse(message.getRecipient().getEmailAddress()));
      smtpMessage.setHeader("Content-Type", "text/html");
      smtpMessage.setSubject(message.getSubject());
      smtpMessage.setContent(message.getContent(), "text/html");
      smtpMessage.setNotifyOptions(SMTPMessage.NOTIFY_SUCCESS);
      smtpMessage.setReturnOption(1);
      transport.sendMessage(smtpMessage,
          InternetAddress.parse(message.getRecipient().getEmailAddress()));
      transport.close();
    } catch (MessagingException me) {
      logger.error(me.getMessage(), me);
      throw new NotificationProblem("Problem sending email", me);
    }

  }

  private Session newSession() {
    Properties props = new Properties();
    props.setProperty("mail.smtp.host", this.smtpHost);
    props.setProperty("mail.smtp.port", this.smtpPort);
    final String mailUser = this.mailUser;
    final String mailPassword = this.mailPassword;

    return Session.getDefaultInstance(props,
        this.needsAuth.equalsIgnoreCase("false") ? null : new javax.mail.Authenticator() {
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(mailUser, mailPassword);
          }
        });
  }

  public String getMailFrom() {
    return mailFrom;
  }

  public void setMailFrom(String mailFrom) {
    this.mailFrom = mailFrom;
  }

  public String getSmtpHost() {
    return smtpHost;
  }

  public void setSmtpHost(String smtpHost) {
    this.smtpHost = smtpHost;
  }

  public String getSmtpPort() {
    return smtpPort;
  }

  public void setSmtpPort(String smtpPort) {
    this.smtpPort = smtpPort;
  }

  public String getNeedsAuth() {
    return needsAuth;
  }

  public void setNeedsAuth(String needsAuth) {
    this.needsAuth = needsAuth;
  }

}
