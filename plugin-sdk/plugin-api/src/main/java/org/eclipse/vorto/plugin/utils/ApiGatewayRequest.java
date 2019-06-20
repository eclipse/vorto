package org.eclipse.vorto.plugin.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents an AWS Multipart/form-data Request and parses its content
 *
 */
public class ApiGatewayRequest {

  private static final String IS_BASE64_ENCODED = "isBase64Encoded";

  private static final String PATH_PARAMETERS = "pathParameters";

  private static final String QUERY_STRING_PARAMETERS = "queryStringParameters";

  private static final String AWS_REQUEST_BODY = "body";

  private static final ObjectMapper mapper = new ObjectMapper();

  private static Logger logger = Logger.getLogger(ApiGatewayRequest.class);

  public static ApiGatewayRequest createFromJson(InputStream jsonInput)
      throws JsonProcessingException, IOException {
    return new ApiGatewayRequest(jsonInput);
  }

  private JsonNode requestNode = null;

  private ApiGatewayRequest(InputStream request) throws JsonProcessingException, IOException {
    requestNode = mapper.readTree(request);
    logger.debug("Incoming request: " + requestNode.toString());
  }

  private byte[] decodeInput() {
    return Base64.getDecoder().decode(requestNode.get(AWS_REQUEST_BODY).asText().getBytes());
  }

  private byte[] getBoundry() {
    String[] boundaryArray = getContentType().split("=");
    if (boundaryArray.length > 1) {
      return boundaryArray[1].getBytes();
    }
    logger.error("AWSRequest, boundary missing");
    return null;
  }

  private String getContentType() {
    return requestNode.get("headers").get("Content-Type").asText();
  }

  public byte[] getInput() throws IOException {
    if (isMultipartRequest()) {
      return getMultiPartInput();
    } else {
      return getPlainInput();
    }
  }

  /**
   * Gets the actual request input
   * 
   * @param inputStream
   * @return contents of file as byte array
   * @throws JsonProcessingException
   * @throws IOException
   */
  @SuppressWarnings("deprecation")
  private byte[] getMultiPartInput() throws IOException {

    byte[] bI = decodeInput();

    logger.debug(new String(bI, StandardCharsets.UTF_8) + "\n");

    ByteArrayInputStream content = new ByteArrayInputStream(bI);

    MultipartStream multipartStream = new MultipartStream(content, getBoundry(), bI.length);

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    // Find first boundary in the MultipartStream
    boolean nextPart = multipartStream.skipPreamble();

    // Loop through each segment
    while (nextPart) {
      String header = multipartStream.readHeaders();

      // Log header for debugging
      logger.debug("Headers:");
      logger.debug(header);

      multipartStream.readBodyData(out);

      nextPart = multipartStream.readBoundary();
    }

    return out.toByteArray();
  }

  public String getPathParam(String key) {
    return requestNode.get(PATH_PARAMETERS).get(key).asText();
  }

  public Map<String, String> getQueryParams() {
    Map<String, String> params = new HashMap<>();
    if (requestNode.get(QUERY_STRING_PARAMETERS) != null) {
      Iterator<String> iter = requestNode.get(QUERY_STRING_PARAMETERS).fieldNames();
      while (iter.hasNext()) {
        String fieldName = iter.next();
        params.put(fieldName, requestNode.get(QUERY_STRING_PARAMETERS).get(fieldName).asText());
      }
    }
    return Collections.unmodifiableMap(params);
  }

  private byte[] getPlainInput() {
    if (requestNode.get(IS_BASE64_ENCODED).asBoolean() == true) {
      return decodeInput();
    } else {
      return requestNode.get(AWS_REQUEST_BODY).asText().getBytes();
    }
  }

  public String getQueryParam(String key) {
    return requestNode.get(QUERY_STRING_PARAMETERS).get(key).asText();
  }

  private boolean isMultipartRequest() {
    if (this.requestNode.get("headers").has("Content-Type") && this.requestNode.get("headers").get("Content-Type").asText().contains("multipart/form-data")) {
      return true;
    }
    return false;
  }

}

