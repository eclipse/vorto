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
package org.eclipse.vorto.repository.conversion;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class ModelInfoRetrieverTask extends RecursiveTask<List<ModelInfo>> {

    private static final long serialVersionUID = 3056763240970631787L;
    private final List<ModelInfo> modelInfos = new ArrayList<>();
    private final ModelId rootID;
    private final IModelRepositoryFactory factory;
    private final SecurityContext securityContext;
    private final RequestAttributes requestAttributes;

    public ModelInfoRetrieverTask(RequestAttributes requestAttributes,
        SecurityContext securityContext, IModelRepositoryFactory factory, ModelId rootID) {
        this.requestAttributes = requestAttributes;
        RequestContextHolder.setRequestAttributes(requestAttributes);
        this.securityContext = securityContext;
        SecurityContextHolder.setContext(securityContext);
        this.factory = factory;
        this.rootID = rootID;
    }

    @Override
    protected List<ModelInfo> compute() {
        RequestContextHolder.setRequestAttributes(requestAttributes, true);
        SecurityContextHolder.setContext(securityContext);
        ModelInfo modelInfo =
            factory.getRepositoryByModelWithoutSessionHelper(rootID).getById(rootID);
        if (Objects.nonNull(modelInfo)) {
            modelInfos.add(modelInfo);
            if (!modelInfo.getReferences().isEmpty()) {
                modelInfos.addAll(processChildren(modelInfo));
            }
        }
        return modelInfos;
    }

    private List<ModelInfo> processChildren(ModelInfo modelInfo) {
        return ForkJoinTask.invokeAll(children(modelInfo)).stream().map(ForkJoinTask::join)
            .flatMap(Collection::stream).collect(Collectors.toList());
    }

    private Collection<ModelInfoRetrieverTask> children(ModelInfo root) {
        return root.getReferences().stream()
            .map(r -> new ModelInfoRetrieverTask(RequestContextHolder.getRequestAttributes(),
                SecurityContextHolder.getContext(), factory, r)).collect(
                Collectors.toList());
    }

}
