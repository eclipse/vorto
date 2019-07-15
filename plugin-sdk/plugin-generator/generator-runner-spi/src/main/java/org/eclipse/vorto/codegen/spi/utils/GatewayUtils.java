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
package org.eclipse.vorto.codegen.spi.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.ServiceClassifier;
import org.eclipse.vorto.codegen.spi.exception.NotFoundException;
import org.eclipse.vorto.codegen.spi.model.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;

public class GatewayUtils {

  private static final String FILE_PROPERTY_PREFIX = "vorto.service.%s";
  private static final String ENV_PROPERTY_PREFIX = "vorto.service.%s.%s";

  private static final String[] PROPERTIES = {"tags", "classifier", "name", "image32x32",
      "image144x144", "documentationUrl", "description", "creator"};

  private static final String VORTO_SERVICE_TAGS =
      String.format(FILE_PROPERTY_PREFIX, PROPERTIES[0]);
  private static final String VORTO_SERVICE_CLASSIFIER =
      String.format(FILE_PROPERTY_PREFIX, PROPERTIES[1]);
  private static final String VORTO_SERVICE_NAME =
      String.format(FILE_PROPERTY_PREFIX, PROPERTIES[2]);
  private static final String VORTO_SERVICE_IMAGE32X32 =
      String.format(FILE_PROPERTY_PREFIX, PROPERTIES[3]);
  private static final String VORTO_SERVICE_IMAGE144X144 =
      String.format(FILE_PROPERTY_PREFIX, PROPERTIES[4]);
  private static final String VORTO_SERVICE_DOCUMENTATION_URL =
      String.format(FILE_PROPERTY_PREFIX, PROPERTIES[5]);
  private static final String VORTO_SERVICE_DESCRIPTION =
      String.format(FILE_PROPERTY_PREFIX, PROPERTIES[6]);
  private static final String VORTO_SERVICE_CREATOR =
      String.format(FILE_PROPERTY_PREFIX, PROPERTIES[7]);

  private static final Logger LOGGER = LoggerFactory.getLogger(GatewayUtils.class);

  public static GeneratorServiceInfo generatorInfoFromFile(String configFile,
      IVortoCodeGenerator generator) {
    Objects.requireNonNull(configFile);
    Objects.requireNonNull(generator);

    Properties properties = getProperties(configFile)
        .orElseThrow(notFound(String.format("ConfigFile [%s]", configFile)));

    GeneratorServiceInfo serviceInfo = new GeneratorServiceInfo();

    serviceInfo.setCreator(properties.getProperty(VORTO_SERVICE_CREATOR));
    serviceInfo.setDescription(properties.getProperty(VORTO_SERVICE_DESCRIPTION));
    serviceInfo.setDocumentationUrl(properties.getProperty(VORTO_SERVICE_DOCUMENTATION_URL));
    serviceInfo.setImage144x144(encodeToBase64(properties.getProperty(VORTO_SERVICE_IMAGE144X144)));
    serviceInfo.setImage32x32(encodeToBase64(properties.getProperty(VORTO_SERVICE_IMAGE32X32)));
    serviceInfo.setKey(generator.getServiceKey());
    serviceInfo.setName(properties.getProperty(VORTO_SERVICE_NAME));
    serviceInfo
        .setClassifier(ServiceClassifier.valueOf(properties.getProperty(VORTO_SERVICE_CLASSIFIER)));
    serviceInfo.setTags(properties.getProperty(VORTO_SERVICE_TAGS, "").split(","));

    return serviceInfo;
  };
  
  private static Optional<Properties> getProperties(String configFile) {
    try {
      Properties properties = new Properties();
      properties.load(new ClassPathResource(configFile).getInputStream());
      return Optional.of(properties);
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  public static String encodeToBase64(String filename) {
    InputStream ioStream = null;
    try {
      ioStream = new ClassPathResource(filename).getInputStream();
    } catch (IOException e) {
      try {
        ioStream = new FileSystemResource(filename).getInputStream();
      } catch (IOException e2) {
        try {
          ioStream = new ClassPathResource(filename.contains("32")?"img/icon32x32.png" : "img/icon144x144.png").getInputStream();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    }

    try {
      return Base64Utils.encodeToString(StreamUtils.copyToByteArray(ioStream));
    } catch (IOException ex) {
      LOGGER.error("Could not encode image " + filename, ex);
      return null;
    }
  }

  public static Map<String, String> mapFromRequest(final HttpServletRequest request) {
    Map<String, String> requestParams = new HashMap<>();
    request.getParameterMap().entrySet().stream()
        .forEach(x -> requestParams.put(x.getKey(), x.getValue()[0]));
    return requestParams;
  }

  public static Supplier<NotFoundException> notFound(String subject) {
    return () -> new NotFoundException(subject + " Not Found.");
  }

  public static Consumer<Generator> checkEnvModifications(Environment env) {
    return gen -> {
      Stream.of(PROPERTIES).forEach(property -> {
        String propertyKey = gen.getInstance().getServiceKey();
        String modProperty = String.format(ENV_PROPERTY_PREFIX, propertyKey, property);
        if (env.containsProperty(modProperty)) {
          String modValue = env.getProperty(modProperty);
          LOGGER
              .info(String.format("Overriding %s for %s with %s", property, propertyKey, modValue));
          if (PROPERTIES[0].equals(property)) {
            gen.getInfo().setTags(modValue.split(","));
          } else if (PROPERTIES[1].equals(property)) {
            gen.getInfo().setClassifier(ServiceClassifier.valueOf(modValue));
          } else if (PROPERTIES[2].equals(property)) {
            gen.getInfo().setName(modValue);
          } else if (PROPERTIES[3].equals(property)) {
            gen.getInfo().setImage32x32(encodeToBase64(modValue));
          } else if (PROPERTIES[4].equals(property)) {
            gen.getInfo().setImage144x144(encodeToBase64(modValue));
          } else if (PROPERTIES[5].equals(property)) {
            gen.getInfo().setDocumentationUrl(modValue);
          } else if (PROPERTIES[6].equals(property)) {
            gen.getInfo().setDescription(modValue);
          } else if (PROPERTIES[7].equals(property)) {
            gen.getInfo().setCreator(modValue);
          }
        }
      });
    };
  }
}
