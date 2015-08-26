/**
 */
package org.eclipse.vorto.core.api.model.mapping.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.vorto.core.api.model.mapping.EntityMapping;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Entity Mapping</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class EntityMappingImpl extends DataTypeMappingImpl implements EntityMapping {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EntityMappingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.ENTITY_MAPPING;
	}

} //EntityMappingImpl
