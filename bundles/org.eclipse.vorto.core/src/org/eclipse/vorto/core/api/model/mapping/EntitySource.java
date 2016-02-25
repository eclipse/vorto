/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.vorto.core.api.model.datatype.Entity;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity Source</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EntitySource#getModel <em>Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEntitySource()
 * @model
 * @generated
 */
public interface EntitySource extends Source {
	/**
	 * Returns the value of the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' reference.
	 * @see #setModel(Entity)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEntitySource_Model()
	 * @model
	 * @generated
	 */
	Entity getModel();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.EntitySource#getModel <em>Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' reference.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(Entity value);

} // EntitySource
