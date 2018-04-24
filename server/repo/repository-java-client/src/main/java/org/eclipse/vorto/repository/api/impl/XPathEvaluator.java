/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
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
