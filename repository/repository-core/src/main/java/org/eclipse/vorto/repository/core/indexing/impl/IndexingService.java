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
package org.eclipse.vorto.repository.core.indexing.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.indexing.IIndexingService;
import org.eclipse.vorto.repository.core.indexing.IndexedModelInfo;
import org.eclipse.vorto.repository.core.indexing.IndexingException;
import org.eclipse.vorto.repository.core.indexing.IndexingResult;
import org.eclipse.vorto.repository.utils.PreConditions;
import org.eclipse.vorto.repository.workflow.ModelState;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.common.base.Strings;

@Component
public class IndexingService implements IIndexingService {

  public static final String TEXT = "text";

  public static final String KEYWORD = "keyword";
  
  private static final String MODEL_TYPE = "modelType";

  private static final String DISPLAY_NAME = "displayName";

  private static final String DESCRIPTION = "description";

  private static final String AUTHOR = "author";

  private static final String STATE = "state";

  private static final String MODEL_ID = "modelId";

  private static final String TENANT_ID = "tenantId";

  private static final String VISIBILITY = "visibility";

  private static final int MAX_SEARCH_RESULTS = 1000;

  private static final String PUBLIC = "public";

  private static final String DOC = "_doc";

  private static final String VORTO_INDEX = "vorto";

  private static Logger logger = Logger.getLogger(IndexingService.class);
  
  private Collection<IIndexFieldSupplier> fieldSuppliers;
  
  private Pattern searchExprPattern = Pattern.compile("name:(\\w+)\\*");
  
  private Pattern authorExprPattern = Pattern.compile("author:(\\w+)");
  
  private RestHighLevelClient client;
  
  public IndexingService(@Autowired RestHighLevelClient client,
      @Autowired Collection<IIndexFieldSupplier> fieldSuppliers) {
    this.client = client;
    this.fieldSuppliers = fieldSuppliers;
  }

  @PostConstruct
  public void init() {
    try {
      createIndexIfNotExisting();
    } catch (IndexingException e) {
      logger.error("Cannot create index", e);
    }
  }
  
  private void createIndexIfNotExisting() {
    logger.info("Checking index.");
    if (!indexExist(VORTO_INDEX)) {
      logger.info("Index doesn't exist. Try creating it.");
      createIndexWithMapping(VORTO_INDEX, createMappingForIndex());
      logger.info("Index '" + VORTO_INDEX + "' created.");
    } else {
      logger.info("Index already exist");
    }
  }
  
