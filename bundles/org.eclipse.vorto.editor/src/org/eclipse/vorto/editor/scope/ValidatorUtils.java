package org.eclipse.vorto.editor.scope;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.xtext.EcoreUtil2;

public class ValidatorUtils {
	
	public static final String UNRESOLVABLE_REFERENCE = "unresolvable.reference";

	public static boolean modelReferenceResolvable(ModelReference modelReference) {
		boolean resolved = false;
		EcoreUtil2.resolveAll(modelReference.eResource().getContents().get(0)); 
		for (Resource resource : modelReference.eResource().getResourceSet().getResources()) {
			if (resource.getContents().get(0) instanceof Model) {
				Model model = (Model)resource.getContents().get(0);
				String importedNamespaceRef = model.getNamespace()+"."+model.getName();
				if (modelReference.getImportedNamespace().equals(importedNamespaceRef) && model.getVersion().equals(modelReference.getVersion())) {
					resolved = true;
				}
			}
		}
			
		return resolved;
	}
}
