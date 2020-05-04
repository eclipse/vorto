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
