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
package org.eclipse.vorto.core.ui.mapping;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;

public class StereoTypeHelper {
	
	/**
	 * Get StereoType of given name from given List
	 * @param stereoTypes
	 * @param stereoTypeName
	 * @return StereoType with given name, or null if Stereo type of given name
	 *         does not exist
	 */
	public static StereoTypeTarget getStereoType(List<StereoTypeTarget> stereoTypes, String stereoTypeName) {
		for (StereoTypeTarget stereoType : stereoTypes) {
			if (StringUtils.equals(stereoType.getName(), stereoTypeName)) {
				return stereoType;
			}
		}
		return null;
	}

	/**
	 * Test of given StereoTypes contains given stereoTypeName
	 * @param stereoTypes
	 * @param stereoTypeName
	 * @return: true if given StereoTypes contains given stereoTypeName, false otherwise
	 */
	public static boolean containsStereoType(List<StereoTypeTarget> stereoTypes, String stereoTypeName) {
		StereoTypeTarget stereoType = getStereoType(stereoTypes, stereoTypeName);
		return stereoType == null ? false : true;
	}

	/**
	 * Get Attribute of given attributeName
	 * 
	 * @param stereoType
	 * @param attributeName
	 * @return Attribute of given attributeName, or NullAttribute if not
	 *         attribute with given name does not exist
	 */
	public static Attribute getAttribute(StereoTypeTarget stereoType, String attributeName) {
		if (stereoType == null) {
			return NullAttribute.INSTANCE;
		}

		for (Attribute attribute : stereoType.getAttributes()) {
			if (attribute.getName().equals(attributeName)) {
				return attribute;
			}
		}
		return NullAttribute.INSTANCE;
	}
}
