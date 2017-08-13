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
