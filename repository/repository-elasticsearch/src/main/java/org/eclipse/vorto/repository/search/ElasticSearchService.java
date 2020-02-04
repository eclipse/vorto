/**
 * Copyright (c) 2018, 2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.search;

import com.google.common.base.Strings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.ModelVisibility;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.search.extractor.BasicIndexFieldExtractor;
import org.eclipse.vorto.repository.search.extractor.IIndexFieldExtractor;
import org.eclipse.vorto.repository.search.extractor.IIndexFieldExtractor.FieldType;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.utils.PreConditions;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceDto;
import org.eclipse.vorto.repository.workflow.ModelState;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
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
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Search Service implementation using a remote Elastic Search Service.<br/>
 * This search service provides a powerful and flexible way to look for specific models.<br>
 * See documentation for {@link ElasticSearchService#search(String, IUserContext)} for full specifications.
 * @author mena-bosch (refactored)
 */
public class ElasticSearchService implements IIndexingService, ISearchService {

  public static final String TEXT = "text";

  public static final String KEYWORD = "keyword";

  private static final String TENANT_ID = "tenantId";

  private static final int MAX_SEARCH_RESULTS = 1000;

  private static final String PUBLIC = "public";

  private static final String DOC = "_doc";

  private static final String VORTO_INDEX = "vorto";

  private static final String VORTO_INDEX_TEMP = "vorto_temp";

  private static Logger logger = Logger.getLogger(ElasticSearchService.class);

  private Collection<IIndexFieldExtractor> fieldExtractors = new ArrayList<IIndexFieldExtractor>();

  private RestHighLevelClient client;

  private IModelRepositoryFactory repositoryFactory;

  private ITenantService tenantService;

  /**
   * An un-tagged name token in a search will search into the following fields:
   * <ul>
   *   <li>
   *     {@link BasicIndexFieldExtractor#DISPLAY_NAME}
   *   </li>
   *   <li>
   *     {@link BasicIndexFieldExtractor#DESCRIPTION}
   *   </li>
   *   <li>
   *     {@link BasicIndexFieldExtractor#MODEL_NAME_SEARCHABLE}
   *   </li>
   * </ul>
   * The ranking of results is equal for the 3 fields.
   */
  public static final Map<String, Float> UNTAGGED_NAME_FIELDS_FOR_QUERY = new HashMap<>();
  static {
    UNTAGGED_NAME_FIELDS_FOR_QUERY.put(BasicIndexFieldExtractor.DISPLAY_NAME, 1.0f);
    UNTAGGED_NAME_FIELDS_FOR_QUERY.put(BasicIndexFieldExtractor.DESCRIPTION, 1.0f);
    UNTAGGED_NAME_FIELDS_FOR_QUERY.put(BasicIndexFieldExtractor.MODEL_NAME_SEARCHABLE, 1.0f);
  }

  /**
   * A value tagged {@literal name:} will be searched in the following field:
   * {@link BasicIndexFieldExtractor#DISPLAY_NAME}
   */
  public static final Map<String, Float> TAGGED_NAME_FIELDS_FOR_QUERY = new HashMap<>();
  static {
    TAGGED_NAME_FIELDS_FOR_QUERY.put(BasicIndexFieldExtractor.DISPLAY_NAME, 1.0f);
  }

  /**
   * A value tagged {@literal userReference:} will be searched in the following two fields:
   * <ul>
   *   <li>
   *     {@link BasicIndexFieldExtractor#AUTHOR}
   *   </li>
   *   <li>
   *     {@link BasicIndexFieldExtractor#MODIFIED_BY}
   *   </li>
   * </ul>
   * The ranking of results is equal for the 2 fields.
   */
  public static final Map<String, Float> USER_REFERENCE_FIELDS_FOR_QUERY = new HashMap<>();
  static {
    USER_REFERENCE_FIELDS_FOR_QUERY.put(BasicIndexFieldExtractor.AUTHOR, 1.0f);
    USER_REFERENCE_FIELDS_FOR_QUERY.put(BasicIndexFieldExtractor.MODIFIED_BY, 1.0f);
  }

  private static final String ANALYZER = "standard";

