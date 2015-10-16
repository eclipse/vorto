package org.eclipse.vorto.perspective.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.vorto.core.model.ModelType;

public class ImageUtil {
	private static final String BUNDLE_ICON_URL = "platform:/plugin/org.eclipse.vorto.core/icons/";
	private static final String SHARED_DT = "shared_dt.png";
	private static final String SHARED_FB = "shared_fb.png";
	private static final String SHARED_IM = "shared_im.png";
	private static Map<ModelType, Image> modelImageMap = getImageMap(); 
	private static Map<ModelType, Image> getImageMap() {
		Map<ModelType, Image> imageMap = new HashMap<ModelType, Image>();
		imageMap.put(ModelType.InformationModel, getImage(SHARED_IM));
		imageMap.put(ModelType.Functionblock, getImage(SHARED_FB));
		imageMap.put(ModelType.Datatype, getImage(SHARED_DT));
		return imageMap;
	}
	
	public static Image getImage(String imageFileName) {
		URL url = null;
		try {
			url = new URL(BUNDLE_ICON_URL + imageFileName);
		} catch (MalformedURLException e) {
			throw new RuntimeException(
					"URL to datatype model image is not correct!", e);
		}
		return ImageDescriptor.createFromURL(url).createImage();
	}
	
	public static Image getImageForModelType(ModelType modelType) {
		return modelImageMap.get(modelType);
	}
}
