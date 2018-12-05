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
package org.eclipse.vorto.repository.api.impl;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;

public class XPathEvaluator {

    public Object eval(String expression, Object ctx) {
      
        try {
        	return newContext(ctx).selectNodes(expression);
        } catch (JXPathException e) {
            throw new RuntimeException("Problem evaluating xpath query",e);
        }

    }

    private JXPathContext newContext(Object context) {
        JXPathContext jXpathContext = JxPathContextFactory.getInstance().newContext(context);
        return jXpathContext;
    }
}

/* EOF */
