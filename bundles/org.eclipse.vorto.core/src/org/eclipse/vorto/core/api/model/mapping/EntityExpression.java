/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.vorto.core.api.model.datatype.Type;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EntityExpression#getEntity <em>Entity</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEntityExpression()
 * @model
 * @generated
 */
public interface EntityExpression extends EntityExpressionRef {
	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(Type)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEntityExpression_Entity()
	 * @model
	 * @generated
	 */
	Type getEntity();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.EntityExpression#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(Type value);

} // EntityExpression
