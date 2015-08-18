/**
 */
package org.eclipse.vorto.core.api.model.mapping;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Block Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockReference#getReference <em>Reference</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFunctionBlockReference()
 * @model
 * @generated
 */
public interface FunctionBlockReference extends InfoModelTargetElement {
	/**
	 * Returns the value of the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference</em>' reference.
	 * @see #setReference(FunctionBlockMapping)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFunctionBlockReference_Reference()
	 * @model
	 * @generated
	 */
	FunctionBlockMapping getReference();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockReference#getReference <em>Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference</em>' reference.
	 * @see #getReference()
	 * @generated
	 */
	void setReference(FunctionBlockMapping value);

} // FunctionBlockReference
