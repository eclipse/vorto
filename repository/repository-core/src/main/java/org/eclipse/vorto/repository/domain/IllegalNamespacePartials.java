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
package org.eclipse.vorto.repository.domain;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class IllegalNamespacePartials {

    /**
     * This list must be equal to the keywords defined in NONREUSABLEKEYORD in
     * vorto/core-bundles/language/org.eclipse.vorto.editor.datatype/src/org/eclipse/vorto/editor/datatype/Datatype.xtext
     */
    public static final List<String> PARTIALS = ImmutableList.of(
        "functionblocks",
        "infomodel",
        "mandatory",
        "namespace",
        "optional",
        "version",
        "dictionary",
        "extension",
        "functionblock"
    );

    private IllegalNamespacePartials() {
        // not meant to be instantiated
    }

}
