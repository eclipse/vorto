/**
 */
package org.eclipse.vorto.core.api.model.datatype;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.Enum#getEnums <em>Enums</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getEnum()
 * @model
 * @generated
 */
public interface Enum extends Type {
	/**
	 * Returns the value of the '<em><b>Enums</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.datatype.EnumLiteral}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Enums</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Enums</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getEnum_Enums()
	 * @model containment="true"
	 * @generated
	 */
	EList<EnumLiteral> getEnums();

} // Enum
