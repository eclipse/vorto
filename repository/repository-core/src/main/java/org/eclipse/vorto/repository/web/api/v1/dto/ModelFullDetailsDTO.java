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
package org.eclipse.vorto.repository.web.api.v1.dto;

import java.util.Collection;
import java.util.Comparator;
import org.eclipse.vorto.repository.core.Attachment;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.PolicyEntry;

/**
 * This DTO represents most of the details for a model, to be loaded by the UI in one REST call.
 * <br/>
 * It contains the following:
 * <ul>
 *   <li>
 *     The {@link ModelInfoDto} - see
 *     {@link org.eclipse.vorto.repository.web.api.v1.ModelController#getModelInfo(String)},
 *     all its references, and all the models referenced by this model.
 *   </li>
 *   <li>
 *     The model's mappings, represented as a map of {@link ModelMappingDTO} by platform key. <br/>
 *     The mappings were formerly retrieved by iterating over the parent's mapping reference (see
 *     {@link ModelInfo#getPlatformMappings()}) and invoking
 *     {@link org.eclipse.vorto.repository.web.api.v1.ModelController#getModelInfo(String)} on
 *     every single key, then returning the whole {@link ModelInfoDto} for each and trimming down
 *     to the required data (namely the model's ID and state, as well as the {@code targetPlatform}
 *     key). <br/>
 *     Those references are now fetched in the back-end without additional REST calls, and only the
 *     required data is sent to the front-end.<br/>
 *     Also worth noting that while {@link ModelInfo} maps platform keys by model ID,
 *     {@link ModelFullDetailsDTO#getMappings()} will return the inverted map.
 *   </li>
 *   <li>
 *     The model's references as a collection of {@link ModelInfo}, formerly loaded for each
 *     reference with a REST call to
 *     {@link org.eclipse.vorto.repository.web.api.v1.ModelController#getModelInfo(String)} by the
 *     front-end controller.
 *   </li>
 *   <li>
 *     The models referenced by this model, as a collection of {@link ModelInfo}, formerly loaded
 *     for each reference with a REST call to
 *     @link org.eclipse.vorto.repository.web.api.v1.ModelController#getModelInfo(String)} by the
 *     front-end controller.
 *   </li>
 *   <li>
 *     The model's {@link org.eclipse.vorto.repository.core.Attachment}s - see
 *     {@link org.eclipse.vorto.repository.web.api.v1.AttachmentController#getAttachments(String)}
 *   </li>
 *   <li>
 *     The model's web links as {@link ModelLink}s - see
 *     {@link org.eclipse.vorto.repository.web.api.v1.AttachmentController#getLinks(String)}
 *   </li>
 *   <li>
 *     The possible "workflow actions" for this specific model and the authenticated user - see
 *     {@link org.eclipse.vorto.repository.web.workflow.WorkflowController#getPossibleActions(String)}
 *   </li>
 *   <li>
 *     The {@link PolicyEntry} "policies" for the querying user's interaction with the model - see
 *     {@link org.eclipse.vorto.repository.web.core.ModelRepositoryController#getPolicies(String)}
 *     and {@link org.eclipse.vorto.repository.web.core.ModelRepositoryController#getUserPolicy(String)}
 *   </li>
 * </ul>
 * <br/>
 * It does <b>not</b> contain the following:
 * <ul>
 *  <li>
 *    The model images, which are loaded through the {@code ng-src} directive directly in the
 *    {@literal details-template.html} UI resource.
 *  </li>
 *  <li>
 *    The generators available, which are loaded independently from the model data (and are not
 *    bound to any specific model) by
 *    {@link org.eclipse.vorto.repository.web.api.v1.GenericGeneratorController#getRegisteredGeneratorServices(String)}
 *  </li>
 * </ul>
 */
public class ModelFullDetailsDTO {

