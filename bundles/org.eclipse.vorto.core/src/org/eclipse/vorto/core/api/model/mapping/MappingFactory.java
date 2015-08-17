/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage
 * @generated
 */
public interface MappingFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MappingFactory eINSTANCE = org.eclipse.vorto.core.api.model.mapping.impl.MappingFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	MappingModel createMappingModel();

	/**
	 * Returns a new object of class '<em>Info Model Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Info Model Mapping Rule</em>'.
	 * @generated
	 */
	InfoModelMappingRule createInfoModelMappingRule();

	/**
	 * Returns a new object of class '<em>Info Model Source Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Info Model Source Element</em>'.
	 * @generated
	 */
	InfoModelSourceElement createInfoModelSourceElement();

	/**
	 * Returns a new object of class '<em>Info Model Child</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Info Model Child</em>'.
	 * @generated
	 */
	InfoModelChild createInfoModelChild();

	/**
	 * Returns a new object of class '<em>Info Model Fb Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Info Model Fb Element</em>'.
	 * @generated
	 */
	InfoModelFbElement createInfoModelFbElement();

	/**
	 * Returns a new object of class '<em>Information Model Property</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Information Model Property</em>'.
	 * @generated
	 */
	InformationModelProperty createInformationModelProperty();

	/**
	 * Returns a new object of class '<em>Function Block Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Function Block Mapping Rule</em>'.
	 * @generated
	 */
	FunctionBlockMappingRule createFunctionBlockMappingRule();

	/**
	 * Returns a new object of class '<em>Function Block Source Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Function Block Source Element</em>'.
	 * @generated
	 */
	FunctionBlockSourceElement createFunctionBlockSourceElement();

	/**
	 * Returns a new object of class '<em>Function Block Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Function Block Element</em>'.
	 * @generated
	 */
	FunctionBlockElement createFunctionBlockElement();

	/**
	 * Returns a new object of class '<em>Function Block Element Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Function Block Element Attribute</em>'.
	 * @generated
	 */
	FunctionBlockElementAttribute createFunctionBlockElementAttribute();

	/**
	 * Returns a new object of class '<em>Function Block Child Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Function Block Child Element</em>'.
	 * @generated
	 */
	FunctionBlockChildElement createFunctionBlockChildElement();

	/**
	 * Returns a new object of class '<em>Operation Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Operation Element</em>'.
	 * @generated
	 */
	OperationElement createOperationElement();

	/**
	 * Returns a new object of class '<em>Configuration Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Configuration Element</em>'.
	 * @generated
	 */
	ConfigurationElement createConfigurationElement();

	/**
	 * Returns a new object of class '<em>Status Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Status Element</em>'.
	 * @generated
	 */
	StatusElement createStatusElement();

	/**
	 * Returns a new object of class '<em>Fault Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fault Element</em>'.
	 * @generated
	 */
	FaultElement createFaultElement();

	/**
	 * Returns a new object of class '<em>Event Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Event Element</em>'.
	 * @generated
	 */
	EventElement createEventElement();

	/**
	 * Returns a new object of class '<em>FB Type Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>FB Type Element</em>'.
	 * @generated
	 */
	FBTypeElement createFBTypeElement();

	/**
	 * Returns a new object of class '<em>FB Type Element Child</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>FB Type Element Child</em>'.
	 * @generated
	 */
	FBTypeElementChild createFBTypeElementChild();

	/**
	 * Returns a new object of class '<em>FB Type Property</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>FB Type Property</em>'.
	 * @generated
	 */
	FBTypeProperty createFBTypeProperty();

	/**
	 * Returns a new object of class '<em>Data Type Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Data Type Mapping Rule</em>'.
	 * @generated
	 */
	DataTypeMappingRule createDataTypeMappingRule();

	/**
	 * Returns a new object of class '<em>Data Type Source Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Data Type Source Element</em>'.
	 * @generated
	 */
	DataTypeSourceElement createDataTypeSourceElement();

	/**
	 * Returns a new object of class '<em>Data Type Property Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Data Type Property Element</em>'.
	 * @generated
	 */
	DataTypePropertyElement createDataTypePropertyElement();

	/**
	 * Returns a new object of class '<em>Entity Expression Ref</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entity Expression Ref</em>'.
	 * @generated
	 */
	EntityExpressionRef createEntityExpressionRef();

	/**
	 * Returns a new object of class '<em>Data Type Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Data Type Attribute</em>'.
	 * @generated
	 */
	DataTypeAttribute createDataTypeAttribute();

	/**
	 * Returns a new object of class '<em>Target Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Target Element</em>'.
	 * @generated
	 */
	TargetElement createTargetElement();

	/**
	 * Returns a new object of class '<em>Stereo Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Stereo Type</em>'.
	 * @generated
	 */
	StereoType createStereoType();

	/**
	 * Returns a new object of class '<em>Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attribute</em>'.
	 * @generated
	 */
	Attribute createAttribute();

	/**
	 * Returns a new object of class '<em>Nested Entity Expression</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Nested Entity Expression</em>'.
	 * @generated
	 */
	NestedEntityExpression createNestedEntityExpression();

	/**
	 * Returns a new object of class '<em>Entity Expression</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entity Expression</em>'.
	 * @generated
	 */
	EntityExpression createEntityExpression();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MappingPackage getMappingPackage();

} //MappingFactory
