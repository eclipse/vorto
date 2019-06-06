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
package org.eclipse.vorto.codegen.spi.config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.eclipse.vorto.codegen.spi.model.Generator;
import org.eclipse.vorto.codegen.spi.repository.GeneratorRepository;
import org.eclipse.vorto.codegen.spi.utils.GatewayUtils;
import org.eclipse.vorto.model.BooleanAttributeProperty;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.EnumAttributeProperty;
import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.IModel;
import org.eclipse.vorto.model.IPropertyAttribute;
import org.eclipse.vorto.model.IReferenceType;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.repository.client.IRepositoryClient;
import org.eclipse.vorto.repository.client.RepositoryClientBuilder;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;

public abstract class AbstractGeneratorConfiguration
    implements ApplicationRunner, EnvironmentAware, IGeneratorConfiguration {

  private static final String LOCALHOST = "localhost";
  private static final Logger LOGGER =
      LoggerFactory.getLogger(AbstractGeneratorConfiguration.class);

  @Autowired
  private Environment env;

  @Autowired
  private GeneratorRepository generatorRepo;

  @PostConstruct
  public void init() {
    ModelWorkspaceReader.init();
    ignoreHostnameVerification();
    ignoreSSLCertificateVerification();
  }

  public void ignoreHostnameVerification() {
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
      public boolean verify(String hostname, SSLSession sslSession) {
        if (hostname.equals(LOCALHOST)) {
          return true;
        }
        return false;
      }
    });
  }

  public void ignoreSSLCertificateVerification() {
    SSLContext ctx = null;
    TrustManager[] trustAllCerts = new X509TrustManager[] {new X509TrustManager() {
      public X509Certificate[] getAcceptedIssuers() {
        return null;
      }

      public void checkClientTrusted(X509Certificate[] certs, String authType) {
        // Do nothing
      }

      public void checkServerTrusted(X509Certificate[] certs, String authType) {
        // Do nothing
      }
    }};

    try {
      ctx = SSLContext.getInstance("SSL");
      ctx.init(null, trustAllCerts, null);
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      LOGGER.error("Error in ignoring SSL Ceritificate Verification", e);
    }

    SSLContext.setDefault(ctx);
  }

  public String getVortoRepoUrl() {
    String vortoRepoUrl = System.getenv("VORTO_REPO_URL");
    if (!Strings.nullToEmpty(vortoRepoUrl).trim().isEmpty()) {
      return vortoRepoUrl;
    }

    String vortoServerUrl = env.getProperty("vorto.serverUrl");
    if (!Strings.nullToEmpty(vortoServerUrl).trim().isEmpty()) {
      return vortoServerUrl;
    }

    throw GatewayUtils.notFound("Not able to get the Vorto Server URL both from the "
        + "Environment variable VORTO_REPO_URL or the config setting 'vorto.serverUrl'").get();
  }

  public String getAppServiceUrl() {
    String serviceoUrl = System.getenv("APP_SERVICE_URL");
    if (!Strings.nullToEmpty(serviceoUrl).trim().isEmpty()) {
      return serviceoUrl;
    }

    String applicationServiceUrl = env.getProperty("server.serviceUrl");
    if (!Strings.nullToEmpty(applicationServiceUrl).trim().isEmpty()) {
      return applicationServiceUrl;
    }

    throw GatewayUtils
        .notFound("Not able to get the Application Service URL both from the "
            + "Environment variable APP_SERVICE_URL or the config setting 'server.serviceUrl'")
        .get();
  }

  @Override
  public void setEnvironment(Environment env) {
    this.env = env;
  }

  protected abstract void doSetup();

  @Override
  public void run(ApplicationArguments args) throws Exception {

    try {
      doSetup();
      generatorRepo.list().stream().forEach(GatewayUtils.checkEnvModifications(env));

    } catch (RuntimeException e) {
      LOGGER.error("Error registering generators", e);
    }
  }

  protected void addGenerator(Generator generator) {
    generatorRepo.add(generator);
  }


  @Bean
  public IRepositoryClient modelRepository() {
    return RepositoryClientBuilder.newBuilder().setBaseUrl(getVortoRepoUrl()).build();
  }

  @Bean
  public Jackson2ObjectMapperBuilder objectMapperBuilder() {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

    builder.deserializerByType(IReferenceType.class, new JsonDeserializer<IReferenceType>() {

      @Override
      public IReferenceType deserialize(JsonParser parser, DeserializationContext context)
          throws IOException, JsonProcessingException {
        try {
          return parser.readValueAs(ModelId.class);
        } catch (IOException ioEx) {
          try {
            return parser.readValueAs(PrimitiveType.class);
          } catch (IOException ex) {
            ex.printStackTrace();
            return null;
          }
        }
      }

    });
    builder.deserializerByType(IPropertyAttribute.class,
        new JsonDeserializer<IPropertyAttribute>() {

          @Override
          public IPropertyAttribute deserialize(JsonParser parser, DeserializationContext context)
              throws IOException, JsonProcessingException {
            try {
              return parser.readValueAs(BooleanAttributeProperty.class);
            } catch (IOException ioEx) {
              try {
                return parser.readValueAs(EnumAttributeProperty.class);
              } catch (IOException ex) {
                ex.printStackTrace();
                return null;
              }
            }
          }

        });

    builder.deserializerByType(HashMap.class, new JsonDeserializer<HashMap<ModelId, IModel>>() {

      @Override
      public HashMap<ModelId, IModel> deserialize(JsonParser parser, DeserializationContext context)
          throws IOException, JsonProcessingException {
        try {
          HashMap<ModelId, IModel> deserialized = new HashMap<>();
          ObjectCodec oc = parser.getCodec();
          JsonNode node = oc.readTree(parser);
          
          Iterator<JsonNode> iterator = node.elements();
          while (iterator.hasNext()) {
            JsonNode childNode = iterator.next();
            JsonNode type = childNode.get("type");
            IModel value = null;
            if (ModelType.valueOf(type.asText()).equals(ModelType.InformationModel)) {
              value = parser.readValueAs(Infomodel.class);
            } else if (ModelType.valueOf(type.asText()).equals(ModelType.Functionblock)) {
              value = parser.readValueAs(FunctionblockModel.class);
            } else if (ModelType.valueOf(type.asText()).equals(ModelType.Datatype) && node.has("literals")) {
              value = parser.readValueAs(EnumModel.class);
            } else {
              value = parser.readValueAs(EntityModel.class);
            }
            
            if (value != null) {
              deserialized.put(getModelId(childNode.get("id").get("prettyFormat").asText()), value);
          }
          }
          
          return deserialized;
        } catch (IOException ioEx) {
          throw new RuntimeException(ioEx);
        }
      }
      
      private ModelId getModelId(String modelId) {
        try {
            return ModelId.fromPrettyFormat(modelId);
        } catch(IllegalArgumentException ex) {
            final int versionIndex = modelId.indexOf(":");
            return ModelId.fromReference(modelId.substring(0,versionIndex),modelId.substring(versionIndex+1));
        }
    }

    });

    return builder;
  }

}
