/**
 */
package org.eclipse.vorto.core.api.model.mapping;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Source</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EnumSource#getModel <em>Model</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEnumSource()
 * @model
 * @generated
 */
public interface EnumSource extends Source {

	/**
	 * Returns the value of the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' reference.
	 * @see #setModel(org.eclipse.vorto.core.api.model.datatype.Enum)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEnumSource_Model()
	 * @model
	 * @generated
	 */
	org.eclipse.vorto.core.api.model.datatype.Enum getModel();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.EnumSource#getModel <em>Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' reference.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(org.eclipse.vorto.core.api.model.datatype.Enum value);
} // EnumSource
