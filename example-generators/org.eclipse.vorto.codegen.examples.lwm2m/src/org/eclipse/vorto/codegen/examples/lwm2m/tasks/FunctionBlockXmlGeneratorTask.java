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
package org.eclipse.vorto.codegen.examples.lwm2m.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.examples.lwm2m.templates.FunctionBlockXmlTemplate;
import org.eclipse.vorto.codegen.examples.lwm2m.utils.ModuleUtil;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

/**
 * Task class to generate LWM2M XML objects from Vorto Function Blocks.
 *
 */
public class FunctionBlockXmlGeneratorTask extends AbstractTemplateGeneratorTask<FunctionblockModel> {

	@Override
   public String getFileName( final FunctionblockModel model ) {
      return model.getName() + ".xml";
   }

   @Override
   public String getPath( final FunctionblockModel model ) {
      return ModuleUtil.getLwm2mObjectsPath();
   }

   @Override
   public ITemplate<FunctionblockModel> getTemplate() {
      return new FunctionBlockXmlTemplate();
   }

}