  private boolean indexExist(String index) {
    GetIndexRequest request = new GetIndexRequest(index);
    try {
      return client.indices().exists(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new IndexingException("Error while checking if index '" + index + "' exist.", e);
    }
  }
  
  private boolean createIndexWithMapping(String index, Map<String, Object> mapping) {
    CreateIndexRequest request = new CreateIndexRequest(index);
    request.mapping(mapping); 
    try {
      CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
      return createIndexResponse.isAcknowledged();
    } catch (IOException e) {
      throw new IndexingException("Error while creating index '" + index + "'.", e);
    }
  }
  
  private Map<String, Object> createMappingForIndex() {
    Map<String, Object> properties = new HashMap<>();
    properties.put(TENANT_ID, createPropertyWithType(KEYWORD));
    
    for(IIndexFieldSupplier supplier : fieldSuppliers) {
      IIndexFieldCreator creator = supplier.creator();
      creator.getFields().forEach((key, value) -> {
        properties.put(key, createPropertyWithType(value));
      });
    }
    
    Map<String, Object> mapping = new HashMap<>();
    mapping.put("properties", properties);
    return mapping;
  }
  
  private Object createPropertyWithType(String type) {
    Map<String, Object> property = new HashMap<>();
    property.put("type", type);
    return property;
  }
  
  @Override
  public IndexingResult reindexAllModels(Map<String, List<ModelInfo>> models) {
    PreConditions.notNull(models, "Models must not be null.");
    
    IndexingResult result = new IndexingResult();
    
    // (1) Delete all models in the index
    deleteAllModels(VORTO_INDEX);
    
    // (2) Index all models
    models.forEach((tenantId, modelInfos) -> {
      if (!modelInfos.isEmpty()) {
        BulkRequest bulkRequest = new BulkRequest();
        logger.info("Trying to bulk index all models in '" + tenantId + "'");
        modelInfos.forEach(modelInfo -> {
          bulkRequest.add(createIndexRequest(modelInfo, tenantId));
        });
        
        try {
          BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
          result.addIndexedTenant(tenantId, modelInfos.size());
          logger.info("Received " + bulkResponse.getItems().length + " replies for tenant '" + tenantId + "' with " 
              + modelInfos.size() + " models" );
        } catch (IOException e) {
          throw new IndexingException("Error trying to index all models in '" + tenantId + "' tenant.", e);
        }
      }
    });
    
    return result;
  }

  private void deleteAllModels(String index) {
    deleteByQuery(index, QueryBuilders.matchAllQuery());
  }
  
  private void deleteByQuery(String index, QueryBuilder query) {
    logger.info("Trying to delete all models in index '" + index + "'");
    DeleteByQueryRequest request = new DeleteByQueryRequest(index);
    request.setQuery(query);
    try {
      BulkByScrollResponse bulkResponse =
          client.deleteByQuery(request, RequestOptions.DEFAULT);
      logger.info("Deleted " + bulkResponse.getTotal() + " models in the index '" + index+ "'");
    } catch (IOException e) {
      throw new IndexingException("Error deleting all models in the '" + index + "' index.", e);
    }
  }

  @SuppressWarnings("unused")
  private boolean modelIndexExist(ModelId modelId) {
    PreConditions.notNull(modelId, "modelId must not be null.");
    
    GetRequest getRequest = new GetRequest(VORTO_INDEX, DOC, modelId.getPrettyFormat());    
    getRequest.fetchSourceContext(new FetchSourceContext(false)); 
    getRequest.storedFields("_none_");
    
    try {
      return client.exists(getRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new IndexingException("Error while querying if model '" + modelId.getPrettyFormat() + "' exist.", e);
    }
  }

  @Override
  public void indexModel(ModelInfo modelInfo, String tenantId) {
    PreConditions.notNull(modelInfo, "modelInfo must not be null.");
    PreConditions.notNullOrEmpty(tenantId, TENANT_ID);
    
    logger.info("Indexing model '" + modelInfo.getId() + "'");
    
    try {
      IndexResponse indexResponse = client.index(createIndexRequest(modelInfo, tenantId), RequestOptions.DEFAULT);
      if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
        logger.info("Index created for '" + modelInfo.getId().getPrettyFormat() + "'");
      }
    } catch (IOException e) {
      throw new IndexingException("Error while indexing '" + modelInfo.getId().getPrettyFormat() + "'", e);
    }
  }
  
  private IndexRequest createIndexRequest(ModelInfo modelInfo, String tenantId) {
    Map<String, Object> jsonMap = new HashMap<>();
    jsonMap.put(TENANT_ID, tenantId);
    
    for(IIndexFieldSupplier supplier : fieldSuppliers) {
      IIndexFieldExtractor extractor = supplier.extractor();
      extractor.extractFields(modelInfo).forEach((key, value) -> {
        jsonMap.put(key, value);
      });
    }
    
    return new IndexRequest(VORTO_INDEX, DOC, modelInfo.getId().getPrettyFormat())
        .source(jsonMap);
  }

  @Override
  public void updateIndex(ModelInfo modelInfo) {
    PreConditions.notNull(modelInfo, "modelInfo must not be null.");
    
    logger.info("Updating index of model '" + modelInfo.getId() + "'");
    
    UpdateRequest request = new UpdateRequest(VORTO_INDEX, DOC, modelInfo.getId().getPrettyFormat())
        .doc(updateMap(modelInfo));
        
    try {
      UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
      if (response.getResult() == DocWriteResponse.Result.UPDATED) {
        logger.info("Index updated for '" + modelInfo.getId().getPrettyFormat() + "'");
      }
    } catch (IOException e) {
      throw new IndexingException("Error while updating the index of '" + modelInfo.getId().getPrettyFormat() + "'", e);
    }
  }

  private Map<String, Object> updateMap(ModelInfo modelInfo) {
    Map<String, Object> jsonMap = new HashMap<>();
    for(IIndexFieldSupplier supplier : fieldSuppliers) {
      IIndexFieldExtractor extractor = supplier.extractor();
      extractor.extractFields(modelInfo).forEach((key, value) -> {
        jsonMap.put(key, value);
      });
    }
    return jsonMap;
  }

  @Override
  public void deleteIndex(ModelId modelId) {
    PreConditions.notNull(modelId, "modelId must not be null.");
    
    DeleteRequest request = new DeleteRequest(VORTO_INDEX, DOC, modelId.getPrettyFormat());
    try {
      DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
      if (response.getResult() == DocWriteResponse.Result.DELETED) {
        logger.info("Index deleted for '" + modelId.getPrettyFormat() + "'");
      }
    } catch (IOException e) {
      throw new IndexingException("Error while deleting the index of '" + modelId.getPrettyFormat() + "'", e);
    }
  }
  
  @Override
  public void deleteIndexForTenant(String tenantId) {
    deleteByQuery(VORTO_INDEX, QueryBuilders.termQuery(TENANT_ID, tenantId));
  }

  @Override
  public List<IndexedModelInfo> search(Optional<Collection<String>> tenantIds, String searchExpression) {
    PreConditions.notNull(tenantIds, "tenantIds must not be null.");
    
    SearchParameters searchParameters = makeSearchParams(tenantIds, Strings.nullToEmpty(searchExpression));
    
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(makeElasticSearchQuery(searchParameters));
    searchSourceBuilder.from(0);
    searchSourceBuilder.size(MAX_SEARCH_RESULTS);
    searchSourceBuilder.timeout(new TimeValue(3, TimeUnit.MINUTES));
    
    SearchRequest searchRequest = new SearchRequest(VORTO_INDEX);
    searchRequest.source(searchSourceBuilder);
    
    try {
      logger.info("Search Expression: " + searchExpression + " Elastic Search: " + searchRequest.toString());
      SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
      SearchHits hits = response.getHits();
      return Stream.of(hits.getHits()).map(this::fromSearchHit).collect(Collectors.toList());
    } catch (IOException e) {
      throw new IndexingException("Error while querying the index for '" + Strings.nullToEmpty(searchExpression) + "' expression", e);
    }
  }
  
  private IndexedModelInfo fromSearchHit(SearchHit searchHit) {
    IndexedModelInfo modelInfo = new IndexedModelInfo();
    
    Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
    
    modelInfo.setTenantId((String) sourceAsMap.get(TENANT_ID));
    modelInfo.setModelId((String) sourceAsMap.get(MODEL_ID));
    modelInfo.setType(ModelType.valueOf((String) sourceAsMap.get(MODEL_TYPE)));
    modelInfo.setState((String) sourceAsMap.get(STATE));
    modelInfo.setVisibility((String) sourceAsMap.get(VISIBILITY));
    modelInfo.setAuthor((String) sourceAsMap.get(AUTHOR));
    modelInfo.setDescription((String) sourceAsMap.get(DESCRIPTION));
    modelInfo.setDisplayName((String) sourceAsMap.get(DISPLAY_NAME));
    
    return modelInfo;
  }
  
  private SearchParameters makeSearchParams(Optional<Collection<String>> tenantIds, String searchExpression) {
    Optional<ModelType> modelType = Optional.empty();
    for(ModelType type : ModelType.values()) {
      if (searchExpression.contains(type.name())) {
        modelType = Optional.of(type);
      }
    }
    
    Optional<ModelState> modelState = Optional.empty();
    for(ModelState state : ModelState.values()) {
      if (searchExpression.contains("state:" + state.getName())) {
        modelState = Optional.of(state);
      }
    }
    
    Optional<String> searchExpr = Optional.empty();
    Matcher matcher = searchExprPattern.matcher(searchExpression);
    if (matcher.find()) {
      searchExpr = Optional.of(matcher.group(1));
    }
    
    Optional<String> author = Optional.empty();
    matcher = authorExprPattern.matcher(searchExpression);
    if (matcher.find()) {
      author = Optional.of(matcher.group(1));
    }
    
    return new SearchParameters(tenantIds, searchExpr, modelState, modelType, author);
  }
  
  private QueryBuilder makeElasticSearchQuery(SearchParameters params) {
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    
    if (params.expression.isPresent()) {
      queryBuilder = queryBuilder.must(matches(params.expression.get()));
    }
    
    if (params.state.isPresent()) {
      queryBuilder = queryBuilder.must(QueryBuilders.termQuery(STATE, params.state.get().getName()));
    }
    
    if (params.type.isPresent()) {
      queryBuilder = queryBuilder.must(QueryBuilders.termQuery(MODEL_TYPE, params.type.get().toString()));
    }
    
    if (params.author.isPresent()) {
      queryBuilder = queryBuilder.must(QueryBuilders.termQuery(AUTHOR, params.author.get()));
    }
    
    if (params.tenantIds.isPresent()) {
      if (params.tenantIds.get().isEmpty()) {
        queryBuilder = queryBuilder.must(isPublic());
      } else {
        queryBuilder = queryBuilder.must(or(isPublic(), isOwnedByTenants(params.tenantIds.get())));
      }
    }
    
    return queryBuilder;
  }
  
  private QueryBuilder or(QueryBuilder ... queries) {
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    for(QueryBuilder query : queries) {
      boolQuery = boolQuery.should(query);
    }
    return boolQuery;
  }
  
  private QueryBuilder matches(String expression) {
    return or(QueryBuilders.matchPhrasePrefixQuery(DISPLAY_NAME, expression),
        QueryBuilders.matchPhrasePrefixQuery(DESCRIPTION, expression),
        QueryBuilders.wildcardQuery(MODEL_ID, "*" + expression + "*"));
  }
  
  private QueryBuilder isPublic() {
    return QueryBuilders.termQuery(VISIBILITY, PUBLIC);
  }
  
  private QueryBuilder isOwnedByTenants(Collection<String> tenants) {
    return QueryBuilders.termsQuery(TENANT_ID, tenants);
  }
  
  private class SearchParameters {
    Optional<Collection<String>> tenantIds; 
    Optional<String> expression; 
    Optional<ModelState> state;
    Optional<ModelType> type; 
    Optional<String> author;
    
    public SearchParameters(Optional<Collection<String>> tenantIds, Optional<String> expression,
        Optional<ModelState> state, Optional<ModelType> type, Optional<String> author) {
      this.tenantIds = tenantIds;
      this.expression = expression;
      this.state = state;
      this.type = type;
      this.author = author;
    }
  }
}