  public ElasticSearchService(RestHighLevelClient client, IModelRepositoryFactory repositoryFactory,
      ITenantService tenantService) {
    this.client = client;
    this.repositoryFactory = repositoryFactory;
    this.tenantService = tenantService;
    this.fieldExtractors.add(new BasicIndexFieldExtractor());

    init();
  }

  /**
   * Creates a Search Index, if it does not yet exist
   */
  private void init() {
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
      logger.info(String.format("Index '%s' created.", VORTO_INDEX));
    } else {
      logger.info("Index already exist");
    }
  }

  private boolean indexExist(String index) {
    GetIndexRequest request = new GetIndexRequest(index);
    try {
      return client.indices().exists(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new IndexingException(String.format("Error while checking if index '%s' exist.", index), e);
    }
  }

  /**
   * This forces a full reindexing of all model, and should be used in the rare occasion where
   * a change in the mapping has been created, e.g. a new searchable field, or a change in a field's
   * type.<br/>
   * The following operations are performed in order, synchronously:
   * <ol>
   *   <li>
   *     Checks if the vorto index actually exists. If it doesn't, creates it and returns (nothing
   *     else worth doing).
   *   </li>
   *   <li>
   *     Checks if the vorto temp index ({@link ElasticSearchService#VORTO_INDEX_TEMP}) exists. If
   *     so, deletes the temp index.
   *   </li>
   *   <li>
   *     Creates the temp index with the current mapping.
   *   </li>
   *   <li>
   *     Uses a reindex request to merge the vorto index ({@link ElasticSearchService#VORTO_INDEX})
   *     into the temp index.
   *   </li>
   *   <li>
   *     Deletes the vorto index.
   *   </li>
   *   <li>
   *     Re-creates the vorto index.
   *   </li>
   *   <li>
   *     Merges back the temp index in the vorto index.
   *   </li>
   *   <li>
   *     Deletes the temp index.
   *   </li>
   *   <li>
   *     Reindexes all model in the vorto index - see {@link IIndexingService#reindexAllModels()}.
   *   </li>
   * </ol>
   *
   * @return
   */
  @Override
  public IndexingResult forceReindexAllModels() {
    // no Vorto index - nothing to do
    if (!indexExist(VORTO_INDEX)) {
      createIndexIfNotExisting();
      return new IndexingResult();
    }
    // first delete the VORTO_INDEX_TEMP index if it exists
    if (indexExist(VORTO_INDEX_TEMP)) {
      DeleteIndexRequest deleteTempIndexRequest = new DeleteIndexRequest().indices(VORTO_INDEX_TEMP);
      try {
        client.indices().delete(deleteTempIndexRequest, RequestOptions.DEFAULT);
      }
      catch (IOException ioe) {
        throw new IndexingException(ioe.getMessage(), ioe);
      }
    }
    // creates the temporary index to hold the new mapping
    CreateIndexRequest createTempIndexRequest = new CreateIndexRequest(VORTO_INDEX_TEMP);
    createTempIndexRequest.mapping(createMappingForIndex());
    try {
      client.indices().create(createTempIndexRequest, RequestOptions.DEFAULT);
    }
    catch (IOException ioe) {
      throw new IndexingException(ioe.getMessage(), ioe);
    }
    // copies the vorto index data to the new temp index with new mapping
    ReindexRequest reindexRequest = new ReindexRequest().setSourceIndices(VORTO_INDEX).setDestIndex(VORTO_INDEX_TEMP);
    reindexRequest.setConflicts("proceed");
    reindexRequest.setRefresh(true);
    try {
      client.reindex(reindexRequest, RequestOptions.DEFAULT);
    }
    catch (IOException ioe) {
      throw new IndexingException(ioe.getMessage(), ioe);
    }
    // deletes the vorto index
    DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest().indices(VORTO_INDEX);
    try {
      client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
    }
    catch (IOException ioe) {
      throw new IndexingException(ioe.getMessage(), ioe);
    }
    // re-creates the vorto index
    createIndexIfNotExisting();

    // moves data back to vorto index
    ReindexRequest moveBackToVortoIndex = new ReindexRequest().setSourceIndices(VORTO_INDEX_TEMP).setDestIndex(VORTO_INDEX);
    reindexRequest.setConflicts("proceed");
    reindexRequest.setRefresh(true);
    try {
      client.reindex(moveBackToVortoIndex, RequestOptions.DEFAULT);
    }
    catch (IOException ioe) {
      throw new IndexingException(ioe.getMessage(), ioe);
    }

    // deletes the temp index
    DeleteIndexRequest deleteTempIndexRequest = new DeleteIndexRequest().indices(VORTO_INDEX_TEMP);
    try {
      client.indices().delete(deleteTempIndexRequest, RequestOptions.DEFAULT);
    }
    catch (IOException ioe) {
      throw new IndexingException(ioe.getMessage(), ioe);
    }

    // finally, re-import all models
    return reindexAllModels();
  }

  private boolean createIndexWithMapping(String index, Map<String, Object> mapping) {
    CreateIndexRequest request = new CreateIndexRequest(index);
    request.mapping(mapping);
    try {
      CreateIndexResponse createIndexResponse =
          client.indices().create(request, RequestOptions.DEFAULT);
      return createIndexResponse.isAcknowledged();
    } catch (IOException e) {
      throw new IndexingException("Error while creating index '" + index + "'.", e);
    }
  }

  private Map<String, Object> createMappingForIndex() {
    Map<String, Object> properties = new HashMap<>();
    properties.put(TENANT_ID, createPropertyWithType(FieldType.KEY));

    for (IIndexFieldExtractor extractor : fieldExtractors) {
      extractor.getFields().forEach((key, value) -> {
        properties.put(key, createPropertyWithType(value));
      });
    }

    Map<String, Object> mapping = new HashMap<>();
    mapping.put("properties", properties);
    return mapping;
  }

  private Object createPropertyWithType(FieldType type) {
    Map<String, Object> property = new HashMap<>();
    property.put("type", type.equals(FieldType.KEY) ? KEYWORD : TEXT);
    return property;
  }

  private void deleteAllModels(String index) {
    deleteByQuery(index, QueryBuilders.matchAllQuery());
  }

  @Override
  public IndexingResult reindexAllModels() {
    IndexingResult result = new IndexingResult();

    // (1) Delete all models in the index
    deleteAllModels(VORTO_INDEX);

    // (2) Index all models in all the tenants
    tenantService.getTenants().stream().forEach(tenant -> {
      IModelRepository repo = this.repositoryFactory.getRepository(tenant.getTenantId());
      List<ModelInfo> modelsToIndex = repo.search("");
      if (!modelsToIndex.isEmpty()) {
        BulkRequest bulkRequest = new BulkRequest();

        modelsToIndex.forEach(model -> {
          bulkRequest.add(createIndexRequest(model, repo.getTenantId()));
        });

        try {
          BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
          // temporary fix: getting namespace name instead of tenant ID here
          // in the long run, once the tenant service is gone we can normalize
          result.addIndexedNamespace(NamespaceDto.fromTenant(tenant).getName(), modelsToIndex.size());
          logger.info(
            String.format(
              "Received %d replies for tenant '%s' with %d models",
              bulkResponse.getItems().length, repo.getTenantId(), modelsToIndex.size()
            )
          );
        } catch (IOException e) {
          throw new IndexingException(
            String.format("Error trying to index all models in '%s' tenant.", repo.getTenantId()), e);
        }
      }
    });

    return result;
  }

  private void deleteByQuery(String index, QueryBuilder query) {
    logger.info(String.format("Trying to delete all models in index '%s'", index));
    DeleteByQueryRequest request = new DeleteByQueryRequest(index);
    request.setQuery(query);
    try {
      BulkByScrollResponse bulkResponse = client.deleteByQuery(request, RequestOptions.DEFAULT);
      logger.info(String.format("Deleted %d models in the index '%s'", bulkResponse.getTotal(), index));
    } catch (IOException e) {
      throw new IndexingException(String.format("Error deleting all models in the '%s' index.", index), e);
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
      throw new IndexingException(
          String.format("Error while querying if model '%s' exist.", modelId.getPrettyFormat()), e
      );
    }
  }

  @Override
  public void indexModel(ModelInfo modelInfo, String tenantId) {
    PreConditions.notNull(modelInfo, "modelInfo must not be null.");
    PreConditions.notNullOrEmpty(tenantId, TENANT_ID);

    logger.info(String.format("Indexing model '%s'", modelInfo.getId()));

    try {
      IndexResponse indexResponse =
          client.index(createIndexRequest(modelInfo, tenantId), RequestOptions.DEFAULT);
      if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
        logger.info(String.format("Index created for '%s'", modelInfo.getId().getPrettyFormat()));
      }
    } catch (IOException e) {
      throw new IndexingException(
          String.format("Error while indexing '%s'", modelInfo.getId().getPrettyFormat()), e);
    }
  }

  private IndexRequest createIndexRequest(ModelInfo modelInfo, String tenantId) {
    Map<String, Object> jsonMap = new HashMap<>();
    jsonMap.put(TENANT_ID, tenantId);

    for (IIndexFieldExtractor extractor : fieldExtractors) {
      extractor.extractFields(modelInfo).forEach((key, value) -> {
        jsonMap.put(key, value);
      });
    }

    return new IndexRequest(VORTO_INDEX, DOC, modelInfo.getId().getPrettyFormat()).source(jsonMap);
  }

  @Override
  public void updateIndex(ModelInfo modelInfo) {
    PreConditions.notNull(modelInfo, "modelInfo must not be null.");

    logger.info(String.format("Updating index of model '%s'", modelInfo.getId()));

    UpdateRequest request = new UpdateRequest(VORTO_INDEX, DOC, modelInfo.getId().getPrettyFormat())
        .doc(updateMap(modelInfo));

    try {
      UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
      if (response.getResult() == DocWriteResponse.Result.UPDATED) {
        logger.info(String.format("Index updated for '%s'", modelInfo.getId().getPrettyFormat()));
      }
    } catch (IOException e) {
      throw new IndexingException(
          String.format("Error while updating the index of '%s'", modelInfo.getId().getPrettyFormat()), e
      );
    }
  }

  private Map<String, Object> updateMap(ModelInfo modelInfo) {
    Map<String, Object> jsonMap = new HashMap<>();
    for (IIndexFieldExtractor extractor : fieldExtractors) {
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
      throw new IndexingException(
          String.format("Error while deleting the index of '%s'", modelId.getPrettyFormat()), e
      );
    }
  }

  @Override
  public void deleteIndexForTenant(String tenantId) {
    deleteByQuery(VORTO_INDEX, QueryBuilders.termQuery(TENANT_ID, tenantId));
  }

  /**
   * @see ElasticSearchService#search(String, IUserContext)
   * @param searchExpression
   * @return
   */
  public List<ModelInfo> search(String searchExpression) {
      return search(searchExpression, UserContext.user(SecurityContextHolder.getContext().getAuthentication()));
  }

  /**
   * The {@code searchExpression} value is composed of tokens explained below. <br/>
   * All tokens are optional, meaning that a search expression can actually be empty (and will
   * consequently yield all known models visible to the tenant searching). <br/>
   * Tokens are made of single values or tagged values, space-separated.<br/>
   * Single values are always interpreted as name searches.<br/>
   * Tagged values are expressed in the form of {@literal tagname:value}, with <b>no whitespace</b>
   * before or after the colon. <br/>
   * Any type of tag that is specified in the search is a hard-requirement, i.e. it <i>must</i> be
   * present in the model that is returned. <br/>
   * However, it is possible to use multiple identical tags with different values (e.g.
   * {@literal type:InformationModel} {@literal type:Functionblock}). In that case, the multiple
   * values for that tag are required <i>in alternative</i> to one another - in other words,
   * qualifying results will contain at least one of the values for that type.<br/>
   * To of the search types implicitly search in different fields in alternative: {@literal name:}
   * and {@literal author:} (see below for details).<br/>
   * The <i>only</i> search term that does not require a tag is the {@literal name:}. In
   * essence, any untagged value is considered as {@literal name:[the value]}.<br/>
   * All values are searched <b>case-insensitive</b>. <br/>
   * All tags are <i>also</i> parsed <b>case-sensitive</b>, e.g. {@literal state:} can also be
   * expressed as {@literal STATE:} (or {@literal sTaTe:}, etc. for that matter). Tags are
   * identified by their known descriptor (see below for details), followed by a colon
   * ({@literal :}).<br/>
   * Multiple search terms can be separated by a single or multiple whitespaces - which is why
   * whitespace is <b>not</b> allowed in values.<br/>
   * All values can be expressed as plain text (e.g. {@literal author:Mena}), or with
   * wildcards (e.g. {@literal author:?en*}.<br/>
   * Asterisk wildcards ({@literal *}) allow multiple characters, while question mark wildcards
   * ({@literal ?}) allow a single character.<br/>
   * For backwards-compatibility, name values (tagged or not) that do not contain any wildcard
   * are automatically appended a multi-character wildcard at the end, e.g. {@literal Raspberry} or
   * {@literal name:Raspberry} become {@literal Raspberry*} and {@literal name:Raspberry*}.<br/>
   * The supported tags are listed below:
   * <ul>
   *   <li>
   *     The model type (with tag {@literal type:)}, which can be:
   *     <ul>
   *       <li>All types (i.e. empty)</li>
   *       <li>{@literal InformationModel}</li>
   *       <li>{@literal Functionblock}</li>
   *       <li>{@literal Datatype}</li>
   *       <li>{@literal Mapping}</li>
   *       <li>Any wildcard search</li>
   *     </ul>
   *     In the web UI, the model type can be set to any specific type by changing the value in the
   *     {@literal TYPES} drop-down. It defaults to all types.
   *     @see ModelType
   *   </li>
   *   <li>
   *     The model state (with tag {@literal state:)}, which can be:
   *    <ul>
   *      <li>All states (i.e. empty)</li>
   *      <li>{@literal Draft}</li>
   *       <li>{@literal InReview}</li>
   *      <li>{@literal Released} (default value in the repository's web UI search)</li>
   *      <li>{@literal Deprecated}</li>
   *      <li>Any wildcard search</li>
   *    </ul>
   *    In the web UI, the model state can be set to any specific type by changing the value in the
   *    {@literal STATES} drop-down. It defaults to {@literal Released}.
   *    @see ModelState
   *   </li>
   *   <li>
   *     The model's name (with optional tag {@literal name:}), which is automatically searched as
   *     {@literal displayName}, or {@literal description} or{@literal searchableName}.
   *   </li>
   *   <li>
   *     The model's author (with tag {@literal author:}). In the web UI, the author can be set
   *     implicitly to the current user only by checking the {@literal Only My Models} checkbox.
   *   </li>
   *   <li>
   *     The model's user reference (with tag {@literal userReference:}), which is automatically
   *     searched as {@literal author} or {@literal lastModifiedBy}.
   *   </li>
   *   <li>
   *     The model's visibility (with tag {@literal visibility:}). In the web UI, the visibility
   *     can be set implicitly to {@literal Public} by checking the {@literal Only Public Models}
   *     checkbox.
   *     @see ModelVisibility
   *   </li>
   *   <li>
   *     In addition to those search options, a collection of tenant IDs containing the current
   *     tenant's ID is typically inferred from context, in order to filter items by ownership.
   *   </li>
   * </ul>
   * @param searchExpression The search expression
   * @param userContext The user context with which to execute this query
   * @return
   */
  public List<ModelInfo> search(String searchExpression, IUserContext userContext) {

    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(toESQuery(SearchParameters.build(findTenantsOfUser(userContext), searchExpression)));
    searchSourceBuilder.from(0);
    searchSourceBuilder.size(MAX_SEARCH_RESULTS);
    searchSourceBuilder.timeout(new TimeValue(3, TimeUnit.MINUTES));

    SearchRequest searchRequest = new SearchRequest(VORTO_INDEX);
    searchRequest.source(searchSourceBuilder);

    try {

      logger.info(String.format("Search Expression: %s Elastic Search: %s", searchExpression, searchRequest.toString()));
      SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
      SearchHits hits = response.getHits();
      logger.info(String.format("Number of hits: %d", hits.getTotalHits()));
      return Stream.of(hits.getHits()).map(this::fromSearchHit).collect(Collectors.toList());
    } catch (IOException e) {
      throw new IndexingException(
        String.format("Error while querying the index for '%s' expression", Strings.nullToEmpty(searchExpression)), e
      );
    }
  }

  private Collection<String> findTenantsOfUser(IUserContext userContext) {
    if (userContext.isAnonymous()) {
      return Collections.emptyList();
    } else {
      return tenantService.getTenants().stream()
          .filter(getUserFilter(userContext))
          .map(t -> t.getTenantId())
          .collect(Collectors.toList());
    }
  }

  private Predicate<Tenant> getUserFilter(IUserContext userContext) {
    if (userContext.isSysAdmin()) {
      return tenant -> true;
    } else {
      return tenant -> tenant.hasUser(userContext.getUsername());
    }
  }

  private ModelInfo fromSearchHit(SearchHit searchHit) {
    ModelInfo modelInfo = new ModelInfo();

    Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();

    modelInfo.setId(
        ModelId.fromPrettyFormat((String) sourceAsMap.get(BasicIndexFieldExtractor.MODEL_ID)));
    modelInfo
        .setType(ModelType.valueOf((String) sourceAsMap.get(BasicIndexFieldExtractor.MODEL_TYPE)));
    modelInfo.setState((String) sourceAsMap.get(BasicIndexFieldExtractor.STATE));
    modelInfo.setVisibility((String) sourceAsMap.get(BasicIndexFieldExtractor.VISIBILITY));
    modelInfo.setAuthor((String) sourceAsMap.get(BasicIndexFieldExtractor.AUTHOR));
    modelInfo.setDescription((String) sourceAsMap.get(BasicIndexFieldExtractor.DESCRIPTION));
    modelInfo.setDisplayName((String) sourceAsMap.get(BasicIndexFieldExtractor.DISPLAY_NAME));
    modelInfo.setHasImage(
        Boolean.parseBoolean((String) sourceAsMap.get(BasicIndexFieldExtractor.MODEL_HASIMAGE)));
    String createdOn = (String) sourceAsMap.get(BasicIndexFieldExtractor.MODEL_CREATIONDATE);
    modelInfo.setCreationDate(new Date(Long.parseLong(createdOn)));

    return modelInfo;
  }

  private static QueryBuilder buildORBoolQueryWith(QueryBuilder... queries) {
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    for (QueryBuilder query : queries) {
      boolQuery = boolQuery.should(query);
    }
    return boolQuery;
  }

  private static QueryBuilder isPublic() {
    return QueryBuilders.termQuery(BasicIndexFieldExtractor.VISIBILITY, PUBLIC);
  }

  private static QueryBuilder isOwnedByTenants(Collection<String> tenants) {
    return QueryBuilders.termsQuery(TENANT_ID, tenants);
  }

  /**
   * Variant of {@link ElasticSearchService#makeChildQuery(BoolQueryBuilder, Collection, String)}
   * that is only in use for search of values into multiple keys.<br/>
   * Contrary to its overload, this does not support enum element name correction, as it is
   * intended to search arbitrary text such as model names, authors, etc.
   * @param parent
   * @param values
   * @param keys
   * @return
   */
  private static QueryBuilder makeChildQuery(BoolQueryBuilder parent, Collection<String> values, Map<String, Float> keys) {
    // no need to go further - returning parent query as-is
    if (values.isEmpty()) {
      return parent;
    }
    // single value: remove one query layer and add must query to parent directly
    if (values.size() == 1) {
      parent = parent.must(QueryBuilders.queryStringQuery(values.toArray(new String[values.size()])[0]).fields(keys).analyzer(ANALYZER));
    }
    // multiple values: wrap in bool query under parent and add leaves with OR relationship
    else {
      BoolQueryBuilder child = QueryBuilders.boolQuery();
      for (String value : values) {
        child = child.should(QueryBuilders.queryStringQuery(value).fields(keys).analyzer(ANALYZER));
      }
      parent = parent.must(child);
    }
    return parent;
  }

  /**
   * Used by {@link ElasticSearchService#toESQuery(SearchParameters)} exclusively. <br/>
   * Appends children bool queries to the given parent. <br/>
   * Values containing only one element produce one child to the parent {@link BoolQueryBuilder}
   * in an {@literal AND} relationship to its peers, which contains the search terms. <br/>
   * Conversely, multiple values produce the same child, but also add as many children to it as
   * there are values to search, each in an {@literal OR} relationship with one another.
   * @param parent
   * @param values
   * @param key
   * @return
   */
  private static QueryBuilder makeChildQuery(BoolQueryBuilder parent, Collection<String> values, String key) {
    // no need to go further - returning parent query as-is
    if (values.isEmpty()) {
      return parent;
    }
    // single value: remove one query layer and add must query to parent directly
    if (values.size() == 1) {
      // getting value from single collection element
      String value = values.toArray(new String[values.size()])[0];
      // value should be searched as query string as it contains wildcards
      parent = parent.must(QueryBuilders.queryStringQuery(value).field(key).analyzer(ANALYZER));
    }
    else {
      BoolQueryBuilder child = QueryBuilders.boolQuery();
      // producing a grandchild for each value
      for (String value: values) {
        // value should be searched as query string as it contains wildcards
        child = child.should(QueryBuilders.queryStringQuery(value).field(key).analyzer(ANALYZER));
      }
      parent = parent.must(child);
    }
    return parent;
  }

  /**
   * Builds a boolean query for ElasticSearch, with the following rules:
   * <ul>
   *   <li>
   *     Top-level arguments are all required, i.e. the top level terms are in a boolean
   *     {@literal AND} relationship with one another. For instance, if specified, the type and
   *     state of the searched model are both required.
   *   </li>
   *   <li>
   *     Child-level arguments are <i>either</i> required, i.e. multiple terms for the same
   *     property are in a boolean {@literal OR} relationship with one another. For instance, if
   *     specified, two different values for the model type will return models for <i>either</i> of
   *     the two specified values.
   *   </li>
   *   <li>
   *     For name searches ({@literal name:} tag optional), 3 fields will be automatically searched
   *     in alternative to one another: {@literal displayName}, {@literal description} and
   *     {@literal searchableName}. Also worth noting, name search terms that have no wildcards will
   *     automatically be appended a multi-character wildcard for backwards-compatibility.
   *   </li>
   *   <li>
   *     Similarly to name searches, {@literal author:} searches use two fields in alternative to
   *     one another: {@literal author:} and {@literal lastModifiedBy:}. Unlike name searches,
   *     author searches are not appended any wildcard automatically (nor any other field search,
   *     for that matter).
   *   </li>
   * </ul>
   * @param parameters
   * @return
   */
  public static QueryBuilder toESQuery(SearchParameters parameters) {
    BoolQueryBuilder result = QueryBuilders.boolQuery();

    // adding tenant ids
    Set<String> tenantIds = parameters.getTenantIds();
    if (tenantIds.isEmpty()) {
      result = result.must(isPublic());
    } else {
      result = result.must(buildORBoolQueryWith(isPublic(), isOwnedByTenants(tenantIds)));
    }

    /*
     adding tagged and untagged names - special rules:
     1. non-wildcard values are appended a multi-character wildcard
     2. tagged names resolve to display name / name
     3. un-tagged names resolve to name, description and display name
     */
    Set<String> taggedNames = parameters.getTaggedNames();
    if (!taggedNames.isEmpty()) {
        makeChildQuery(
            result, taggedNames, TAGGED_NAME_FIELDS_FOR_QUERY
        );
    }

    Set<String> unTaggedNames = parameters.getUntaggedNames();
    if (!unTaggedNames.isEmpty()) {
      makeChildQuery(
          result, unTaggedNames, UNTAGGED_NAME_FIELDS_FOR_QUERY
      );
    }

    // adding states
    makeChildQuery(result, parameters.getStates(), BasicIndexFieldExtractor.STATE);

    // adding types
    makeChildQuery(result, parameters.getTypes(), BasicIndexFieldExtractor.MODEL_TYPE);

    // adding authors
    makeChildQuery(result, parameters.getAuthors(), BasicIndexFieldExtractor.AUTHOR);

    // adding user references
    makeChildQuery(result, parameters.getUserReferences(), USER_REFERENCE_FIELDS_FOR_QUERY);

    // adding visibilities
    makeChildQuery(result, parameters.getVisibilities(), BasicIndexFieldExtractor.VISIBILITY);

    // adding namespaces
    makeChildQuery(result, parameters.getNamespaces(), BasicIndexFieldExtractor.NAMESPACE);

    // adding versions
    makeChildQuery(result, parameters.getVersions(), BasicIndexFieldExtractor.VERSION);

    return result;
  }

  public Collection<IIndexFieldExtractor> getFieldExtractors() {
    return fieldExtractors;
  }

  public void setFieldExtractors(Collection<IIndexFieldExtractor> fieldExtractors) {
    this.fieldExtractors = fieldExtractors;
  }

}
