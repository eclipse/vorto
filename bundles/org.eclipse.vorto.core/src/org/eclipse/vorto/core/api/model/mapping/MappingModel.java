/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.vorto.core.api.model.model.Model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getMappingType <em>Mapping Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel()
 * @model
 * @generated
 */
public interface MappingModel extends Model {
	/**
	 * Returns the value of the '<em><b>Mapping Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mapping Type</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mapping Type</em>' containment reference.
	 * @see #setMappingType(MappingType)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel_MappingType()
	 * @model containment="true"
	 * @generated
	 */
	MappingType getMappingType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getMappingType <em>Mapping Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mapping Type</em>' containment reference.
	 * @see #getMappingType()
	 * @generated
	 */
	void setMappingType(MappingType value);

} // MappingModel
