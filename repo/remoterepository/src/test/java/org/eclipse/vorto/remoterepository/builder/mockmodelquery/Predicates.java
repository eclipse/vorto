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
package org.eclipse.vorto.remoterepository.builder.mockmodelquery;

import java.util.Collection;

import org.eclipse.vorto.remoterepository.model.ModelView;

public class Predicates {

	public static Predicate<ModelView> name(final String param) {
		return new Predicate<ModelView>() {
			public boolean apply(ModelView obj) {
				return obj.getModelId().getName().equals(param);
			}
		};
	}
	
	public static Predicate<ModelView> nameLike(final String param) {
		return new Predicate<ModelView>() {
			public boolean apply(ModelView obj) {
				return obj.getModelId().getName().contains(param);
			}
		};
	}
	
	public static Predicate<ModelView> namespace(final String param) {
		return new Predicate<ModelView>() {
			public boolean apply(ModelView obj) {
				return obj.getModelId().getNamespace().equals(param);
			}
		};
	}
	
	public static Predicate<ModelView> namespaceLike(final String param) {
		return new Predicate<ModelView>() {
			public boolean apply(ModelView obj) {
				return obj.getModelId().getNamespace().contains(param);
			}
		};
	}
	
	public static Predicate<ModelView> version(final String param) {
		return new Predicate<ModelView>() {
			public boolean apply(ModelView obj) {
				return obj.getModelId().getVersion().equals(param);
			}
		};
	}
	
	public static Predicate<ModelView> versionLike(final String param) {
		return new Predicate<ModelView>() {
			public boolean apply(ModelView obj) {
				return obj.getModelId().getVersion().contains(param);
			}
		};
	}
	
	public static Predicate<ModelView> and(final Collection<Predicate<ModelView>> predicates) {
		return new Predicate<ModelView>() {
			public boolean apply(final ModelView obj) {
				for(Predicate<ModelView> predicate : predicates) {
					if (!predicate.apply(obj)) {
						return false;
					}
				}
				return true;
			}
		};
	}
	
	public static Predicate<ModelView> or(final Collection<Predicate<ModelView>> predicates) {
		return new Predicate<ModelView>() {
			public boolean apply(final ModelView obj) {
				for(Predicate<ModelView> predicate : predicates) {
					if (predicate.apply(obj)) {
						return true;
					}
				}
				return false;
			}
		};
	}
	
	public static Predicate<ModelView> not(final Predicate<ModelView> predicate) {
		return new Predicate<ModelView>() {
			public boolean apply(final ModelView obj) {
				return !predicate.apply(obj);
			}
		};
	}

}
