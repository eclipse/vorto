package org.eclipse.vorto.perspective.labelprovider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.model.ModelType;

public class ModelRepositoryLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	private static final String SHARED_DT = "shared_dt.png";
	private static final String SHARED_FB = "shared_fb.png";
	private static final String SHARED_IM = "shared_im.png";
	private static final String BUNDLE_ICON_URL = "platform:/plugin/org.eclipse.vorto.perspective/icons/";
	private Map<ModelType, Image> modelImageMap = getImageMap();

	public String getColumnText(Object obj, int index) {
		ModelResource resource = (ModelResource) obj;
		switch (index) {
		case 0:
			return "";
		case 1:
			return resource.getId().getModelType().name();
		case 2:
			return resource.getId().getNamespace();
		case 3:
			return resource.getId().getName();
		case 4:
			return resource.getId().getVersion();
		default:
			return "";
		}
	}

	public Image getColumnImage(Object obj, int index) {
		if (index == 0) {
			ModelResource resource = (ModelResource) obj;
			return modelImageMap.get(resource.getId().getModelType());
		}
		return null;
	}

	public Image getImage(Object obj) {
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_OBJ_ELEMENT);
	}

	private Map<ModelType, Image> getImageMap() {
		Map<ModelType, Image> imageMap = new HashMap<ModelType, Image>();
		imageMap.put(ModelType.INFORMATIONMODEL, getImage(SHARED_IM));
		imageMap.put(ModelType.FUNCTIONBLOCK, getImage(SHARED_FB));
		imageMap.put(ModelType.DATATYPE, getImage(SHARED_DT));
		return imageMap;
	}

	private Image getImage(String imageFileName) {
		URL url = null;
		try {
			url = new URL(BUNDLE_ICON_URL + imageFileName);
		} catch (MalformedURLException e) {
			throw new RuntimeException(
					"URL to datatype model image is not correct!", e);
		}
		return ImageDescriptor.createFromURL(url).createImage();
	}

}
