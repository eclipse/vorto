/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.tutorial.tasks;

import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.codegen.examples.tutorial.tasks.templates.LWM2MResourceEnumTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.model.IMapping;

public class LWM2MResourceEnumGenerateTask extends AbstractMappingAwareGeneratorTask{

	/**
	 * @param mapping
	 */
	public LWM2MResourceEnumGenerateTask(IMapping mapping) {
		super(mapping);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.vorto.codegen.api.tasks.AbstractTemplateGeneratorTask#getFileName(java.lang.Object)
	 */
	@Override
	public String getFileName(InformationModel ctx) {
		return "LWM2MResourceEnumTemplate_generated.xml";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.vorto.codegen.api.tasks.AbstractTemplateGeneratorTask#getTemplate()
	 */
	@Override
	public ITemplate<InformationModel> getTemplate() {
		return new LWM2MResourceEnumTemplate(this.getMapping());
	}

}
