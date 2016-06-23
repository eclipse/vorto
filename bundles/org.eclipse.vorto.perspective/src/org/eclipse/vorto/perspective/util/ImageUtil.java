/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.perspective.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.vorto.core.api.model.model.ModelType;

public class ImageUtil {
	private static final String BUNDLE_ICON_URL = "platform:/plugin/org.eclipse.vorto.core.ui/icons/";
	private static final String SHARED_DT = "dt.png";
	private static final String SHARED_FB = "fb.png";
	private static final String SHARED_IM = "im.png";
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
