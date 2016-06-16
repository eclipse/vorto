/**
 */
package org.eclipse.vorto.core.api.model.datatype;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constraint Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.ConstraintRule#getConstraints <em>Constraints</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getConstraintRule()
 * @model
 * @generated
 */
public interface ConstraintRule extends EObject {
	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.datatype.Constraint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraints</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraints</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getConstraintRule_Constraints()
	 * @model containment="true"
	 * @generated
	 */
	EList<Constraint> getConstraints();

} // ConstraintRule