  private ModelInfo modelInfo;
  private Collection<ModelMinimalInfoDTO> mappings;
  private Collection<ModelMinimalInfoDTO> references;
  private Collection<ModelMinimalInfoDTO> referencedBy;
  private Collection<Attachment> attachments;
  private Collection<ModelLink> links;
  private Collection<String> actions;
  private Collection<PolicyEntry> policies;
  private PolicyEntry bestPolicy;
  private String encodedModelSyntax;

  public ModelFullDetailsDTO withModelInfo(ModelInfo modelInfo) {
    this.modelInfo = modelInfo;
    return this;
  }

  public ModelFullDetailsDTO withMappings(Collection<ModelMinimalInfoDTO> mappings) {
    this.mappings = mappings;
    return this;
  }

  public ModelFullDetailsDTO withReferences(Collection<ModelMinimalInfoDTO> references) {
    this.references = references;
    return this;
  }

  public ModelFullDetailsDTO withReferencedBy(Collection<ModelMinimalInfoDTO> referencedBy) {
    this.referencedBy = referencedBy;
    return this;
  }

  public ModelFullDetailsDTO withAttachments(Collection<Attachment> attachments) {
    this.attachments = attachments;
    return this;
  }

  public ModelFullDetailsDTO withLinks(Collection<ModelLink> links) {
    this.links = links;
    return this;
  }

  public ModelFullDetailsDTO withActions(Collection<String> actions) {
    this.actions = actions;
    return this;
  }

  public ModelFullDetailsDTO withPolicies(Collection<PolicyEntry> policies) {
    this.policies = policies;
    this.bestPolicy = getBestPolicyEntryForUser();
    return this;
  }

  public ModelFullDetailsDTO withEncodedModelSyntax(String encodedModelSyntax) {
    this.encodedModelSyntax = encodedModelSyntax;
    return this;
  }

  public ModelInfo getModelInfo() {
    return modelInfo;
  }

  public void setModelInfo(ModelInfo modelInfo) {
    this.modelInfo = modelInfo;
  }

  public Collection<ModelMinimalInfoDTO> getMappings() {
    return mappings;
  }

  public void setMappings(
      Collection<ModelMinimalInfoDTO> mappings) {
    this.mappings = mappings;
  }

  public Collection<ModelMinimalInfoDTO> getReferences() {
    return references;
  }

  public void setReferences(Collection<ModelMinimalInfoDTO> references) {
    this.references = references;
  }

  public Collection<ModelMinimalInfoDTO> getReferencedBy() {
    return referencedBy;
  }

  public void setReferencedBy(
      Collection<ModelMinimalInfoDTO> referencedBy) {
    this.referencedBy = referencedBy;
  }

  public Collection<Attachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(Collection<Attachment> attachments) {
    this.attachments = attachments;
  }

  public Collection<ModelLink> getLinks() {
    return links;
  }

  public void setLinks(Collection<ModelLink> links) {
    this.links = links;
  }

  public Collection<String> getActions() {
    return actions;
  }

  public void setActions(Collection<String> actions) {
    this.actions = actions;
  }

  public Collection<PolicyEntry> getPolicies() {
    return policies;
  }

  public void setPolicies(Collection<PolicyEntry> policies) {
    this.policies = policies;
    this.bestPolicy = getBestPolicyEntryForUser();
  }

  public PolicyEntry getBestPolicy() {
    return bestPolicy;
  }

  public void setBestPolicy(PolicyEntry bestPolicy) {
    this.bestPolicy = bestPolicy;
  }

  private PolicyEntry getBestPolicyEntryForUser() {
    return policies
        .stream()
        .sorted(Comparator.comparing(p -> p.getPermission().ordinal()))
        .findFirst()
        .orElse(null);
  }

  public String getEncodedModelSyntax() {
    return encodedModelSyntax;
  }

  public void setEncodedModelSyntax(String encodedModelSyntax) {
    this.encodedModelSyntax = encodedModelSyntax;
  }
}
