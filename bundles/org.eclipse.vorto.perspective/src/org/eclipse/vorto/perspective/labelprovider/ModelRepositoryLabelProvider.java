package org.eclipse.vorto.perspective.labelprovider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.perspective.util.ImageUtil;

public class ModelRepositoryLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	public String getColumnText(Object obj, int index) {
		ModelResource resource = (ModelResource) obj;
		switch (index) {
		case 0:
			return "";
		case 1:
			return resource.getId().getNamespace();
		case 2:
			return resource.getId().getName();
		case 3:
			return resource.getId().getVersion();
		case 4:
			return resource.getDescription();
		default:
			return "";
		}
	}

	public Image getColumnImage(Object obj, int index) {
		if (index == 0) {
			ModelResource resource = (ModelResource) obj;
			return ImageUtil.getImageForModelType(resource.getId().getModelType());
		}
		return null;
	}

	public Image getImage(Object obj) {
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_OBJ_ELEMENT);
	}
}
