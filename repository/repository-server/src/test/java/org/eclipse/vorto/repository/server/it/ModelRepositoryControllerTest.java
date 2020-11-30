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
package org.eclipse.vorto.repository.server.it;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Base64;
import org.apache.log4j.Logger;
import org.eclipse.vorto.mapping.engine.model.spec.MappingSpecification;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.web.api.v1.dto.AttachResult;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.ModelFullDetailsDTO;
import org.eclipse.vorto.repository.web.api.v1.dto.ModelLink;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class ModelRepositoryControllerTest extends IntegrationTestBase {

  private static final Logger LOGGER = Logger.getLogger(ModelRepositoryControllerTest.class);

  @Autowired
  protected AttachmentValidator attachmentValidator;

  @Test
  public void getModelImage() throws Exception {
    createImage("stock_coffee.jpg", testModel.prettyName, userSysadmin)
        .andExpect(status().isCreated());

    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/images").with(userSysadmin))
        .andExpect(status().isOk());
  }

  /*
   * Retrieve Model image with empty image attachments
   */
  @Test
  public void getModelImageWithEmptyAttachments() throws Exception {
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/images").with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  /**
   * This originally only tested that consecutive image attachments on a same model responded with
   * HTTP 201. <br/>
   * With the new "display image" tag that is programmatically unique to the last image updated to
   * a model, this test is enriched with a few additional checks. <br/>
   * There is little to test at controller-level, because the images themselves are mocked, and the
   * GET calls actually return the model name as image in the header (so one cannot check that the
   * actual image file name is returned).<br/>
   * Here, we only check that there is indeed an image once it's been added - twice. <br/>
   * More thorough tests are added at repository level.
   *
   * @throws Exception
   */
  @Test
  public void uploadModelImage() throws Exception {
    createImage("stock_coffee.jpg", testModel.prettyName, userSysadmin)
        .andExpect(status().isCreated());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/images").with(userSysadmin))
        .andExpect(status().isOk());
    createImage("model_image.png", testModel.prettyName, userSysadmin)
        .andExpect(status().isCreated());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/images").with(userSysadmin))
        .andExpect(status().isOk());
  }

  /**
   * Contrary to the attachment controller, which is API v.1 and cannot be changed, we can
   * respond with a specific status in the ModelRepositoryController when an attachment is too
   * large.
   *
   * @throws Exception
   */
  @Test
  public void uploadModelImageSizeTooLarge() throws Exception {
    createImage("stock_coffee.jpg", testModel.prettyName, userSysadmin,
        (attachmentValidator.getMaxFileSizeSetting() + 1) * 1024 * 1024)
        .andExpect(status().isPayloadTooLarge());
  }

  @Test
  public void uploadModelImageSizeReasonable() throws Exception {
    createImage("stock_coffee.jpg", testModel.prettyName, userSysadmin,
        (attachmentValidator.getMaxFileSizeSetting() - 1) * 1024 * 1024)
        .andExpect(status().isCreated());
  }

  @Test
  public void createModelWithAPI() throws Exception {
    String modelId = "com.test.Location:1.0.0";
    String fileName = "Location.fbmodel";
    createNamespaceSuccessfully("com.test", userSysadmin);
    repositoryServer
        .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
            .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    repositoryServer
        .perform(post("/rest/models/" + modelId + "/" + ModelType.fromFileName(fileName))
            .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict());
    repositoryServer.perform(delete("/rest/models/" + modelId).with(userSysadmin));
  }

  @Test
  public void createVersionOfModel() throws Exception {
    TestModel testModel = TestModel.TestModelBuilder.aTestModel().build();
    testModel.createModel(repositoryServer, userSysadmin);
    repositoryServer.perform(post("/rest/models/" + testModel.prettyName + "/versions/1.0.1")
        .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    repositoryServer.perform(post("/rest/models/com.test1:Location:1.0.0/versions/1.0.1")
        .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    repositoryServer.perform(delete("/rest/models/" + testModel.prettyName).with(userSysadmin));
  }

  @Test
  public void deleteModelResource() throws Exception {
    String modelId = "com.test.Location:1.0.0";
    String fileName = "Location.fbmodel";
    String json = createContent("Location.fbmodel");

    // making sure the model is not there
    repositoryServer.perform(delete("/rest/models/" + modelId).with(userSysadmin))
        .andExpect(status().isNotFound());

    createNamespaceSuccessfully("com.test", userSysadmin);
    Collaborator collaborator = new Collaborator();
    collaborator.setTechnicalUser(false);
    collaborator.setUserId(USER_MODEL_CREATOR_NAME);
    collaborator.setAuthenticationProviderId(GITHUB);
    collaborator.setRoles(Sets.newHashSet("model_creator"));
    addCollaboratorToNamespace("com.test", collaborator);
    createModel(userModelCreator, fileName, modelId);
    repositoryServer.perform(delete("/rest/models/" + modelId).with(userSysadmin))
        .andExpect(status().isOk());
    this.repositoryServer.perform(put("/rest/models/" + modelId)
        .contentType(MediaType.APPLICATION_JSON).content(json).with(userSysadmin))
        .andExpect(status().isNotFound());

    // delete non existent model
    repositoryServer.perform(delete("/rest/models/com.test:ASDASD:0.0.1").with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void getUserModels() throws Exception {
    this.repositoryServer.perform(get("/rest/models/mine/download").with(userSysadmin))
        .andExpect(status().isOk());
  }

  @Test
  public void downloadMappingsForPlatform() throws Exception {
    this.repositoryServer
        .perform(
            get("/rest/models/" + testModel.prettyName + "/download/mappings/test")
                .with(userSysadmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/com.test:Test1:1.0.0/download/mappings/test").with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void runDiagnostics() throws Exception {
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/diagnostics").with(userSysadmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/test:Test123:1.0.0/diagnostics").with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void getPolicies() throws Exception {
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policies").with(userSysadmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policies").with(userModelCreator))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/test:Test123:1.0.0/policies").with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void getUserPolicy() throws Exception {
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policy").with(userModelViewer))
        .andExpect(status().isNotFound());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policy").with(userSysadmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policy").with(userModelCreator))
        .andExpect(status().isOk());
  }

  @Test
  public void addValidPolicyEntry() throws Exception {
    String json =
        "{\"principalId\":\"" + USER_MODEL_CREATOR_NAME
            + "\", \"principalType\": \"User\", \"permission\":\"READ\"}";
    // Valid creation of policy
    this.repositoryServer
        .perform(put("/rest/models/" + testModel.prettyName + "/policies")
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userSysadmin))
        .andExpect(status().isOk());
    this.repositoryServer
        .perform(get("/rest/models/" + testModel.prettyName + "/policy").with(userModelCreator))
        .andExpect(result -> result.getResponse().getContentAsString().contains(
            "{\"principalId\":\"user3\",\"principalType\":\"User\",\"permission\":\"READ\",\"adminPolicy\":false}"));
  }

  @Test
  public void editOwnPolicyEntry() throws Exception {
    String json =
        "{\"principalId\":\"" + USER_MODEL_CREATOR_NAME
            + "\", \"principalType\": \"User\", \"permission\":\"READ\"}";
    // Try changing current user policy
    this.repositoryServer
        .perform(put("/rest/models/" + testModel.prettyName + "/policies")
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userModelCreator))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void addInvalidPolicyEntry() throws Exception {
    String json =
        "{\"principalId\":\"user3\", \"principalType\": \"AUser\", \"permission\":\"READ\"}";
    // Try changing current user policy
    this.repositoryServer
        .perform(put("/rest/models/" + testModel.prettyName + "/policies")
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userSysadmin))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void removePolicyEntry() throws Exception {
    String json =
        "{\"principalId\":\"" + USER_MODEL_CREATOR_NAME
            + "\", \"principalType\": \"User\", \"permission\":\"READ\"}";
    // Valid creation of policy
    this.repositoryServer
        .perform(put("/rest/models/" + testModel.prettyName + "/policies")
            .contentType(MediaType.APPLICATION_JSON).content(json).with(userSysadmin))
        .andExpect(status().isOk());
    repositoryServer
        .perform(
            delete("/rest/models/" + testModel.prettyName + "/policies/user2/User")
                .with(userSysadmin))
        .andExpect(status().isOk());
  }

  /*
   * Only models in Released state can be made public, all other states must return exception
   */
  @Test
  public void makeModelPublicNotReleased() throws Exception {
    this.repositoryServer
        .perform(post("/rest/models/" + testModel.prettyName + "/makePublic").with(userSysadmin))
        .andExpect(status().isForbidden());
  }

  /*
   * Users with SysAdmin access or model publisher role can only make models public, other users should receive Unauthorized
   * exception
   */
  @Test
  public void makeModelPublicNonSysAdmin() throws Exception {
    this.repositoryServer
        .perform(post("/rest/models/" + testModel.prettyName + "/makePublic").with(userModelViewer))
        .andExpect(status().isUnauthorized());
  }

  /**
   * This verifies that the {@literal DEFAULT} constraint applied to boolean types does not
   * prevent model parsing.
   *
   * @throws Exception
   */
  @Test
  public void createDatatypeWithBooleanDefaultConstraint() throws Exception {
    // creates namespace
    createNamespaceSuccessfully("org.eclipse.vorto.examples.type", userSysadmin);
    // creates model with constraint
    createModel(userSysadmin, "HasBooleanDefaultConstraint.type",
        "org.eclipse.vorto.examples.type.HasBooleanDefaultConstraint:1.0.0");
    // cleans up
    repositoryServer
        .perform(
            delete(String.format("/rest/namespaces/%s", "org.eclipse.vorto.examples.type"))
                .with(userSysadmin)
        )
        .andExpect(status().isNoContent());

  }

  /**
   * This verifies that a value (e.g. of {@literal 1.0}) in a float type constraint is not shadowed by
   * the Vortolang version value (typically also {@literal 1.0}).
   *
   * @throws Exception
   */
  @Test
  public void createDatatypeWithFloatDefaultConstraint() throws Exception {
    // creates namespace
    createNamespaceSuccessfully("org.eclipse.vorto.examples.type", userSysadmin);
    // creates model with constraint
    createModel(userSysadmin, "HasFloatDefaultConstraint.type",
        "org.eclipse.vorto.examples.type.HasFloatDefaultConstraint:1.0.1");
    // cleans up
    repositoryServer
        .perform(
            delete(String.format("/rest/namespaces/%s", "org.eclipse.vorto.examples.type"))
                .with(userSysadmin)
        )
        .andExpect(status().isNoContent());
  }

  /**
   * This verifies that a non-supported Vortolang version (anything {@literal != 1.0} at the time
   * of writing) prevents Vorto from saving a model with content.
   *
   * @throws Exception
   */
  @Test
  public void createDatatypeWithUnsupportedVortolangVersion() throws Exception {
    // creates namespace
    createNamespaceSuccessfully("org.eclipse.vorto.examples.type", userSysadmin);
    // creates model with constraint

    repositoryServer
        .perform(post(
            "/rest/models/org.eclipse.vorto.examples.type.UnsupportedVortolangVersion:1.0.0/"
                + ModelType.fromFileName("UnsupportedVortolangVersion.type"))
            .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    repositoryServer
        .perform(
            put("/rest/models/org.eclipse.vorto.examples.type.UnsupportedVortolangVersion:1.0.0")
                .with(userSysadmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createContent("UnsupportedVortolangVersion.type")))
        .andExpect(status().isBadRequest());

    // cleans up
    repositoryServer
        .perform(
            delete(String.format("/rest/namespaces/%s", "org.eclipse.vorto.examples.type"))
                .with(userSysadmin)
        )
        .andExpect(status().isNoContent());
  }

  /**
   * This verifies that a malformed vortolang version declaration (e.g. {@literal 0.blah})
   * prevents Vorto from saving a model with content.
   *
   * @throws Exception
   */
  @Test
  public void createDatatypeWithMalformedVortolangVersion() throws Exception {
    // creates namespace
    createNamespaceSuccessfully("org.eclipse.vorto.examples.type", userSysadmin);
    // creates model with constraint

    repositoryServer
        .perform(post(
            "/rest/models/org.eclipse.vorto.examples.type.MalformedVortolangVersion:1.0.0/"
                + ModelType.fromFileName("MalformedVortolangVersion.type"))
            .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    repositoryServer
        .perform(
            put("/rest/models/org.eclipse.vorto.examples.type.MalformedVortolangVersion:1.0.0")
                .with(userSysadmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createContent("MalformedVortolangVersion.type")))
        .andExpect(status().isBadRequest());

    // cleans up
    repositoryServer
        .perform(
            delete(String.format("/rest/namespaces/%s", "org.eclipse.vorto.examples.type"))
                .with(userSysadmin)
        )
        .andExpect(status().isNoContent());
  }

  /**
   * This test performs the following:
   * <ol>
   *   <li>
   *     Creates the {@literal com.test} namespace with a sysadmin user, to hold the models.
   *   </li>
   *   <li>
   *     Adds a non-sysadmin user with {@literal model_creator} role to the {@literal com.test}
   *     namespace.
   *   </li>
   *   <li>
   *     Creates a "Zone" datatype model from the corresponding file resource.
   *   </li>
   *   <li>
   *     Creates a "Lamp" functionblock model from the corresponding file resource.
   *   </li>
   *   <li>
   *     Creates an "Address" functionblock model from the corresponding file resource.
   *   </li>
   *   <li>
   *     Creates a "StreetLamp" model from the corresponding file resource, using the above
   *     functionblocks as dependencies.
   *   </li>
   *   <li>
   *     Adds an attachment to the "StreetLamp" model.
   *   </li>
   *   <li>
   *     Adds a link to the "StreetLamp" model.
   *   </li>
   *   <li>
   *     Adds a mapping to the "StreetLamp" model.
   *   </li>
   *   <li>
   *     Loads the "StreetLamp" model with the REST call used by the UI, and verifies / validates:
   *     <ul>
   *       <li>
   *         Basic {@link org.eclipse.vorto.repository.core.ModelInfo} properties.
   *       </li>
   *       <li>
   *         The Base64-encoded model syntax
   *         (see {@link ModelFullDetailsDTO#getEncodedModelSyntax()})
   *       </li>
   *       <li>
   *         Mapping (see {@link ModelFullDetailsDTO#getMappings()}).
   *       </li>
   *       <li>
   *         Attachment (see {@link ModelFullDetailsDTO#getAttachments()}
   *       </li>
   *       <li>
   *         Link (see {@link ModelFullDetailsDTO#getLinks()}
   *       </li>
   *       <li>
   *         References (see {@link ModelFullDetailsDTO#getReferences()}
   *       </li>
   *       <li>
   *         "Referenced by" (see {@link ModelFullDetailsDTO#getReferencedBy()})
   *       </li>
   *       <li>
   *         Policies (see {@link ModelFullDetailsDTO#getPolicies()} and
   *         {@link ModelFullDetailsDTO#getBestPolicy()} for the creating user, and conversely, for
   *         an extraneous user with no access.
   *       </li>
   *       <li>
   *         Workflow actions (see {@link ModelFullDetailsDTO#getActions()} for the creating user,
   *         and conversely, for an extraneous user with no access.
   *       </li>
   *     </ul>
   *   </li>
   *   <li>
   *     Loads the "Lamp" functionblock model with the REST call used by the UI, and verifies the
   *     "referenced by" node (see {@link ModelFullDetailsDTO#getReferencedBy()}.
   *   </li>
   *   <li>
   *     Finally, cleans up and deletes all 4 models, then the namespace.
   *   </li>
   * </ol>
   *
   * @throws Exception
   */
  @Test
  public void verifyFullModelPayloadForUI() throws Exception {
    // required for some extra properties in DTOs not annotated with Jackson polymorphism
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // model names, id strings and file names
    String namespace = "com.test";
    String version = "1.0.0";
    String idFormat = "%s.%s:%s";
    String zoneModelName = "Zone";
    String zoneModelID = String.format(idFormat, namespace, zoneModelName, version);
    String zoneFileName = zoneModelName.concat(".type");
    String lampModelName = "Lamp";
    String lampModelID = String.format(idFormat, namespace, lampModelName, version);
    String lampFileName = lampModelName.concat(".fbmodel");
    String addressModelName = "Address";
    String addressModelID = String.format(idFormat, namespace, addressModelName, version);
    String addressFileName = addressModelName.concat(".fbmodel");
    String streetLampModelName = "StreetLamp";
    String streetLampModelID = String.format(idFormat, namespace, streetLampModelName, version);
    String streetLampFileName = streetLampModelName.concat(".infomodel");
    String streetLampAttachmentFileName = "StreetLampAttachment.json";
    String streetLampLinkURL = "https://vorto.eclipse.org/";
    String streetLampLinkName = "Vorto";

    // creates the namespace as sysadmin
    createNamespaceSuccessfully(namespace, userSysadmin);

    // creates the collaborator payload to add userModelCreator to the namespace
    Collaborator userModelCreatorCollaborator = new Collaborator();
    userModelCreatorCollaborator.setAuthenticationProviderId(GITHUB);
    userModelCreatorCollaborator.setRoles(Arrays.asList("model_viewer", "model_creator"));
    userModelCreatorCollaborator.setTechnicalUser(false);
    userModelCreatorCollaborator.setUserId(USER_MODEL_CREATOR_NAME);

    // allows creator rights to userCreator on namespace
    repositoryServer
        .perform(
            put(String.format("/rest/namespaces/%s/users", namespace))
                .with(userSysadmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userModelCreatorCollaborator))
        )
        .andExpect(status().isOk());

    // creates the Zone model
    createModel(userModelCreator, zoneFileName, zoneModelID);

    // creates the Lamp model
    createModel(userModelCreator, lampFileName, lampModelID);

    // creates the Address model
    createModel(userModelCreator, addressFileName, addressModelID);

    // creates the StreetLamp model
    createModel(userModelCreator, streetLampFileName, streetLampModelID);

    // adds an attachment to the StreetLamp model (still requires sysadmin for this)
    addAttachment(streetLampModelID, userSysadmin, streetLampAttachmentFileName,
        MediaType.APPLICATION_JSON)
        .andExpect(status().isOk())
        .andExpect(content().json(
            objectMapper.writeValueAsString(
                AttachResult.success(
                    ModelId.fromPrettyFormat(streetLampModelID),
                    streetLampAttachmentFileName
                )
            )
            )
        );

    // adds a link to the StreetLamp model
    ModelLink link = new ModelLink(streetLampLinkURL, streetLampLinkName);
    addLink(streetLampModelID, userModelCreator, link);

    // saves a minimal mapping specification for the street lamp model
    Infomodel streetLampInfomodel = new Infomodel(ModelId.fromPrettyFormat(streetLampModelID));
    streetLampInfomodel.setTargetPlatformKey("myTargetPlatform");
    MappingSpecification mapping = new MappingSpecification(streetLampInfomodel);
    repositoryServer
        .perform(
            put(String.format("/rest/mappings/specifications/%s", streetLampModelID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mapping))
                .with(userModelCreator)
        )
        .andExpect(status().isOk());

    // expected ID of the payload mapping model
    String mappingId = String.format(
        "%s.%s:%sPayloadMapping:%s",
        // root namespace
        namespace,
        // virtual namespace for mapping
        streetLampModelName.toLowerCase(),
        // mapping name part 1 matching root model
        streetLampModelName,
        // root model version
        version
    );

    // fetches the full model for the UI
    repositoryServer
        .perform(
            get(String.format("/rest/models/ui/%s", streetLampModelID))
                .with(userModelCreator)
        )
        .andExpect(status().isOk())
        // cannot test full json due to arbitrary date offsets, so checking essentials in every node
        // prints some helpful representation of the result as received
        .andDo(
            mvcResult -> {
              ModelFullDetailsDTO output = objectMapper
                  .readValue(mvcResult.getResponse().getContentAsString(),
                      ModelFullDetailsDTO.class);
              LOGGER.info(
                  new StringBuilder("\nReceived response body:\n\n")
                      .append(
                          objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(output)
                      )
                      .toString()
              );
            }
        )
        // basic model info: checking some fields of id, type, author
        .andExpect(
            jsonPath("$.modelInfo").exists()
        )
        .andExpect(
            jsonPath("$.modelInfo.id.name").value(streetLampModelName)
        )
        .andExpect(
            jsonPath("$.modelInfo.id.namespace").value(namespace)
        )
        .andExpect(
            jsonPath("$.modelInfo.type").value("InformationModel")
        )
        .andExpect(
            jsonPath("$.modelInfo.author").value(USER_MODEL_CREATOR_NAME)
        )
        // mappings
        .andExpect(
            jsonPath("$.mappings").isNotEmpty()
        )
        // com.test.streetlamp:StreetLampPayloadMapping:1.0.0
        .andExpect(
            jsonPath("$.mappings[0].id").value(mappingId)
        )
        // references (only size)
        .andExpect(
            jsonPath("$.references", hasSize(2))
        )
        // referenced by
        .andExpect(
            jsonPath("$.referencedBy", hasSize(1))
        )
        .andExpect(
            jsonPath("$.referencedBy[0].id").value(mappingId)
        )
        // attachments: present. checking filename property of first and only attachment
        .andExpect(jsonPath("$.attachments").exists())
        .andExpect(
            jsonPath("$.attachments[0].filename").value(streetLampAttachmentFileName)
        )
        .andExpect(
            jsonPath("$.attachments[0].modelId.name").value(streetLampModelName)
        )
        // single link exists
        .andExpect(
            jsonPath("$.links").exists()
        )
        .andExpect(
            jsonPath("$.links[0].url").value(equalTo(link.getUrl()))
        )
        .andExpect(
            jsonPath("$.links[0].displayText").value(equalTo(link.getDisplayText()))
        )
        // actions: present but empty
        .andExpect(jsonPath("$.actions").exists())
        .andExpect(jsonPath("$.actions").isEmpty())
        // policies (only size)
        .andExpect(
            jsonPath("$.policies", hasSize(2))
        )
        // best policy: model creator with full access
        .andExpect(
            jsonPath("$.bestPolicy").exists()
        )
        .andExpect(
            jsonPath("$.bestPolicy.principalId").value("model_creator")
        )
        .andExpect(
            jsonPath("$.bestPolicy.permission").value(Permission.FULL_ACCESS.toString())
        )
        // syntax string equal to model content base-64-encoded
        .andExpect(
            jsonPath("$.encodedModelSyntax")
                .value(
                    Base64.getEncoder()
                        .encodeToString(createContentAsString(streetLampFileName).getBytes())
                )
        );

    // cleanup: deletes models in reverse order of creation, then namespace
    repositoryServer
        .perform(
            delete(String.format("/rest/models/%s", mappingId))
                .with(userModelCreator)
        )
        .andExpect(status().isOk());
    repositoryServer
        .perform(
            delete(String.format("/rest/models/%s", streetLampModelID))
                .with(userModelCreator)
        )
        .andExpect(status().isOk());
    repositoryServer
        .perform(
            delete(String.format("/rest/models/%s", lampModelID))
                .with(userModelCreator)
        )
        .andExpect(status().isOk());
    repositoryServer
        .perform(
            delete(String.format("/rest/models/%s", addressModelID))
                .with(userModelCreator)
        )
        .andExpect(status().isOk());
    repositoryServer
        .perform(
            delete(String.format("/rest/models/%s", zoneModelID))
                .with(userModelCreator)
        )
        .andExpect(status().isOk());
    repositoryServer
        .perform(
            delete(String.format("/rest/namespaces/%s", namespace))
                .with(userSysadmin)
        )
        .andExpect(status().isNoContent());
    // removes test-specific configuration
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);


  }

  /**
   * This verifies that an API call for a model is case-insensitive with regards to the part of its
   * ID that represents the namespace.<br/>
   * In other words, namespaces are case-insensitive both when manipulating the namespace itself,
   * and when using the namespace as part of an ID to represent a model.<br/>
   * Tests the REST call for the UI payload.
   *
   * @throws Exception
   * @see ModelControllerIntegrationTest#resolveModelWithCaseInsensitiveNamespace() for API V1 payloads.
   */
  @Test
  public void resolveModelWithCaseInsensitiveNamespace() throws Exception {
    String namespace = "com.Some_Other_Company.oFFICIA1";
    createNamespaceSuccessfully(namespace, userSysadmin);
    String id = String.format("%s.ModelIDCaseInsensitiveTest:1.0.0", namespace);
    createModel(
        userSysadmin,
        "ModelIDCaseInsensitiveTest.type",
        id
    );
    repositoryServer
        .perform(
            get(String.format("/rest/models/ui/%s", id))
                .with(userSysadmin)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.modelInfo.id.namespace", equalTo(namespace.toLowerCase())));

    // cleanup
    repositoryServer
        .perform(
            delete(String.format("/rest/models/%s", id))
                .with(userSysadmin)
        )
        .andExpect(status().isOk());

    repositoryServer
        .perform(
            delete(String.format("/rest/namespaces/%s", namespace))
                .with(userSysadmin)
        )
        .andExpect(status().isNoContent());
  }

  /**
   * Minimal coverage for model with malformed vortolang (missing a value for namespace). <br/>
   * The parsing behavior is slightly different between models created for integration tests and
   * models created in real life through REST/UI insofar as the parsing here fails early and no
   * list of {@link org.eclipse.emf.ecore.EObject} can be retrieved from the resource's contents
   * in the {@link org.eclipse.vorto.repository.core.impl.parser.AbstractModelParser} at test time,
   * meaning the failure to parse occurs earlier and displays a generic "Xtext cannot parse..."
   * message.<br/>
   * Conversely when saving a similar model in the UI, the parser will create the
   * {@link org.eclipse.emf.ecore.EObject}, but then populate the
   * {@link org.eclipse.vorto.core.api.model.model.Model} with {@code null} values when parsing
   * failed, which will require an additional validation and return more elaborate error messages.
   * <br/>
   * Bottomline, only checking for bad response status here.
   *
   * @throws Exception
   */
  @Test
  public void createModelMissingNamespace() throws Exception {
    // creates namespace
    createNamespaceSuccessfully("org.eclipse.vorto.examples.type", userSysadmin);
    // creates model with constraint

    repositoryServer
        .perform(post(
            "/rest/models/org.eclipse.vorto.examples.type.MissingNamespace:1.0.0/"
                + ModelType.fromFileName("MissingNamespace.type"))
            .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    repositoryServer
        .perform(
            put("/rest/models/org.eclipse.vorto.examples.type.MissingNamespace:1.0.0")
                .with(userSysadmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createContent("MissingNamespace.type")))
        .andExpect(status().isBadRequest());

    // cleans up
    repositoryServer
        .perform(
            delete(String.format("/rest/namespaces/%s", "org.eclipse.vorto.examples.type"))
                .with(userSysadmin)
        )
        .andExpect(status().isNoContent());
  }

  /**
   * See {@link ModelRepositoryControllerTest#createModelMissingNamespace()} for rationale on
   * testing strategy.
   *
   * @throws Exception
   */
  @Test
  public void createModelMissingVersion() throws Exception {
    // creates namespace
    createNamespaceSuccessfully("org.eclipse.vorto.examples.type", userSysadmin);
    // creates model with constraint

    repositoryServer
        .perform(post(
            "/rest/models/org.eclipse.vorto.examples.type.MissingVersion:1.0.0/"
                + ModelType.fromFileName("MissingVersion.type"))
            .with(userSysadmin).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    repositoryServer
        .perform(
            put("/rest/models/org.eclipse.vorto.examples.type.MissingVersion:1.0.0")
                .with(userSysadmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createContent("MissingVersion.type")))
        .andExpect(status().isBadRequest());

    // cleans up
    repositoryServer
        .perform(
            delete(String.format("/rest/namespaces/%s", "org.eclipse.vorto.examples.type"))
                .with(userSysadmin)
        )
        .andExpect(status().isNoContent());
  }

}
