package org.eclipse.vorto.plugins.generator.lambda.meta.plugins.function;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo;
import org.eclipse.vorto.plugin.utils.ApiGatewayRequest;
import org.eclipse.vorto.plugin.utils.ApiGatewayResponse;
import org.eclipse.vorto.plugins.generator.lambda.meta.plugins.plugins.GeneratorPluginInfoFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorMetaHandler implements RequestStreamHandler {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void handleRequest(InputStream input, OutputStream output, Context context)
      throws IOException {

    ApiGatewayRequest request = ApiGatewayRequest.createFromJson(input);
    
    GeneratorPluginInfo pluginInfo = GeneratorPluginInfoFactory.getInstance().getForPlugin(request.getPathParam("pluginkey"));
    
    if (pluginInfo == null) {
      ApiGatewayResponse response = ApiGatewayResponse.builder().setStatusCode(404).build();
      objectMapper.writeValue(output, response);
    } else {
      ApiGatewayResponse gatewayResponse = ApiGatewayResponse.builder()
          .setStatusCode(200)
          .setRawBody(objectMapper.writeValueAsString(pluginInfo))
          .build();
      
      objectMapper.writeValue(output, gatewayResponse);
    }
   
    
    
  }
}
