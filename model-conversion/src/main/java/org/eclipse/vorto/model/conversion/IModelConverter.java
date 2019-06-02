/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.model.conversion;

import java.util.Optional;

/**
 * Converts between different Vorto model representations
 * 
 * @author Alexander Edelmann
 *
 * @param <Source>
 * @param <Target>
 */
public interface IModelConverter<Source, Target> {

	/**
	 * Converts the given source model to the target model
	 * 
	 * @param modelId
	 * @param platformKey
	 * @return
	 */
	Target convert(Source source, Optional<String> platformKey);
}
