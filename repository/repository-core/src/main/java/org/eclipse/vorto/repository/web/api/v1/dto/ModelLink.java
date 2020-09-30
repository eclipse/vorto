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

public class ModelLink {

    private String url;

    private String displayText;

    public ModelLink() {
        // default constructor for serialization.
    }

    public ModelLink(String url, String displayText) {
        this.url = url;
        this.displayText = displayText;
    }

    public String getUrl() {
        return url;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }
}
