package org.eclipse.vorto.repository.core.indexing.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.indexing.IIndexingService;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

@Component
public class IndexingService implements IIndexingService {

  private static final String VORTO_INDEX = "vorto";

  private static Logger logger = Logger.getLogger(IndexingService.class);
  
  private RestHighLevelClient client;
  
  @PostConstruct
  public void init() {
    client = new RestHighLevelClient(
        RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")));
  }
  
  @PreDestroy
  public void deinit() {
    try {
      client.close();
    } catch (IOException e) {
      logger.error("Not able to close indexing client", e);
    }
  }
  
  @Override
  public void indexModel(ModelInfo modelInfo, IUserContext userContext) {
    logger.info("Indexing model '" + modelInfo.getId() + "'");
    
    Map<String, Object> jsonMap = new HashMap<>();
    jsonMap.put("tenantId", userContext.getTenant());
    jsonMap.put("modelId", modelInfo.getId().getPrettyFormat());
    jsonMap.put("author", modelInfo.getAuthor());
    jsonMap.put("description", modelInfo.getDescription());
    jsonMap.put("state", modelInfo.getState());
    jsonMap.put("visibility", "private");
    
    IndexRequest request = new IndexRequest(VORTO_INDEX, "doc", modelInfo.getId().getPrettyFormat())
        .source(jsonMap);
        
    try {
      IndexResponse indexResponse = client.index(request);
      if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
        logger.info("Index created for '" + modelInfo.getId().getPrettyFormat() + "'");
      }
    } catch (IOException e) {
      logger.error("Error while indexing '" + modelInfo.getId().getPrettyFormat() + "'", e);
    }
  }

  @Override
  public void updateIndex(ModelInfo modelInfo, IUserContext userContext) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteIndex(ModelId modelId, IUserContext userContext) {
    // TODO Auto-generated method stub

  }

}
