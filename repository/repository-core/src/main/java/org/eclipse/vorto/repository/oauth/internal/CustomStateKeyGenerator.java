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
package org.eclipse.vorto.repository.oauth.internal;

import org.springframework.security.oauth2.client.filter.state.DefaultStateKeyGenerator;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;

public class CustomStateKeyGenerator extends DefaultStateKeyGenerator {

    private final RandomValueStringGenerator randomValueStringGenerator = new RandomValueStringGenerator(8);

    @Override
    public String generateKey(OAuth2ProtectedResourceDetails resource) {
        return randomValueStringGenerator.generate();
    }
}
