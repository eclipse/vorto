/**
 */
package org.eclipse.vorto.core.api.model.mapping.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.vorto.core.api.model.mapping.*;

import org.eclipse.vorto.core.api.model.model.Model;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage
 * @generated
 */
public class MappingSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MappingPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingSwitch() {
		if (modelPackage == null) {
			modelPackage = MappingPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case MappingPackage.MAPPING_MODEL: {
				MappingModel mappingModel = (MappingModel)theEObject;
				T result = caseMappingModel(mappingModel);
				if (result == null) result = caseModel(mappingModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.MAPPING: {
				Mapping mapping = (Mapping)theEObject;
				T result = caseMapping(mapping);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.INFO_MODEL_MAPPING: {
				InfoModelMapping infoModelMapping = (InfoModelMapping)theEObject;
				T result = caseInfoModelMapping(infoModelMapping);
				if (result == null) result = caseMapping(infoModelMapping);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.INFO_MODEL_MAPPING_RULE: {
				InfoModelMappingRule infoModelMappingRule = (InfoModelMappingRule)theEObject;
				T result = caseInfoModelMappingRule(infoModelMappingRule);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.INFO_MODEL_TARGET_ELEMENT: {
				InfoModelTargetElement infoModelTargetElement = (InfoModelTargetElement)theEObject;
				T result = caseInfoModelTargetElement(infoModelTargetElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.INFO_MODEL_SOURCE_ELEMENT: {
				InfoModelSourceElement infoModelSourceElement = (InfoModelSourceElement)theEObject;
				T result = caseInfoModelSourceElement(infoModelSourceElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.INFO_MODEL_CHILD: {
				InfoModelChild infoModelChild = (InfoModelChild)theEObject;
				T result = caseInfoModelChild(infoModelChild);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.INFO_MODEL_FB_ELEMENT: {
				InfoModelFbElement infoModelFbElement = (InfoModelFbElement)theEObject;
				T result = caseInfoModelFbElement(infoModelFbElement);
				if (result == null) result = caseInfoModelChild(infoModelFbElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.INFORMATION_MODEL_ATTRIBUTE: {
				InformationModelAttribute informationModelAttribute = (InformationModelAttribute)theEObject;
				T result = caseInformationModelAttribute(informationModelAttribute);
				if (result == null) result = caseInfoModelChild(informationModelAttribute);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FUNCTION_BLOCK_MAPPING: {
				FunctionBlockMapping functionBlockMapping = (FunctionBlockMapping)theEObject;
				T result = caseFunctionBlockMapping(functionBlockMapping);
				if (result == null) result = caseMapping(functionBlockMapping);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FUNCTION_BLOCK_MAPPING_RULE: {
				FunctionBlockMappingRule functionBlockMappingRule = (FunctionBlockMappingRule)theEObject;
				T result = caseFunctionBlockMappingRule(functionBlockMappingRule);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FUNCTION_BLOCK_TARGET_ELEMENT: {
				FunctionBlockTargetElement functionBlockTargetElement = (FunctionBlockTargetElement)theEObject;
				T result = caseFunctionBlockTargetElement(functionBlockTargetElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FUNCTION_BLOCK_SOURCE_ELEMENT: {
				FunctionBlockSourceElement functionBlockSourceElement = (FunctionBlockSourceElement)theEObject;
				T result = caseFunctionBlockSourceElement(functionBlockSourceElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FUNCTION_BLOCK_ELEMENT: {
				FunctionBlockElement functionBlockElement = (FunctionBlockElement)theEObject;
				T result = caseFunctionBlockElement(functionBlockElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FUNCTION_BLOCK_ELEMENT_ATTRIBUTE: {
				FunctionBlockElementAttribute functionBlockElementAttribute = (FunctionBlockElementAttribute)theEObject;
				T result = caseFunctionBlockElementAttribute(functionBlockElementAttribute);
				if (result == null) result = caseFunctionBlockElement(functionBlockElementAttribute);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FUNCTION_BLOCK_CHILD_ELEMENT: {
				FunctionBlockChildElement functionBlockChildElement = (FunctionBlockChildElement)theEObject;
				T result = caseFunctionBlockChildElement(functionBlockChildElement);
				if (result == null) result = caseFunctionBlockElement(functionBlockChildElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.OPERATION_ELEMENT: {
				OperationElement operationElement = (OperationElement)theEObject;
				T result = caseOperationElement(operationElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.CONFIGURATION_ELEMENT: {
				ConfigurationElement configurationElement = (ConfigurationElement)theEObject;
				T result = caseConfigurationElement(configurationElement);
				if (result == null) result = caseFunctionBlockChildElement(configurationElement);
				if (result == null) result = caseFunctionBlockElement(configurationElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.STATUS_ELEMENT: {
				StatusElement statusElement = (StatusElement)theEObject;
				T result = caseStatusElement(statusElement);
				if (result == null) result = caseFunctionBlockChildElement(statusElement);
				if (result == null) result = caseFunctionBlockElement(statusElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FAULT_ELEMENT: {
				FaultElement faultElement = (FaultElement)theEObject;
				T result = caseFaultElement(faultElement);
				if (result == null) result = caseFunctionBlockChildElement(faultElement);
				if (result == null) result = caseFunctionBlockElement(faultElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.EVENT_ELEMENT: {
				EventElement eventElement = (EventElement)theEObject;
				T result = caseEventElement(eventElement);
				if (result == null) result = caseFunctionBlockChildElement(eventElement);
				if (result == null) result = caseFunctionBlockElement(eventElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FB_TYPE_ELEMENT: {
				FBTypeElement fbTypeElement = (FBTypeElement)theEObject;
				T result = caseFBTypeElement(fbTypeElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENTITY_MAPPING: {
				EntityMapping entityMapping = (EntityMapping)theEObject;
				T result = caseEntityMapping(entityMapping);
				if (result == null) result = caseDataTypeMapping(entityMapping);
				if (result == null) result = caseMapping(entityMapping);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENTITY_MAPPING_RULE: {
				EntityMappingRule entityMappingRule = (EntityMappingRule)theEObject;
				T result = caseEntityMappingRule(entityMappingRule);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENTITY_TARGET_ELEMENT: {
				EntityTargetElement entityTargetElement = (EntityTargetElement)theEObject;
				T result = caseEntityTargetElement(entityTargetElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENTITY_SOURCE_ELEMENT: {
				EntitySourceElement entitySourceElement = (EntitySourceElement)theEObject;
				T result = caseEntitySourceElement(entitySourceElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENTITY_ATTRIBUTE_ELEMENT: {
				EntityAttributeElement entityAttributeElement = (EntityAttributeElement)theEObject;
				T result = caseEntityAttributeElement(entityAttributeElement);
				if (result == null) result = caseEntitySourceElement(entityAttributeElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENTITY_EXPRESSION_REF: {
				EntityExpressionRef entityExpressionRef = (EntityExpressionRef)theEObject;
				T result = caseEntityExpressionRef(entityExpressionRef);
				if (result == null) result = caseEntitySourceElement(entityExpressionRef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENUM_MAPPING: {
				EnumMapping enumMapping = (EnumMapping)theEObject;
				T result = caseEnumMapping(enumMapping);
				if (result == null) result = caseDataTypeMapping(enumMapping);
				if (result == null) result = caseMapping(enumMapping);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENUM_MAPPING_RULE: {
				EnumMappingRule enumMappingRule = (EnumMappingRule)theEObject;
				T result = caseEnumMappingRule(enumMappingRule);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENUM_TARGET_ELEMENT: {
				EnumTargetElement enumTargetElement = (EnumTargetElement)theEObject;
				T result = caseEnumTargetElement(enumTargetElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENUM_SOURCE_ELEMENT: {
				EnumSourceElement enumSourceElement = (EnumSourceElement)theEObject;
				T result = caseEnumSourceElement(enumSourceElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENUM_ATTRIBUTE_ELEMENT: {
				EnumAttributeElement enumAttributeElement = (EnumAttributeElement)theEObject;
				T result = caseEnumAttributeElement(enumAttributeElement);
				if (result == null) result = caseEnumSourceElement(enumAttributeElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENUM_EXPRESSION: {
				EnumExpression enumExpression = (EnumExpression)theEObject;
				T result = caseEnumExpression(enumExpression);
				if (result == null) result = caseEnumSourceElement(enumExpression);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENUM_REFERENCE: {
				EnumReference enumReference = (EnumReference)theEObject;
				T result = caseEnumReference(enumReference);
				if (result == null) result = caseEnumTargetElement(enumReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FUNCTION_BLOCK_REFERENCE: {
				FunctionBlockReference functionBlockReference = (FunctionBlockReference)theEObject;
				T result = caseFunctionBlockReference(functionBlockReference);
				if (result == null) result = caseInfoModelTargetElement(functionBlockReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.DATA_TYPE_REFERENCE: {
				DataTypeReference dataTypeReference = (DataTypeReference)theEObject;
				T result = caseDataTypeReference(dataTypeReference);
				if (result == null) result = caseFunctionBlockTargetElement(dataTypeReference);
				if (result == null) result = caseEntityTargetElement(dataTypeReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.DATA_TYPE_MAPPING: {
				DataTypeMapping dataTypeMapping = (DataTypeMapping)theEObject;
				T result = caseDataTypeMapping(dataTypeMapping);
				if (result == null) result = caseMapping(dataTypeMapping);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.STEREO_TYPE_REFERENCE: {
				StereoTypeReference stereoTypeReference = (StereoTypeReference)theEObject;
				T result = caseStereoTypeReference(stereoTypeReference);
				if (result == null) result = caseInfoModelTargetElement(stereoTypeReference);
				if (result == null) result = caseFunctionBlockTargetElement(stereoTypeReference);
				if (result == null) result = caseEntityTargetElement(stereoTypeReference);
				if (result == null) result = caseEnumTargetElement(stereoTypeReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.STEREO_TYPE_ELEMENT: {
				StereoTypeElement stereoTypeElement = (StereoTypeElement)theEObject;
				T result = caseStereoTypeElement(stereoTypeElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.STEREO_TYPE: {
				StereoType stereoType = (StereoType)theEObject;
				T result = caseStereoType(stereoType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ATTRIBUTE: {
				Attribute attribute = (Attribute)theEObject;
				T result = caseAttribute(attribute);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.NESTED_ENTITY_EXPRESSION: {
				NestedEntityExpression nestedEntityExpression = (NestedEntityExpression)theEObject;
				T result = caseNestedEntityExpression(nestedEntityExpression);
				if (result == null) result = caseEntityExpressionRef(nestedEntityExpression);
				if (result == null) result = caseEntitySourceElement(nestedEntityExpression);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENTITY_EXPRESSION: {
				EntityExpression entityExpression = (EntityExpression)theEObject;
				T result = caseEntityExpression(entityExpression);
				if (result == null) result = caseEntityExpressionRef(entityExpression);
				if (result == null) result = caseEntitySourceElement(entityExpression);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMappingModel(MappingModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Mapping</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Mapping</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMapping(Mapping object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Info Model Mapping</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Info Model Mapping</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInfoModelMapping(InfoModelMapping object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Info Model Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Info Model Mapping Rule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInfoModelMappingRule(InfoModelMappingRule object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Info Model Target Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Info Model Target Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInfoModelTargetElement(InfoModelTargetElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Info Model Source Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Info Model Source Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInfoModelSourceElement(InfoModelSourceElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Info Model Child</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Info Model Child</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInfoModelChild(InfoModelChild object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Info Model Fb Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Info Model Fb Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInfoModelFbElement(InfoModelFbElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Information Model Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Information Model Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInformationModelAttribute(InformationModelAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Function Block Mapping</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Function Block Mapping</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFunctionBlockMapping(FunctionBlockMapping object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Function Block Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Function Block Mapping Rule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFunctionBlockMappingRule(FunctionBlockMappingRule object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Function Block Target Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Function Block Target Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFunctionBlockTargetElement(FunctionBlockTargetElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Function Block Source Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Function Block Source Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFunctionBlockSourceElement(FunctionBlockSourceElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Function Block Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Function Block Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFunctionBlockElement(FunctionBlockElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Function Block Element Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Function Block Element Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFunctionBlockElementAttribute(FunctionBlockElementAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Function Block Child Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Function Block Child Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFunctionBlockChildElement(FunctionBlockChildElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operation Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operation Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOperationElement(OperationElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Configuration Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Configuration Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConfigurationElement(ConfigurationElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Status Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Status Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStatusElement(StatusElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fault Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fault Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFaultElement(FaultElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Event Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Event Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEventElement(EventElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>FB Type Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>FB Type Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFBTypeElement(FBTypeElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity Mapping</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity Mapping</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntityMapping(EntityMapping object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity Mapping Rule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntityMappingRule(EntityMappingRule object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity Target Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity Target Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntityTargetElement(EntityTargetElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity Source Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity Source Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntitySourceElement(EntitySourceElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity Attribute Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity Attribute Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntityAttributeElement(EntityAttributeElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity Expression Ref</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity Expression Ref</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntityExpressionRef(EntityExpressionRef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Enum Mapping</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Enum Mapping</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnumMapping(EnumMapping object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Enum Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Enum Mapping Rule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnumMappingRule(EnumMappingRule object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Enum Target Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Enum Target Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnumTargetElement(EnumTargetElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Enum Source Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Enum Source Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnumSourceElement(EnumSourceElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Enum Attribute Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Enum Attribute Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnumAttributeElement(EnumAttributeElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Enum Expression</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Enum Expression</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnumExpression(EnumExpression object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Enum Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Enum Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnumReference(EnumReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Function Block Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Function Block Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFunctionBlockReference(FunctionBlockReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Type Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Type Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataTypeReference(DataTypeReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Type Mapping</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Type Mapping</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataTypeMapping(DataTypeMapping object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Stereo Type Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Stereo Type Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStereoTypeReference(StereoTypeReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Stereo Type Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Stereo Type Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStereoTypeElement(StereoTypeElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Stereo Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Stereo Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStereoType(StereoType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAttribute(Attribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Nested Entity Expression</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Nested Entity Expression</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNestedEntityExpression(NestedEntityExpression object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity Expression</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity Expression</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntityExpression(EntityExpression object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModel(Model object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //MappingSwitch
