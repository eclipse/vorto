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
@Deprecated
public class JxPathContextFactory {
		
	private static JxPathContextFactory inst = new JxPathContextFactory();
	
	private JxPathContextFactory() {}
	
	public static JxPathContextFactory getInstance() {
		return inst;
	}
	
	private JXPathContext getSharedContext() {
		JXPathContext context = JXPathContext.newContext(null);
		context.setLenient(true);		       
        return context;
	}
	
    public JXPathContext newContext(Object ctxObject) {
    	return JXPathContext.newContext(getSharedContext(),ctxObject);
    }
    
    
}

/* EOF */
