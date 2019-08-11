/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package com.acme.solution.service;

import java.nio.charset.StandardCharsets;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AMQP Queue listener for any telemetry data
 *
 */
public class AMQPListener implements MessageListener {
  private static final Logger logger = LoggerFactory.getLogger(AMQPListener.class);

  @Override
  public void onMessage(Message message) {
    try {
      if (message instanceof BytesMessage) {
    	  logger.info("Received AMQP message: "+convertBytesMessageToString((BytesMessage)message));  
      } else {
    	  logger.info("Received AMQP message: "+((TextMessage)message).getText());
      }
      
    } catch (JMSException e) {
      logger.error("Problem receiving AMQP message",e);
    }
    
  }
  
  private static String convertBytesMessageToString(BytesMessage message) throws JMSException {
		byte[] byteData = null;
		byteData = new byte[(int) message.getBodyLength()];
		message.readBytes(byteData);
		message.reset();
		return new String(byteData,StandardCharsets.UTF_8);
	}
}
