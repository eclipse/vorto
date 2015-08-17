/**
 */
package org.eclipse.vorto.core.api.model.mapping;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Block Element Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockElementAttribute#getAttribute <em>Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFunctionBlockElementAttribute()
 * @model
 * @generated
 */
public interface FunctionBlockElementAttribute extends FunctionBlockElement {
	/**
	 * Returns the value of the '<em><b>Attribute</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attribute</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute
	 * @see #setAttribute(FunctionblockModelAttribute)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFunctionBlockElementAttribute_Attribute()
	 * @model
	 * @generated
	 */
	FunctionblockModelAttribute getAttribute();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockElementAttribute#getAttribute <em>Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attribute</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute
	 * @see #getAttribute()
	 * @generated
	 */
	void setAttribute(FunctionblockModelAttribute value);

} // FunctionBlockElementAttribute
