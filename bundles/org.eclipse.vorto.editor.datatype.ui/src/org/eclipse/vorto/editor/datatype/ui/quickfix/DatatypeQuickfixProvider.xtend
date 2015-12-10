/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/

package org.eclipse.vorto.editor.datatype.ui.quickfix

import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider

/**
 * Custom quickfixes.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#quickfixes
 */
class DatatypeQuickfixProvider extends DefaultQuickfixProvider {

//	@Fix("unresolvable.reference")
//	def createProjectReference(Issue issue, IssueResolutionAcceptor acceptor) {
//
//		acceptor.accept(
//			issue,
//			"Create link", // label
//			"Create link", // description
//			"createLink.png", // icon 
//			new ISemanticModification() {
//				def override apply(EObject element, IModificationContext context) {
//					var xtextDocument = context.getXtextDocument();
//					var referencedNamespace = xtextDocument.get(issue.getOffset(), issue.getLength());
//					var namespace = referencedNamespace.substring(0,referencedNamespace.lastIndexOf(".")-1);
//					var name = referencedNamespace.substring(referencedNamespace.lastIndexOf("."+1));
//					var modelProject = ModelProjectServiceFactory.getDefault().getProjectByModelId(new ModelId(ModelType.Datatype,name,namespace,"1.0.0"))
//					if (modelProject != null) {
//						ModelProjectServiceFactory.getDefault().projectFromSelection.addReference(modelProject);
//						
//					}
//				}
//			}
//		);
//	}
}
