/**
 */
package org.eclipse.vorto.core.api.model.mapping.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.vorto.core.api.model.mapping.*;

import org.eclipse.vorto.core.api.model.model.Model;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage
 * @generated
 */
public class MappingAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MappingPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = MappingPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MappingSwitch<Adapter> modelSwitch =
		new MappingSwitch<Adapter>() {
			@Override
			public Adapter caseMappingModel(MappingModel object) {
				return createMappingModelAdapter();
			}
			@Override
			public Adapter caseInfoModelMappingRule(InfoModelMappingRule object) {
				return createInfoModelMappingRuleAdapter();
			}
			@Override
			public Adapter caseInfoModelTargetElement(InfoModelTargetElement object) {
				return createInfoModelTargetElementAdapter();
			}
			@Override
			public Adapter caseInfoModelSourceElement(InfoModelSourceElement object) {
				return createInfoModelSourceElementAdapter();
			}
			@Override
			public Adapter caseInfoModelChild(InfoModelChild object) {
				return createInfoModelChildAdapter();
			}
			@Override
			public Adapter caseInfoModelFbElement(InfoModelFbElement object) {
				return createInfoModelFbElementAdapter();
			}
			@Override
			public Adapter caseInformationModelProperty(InformationModelProperty object) {
				return createInformationModelPropertyAdapter();
			}
			@Override
			public Adapter caseFunctionBlockMapping(FunctionBlockMapping object) {
				return createFunctionBlockMappingAdapter();
			}
			@Override
			public Adapter caseFunctionBlockMappingRule(FunctionBlockMappingRule object) {
				return createFunctionBlockMappingRuleAdapter();
			}
			@Override
			public Adapter caseFunctionBlockTargetElement(FunctionBlockTargetElement object) {
				return createFunctionBlockTargetElementAdapter();
			}
			@Override
			public Adapter caseFunctionBlockSourceElement(FunctionBlockSourceElement object) {
				return createFunctionBlockSourceElementAdapter();
			}
			@Override
			public Adapter caseFunctionBlockElement(FunctionBlockElement object) {
				return createFunctionBlockElementAdapter();
			}
			@Override
			public Adapter caseFunctionBlockElementAttribute(FunctionBlockElementAttribute object) {
				return createFunctionBlockElementAttributeAdapter();
			}
			@Override
			public Adapter caseFunctionBlockChildElement(FunctionBlockChildElement object) {
				return createFunctionBlockChildElementAdapter();
			}
			@Override
			public Adapter caseOperationElement(OperationElement object) {
				return createOperationElementAdapter();
			}
			@Override
			public Adapter caseConfigurationElement(ConfigurationElement object) {
				return createConfigurationElementAdapter();
			}
			@Override
			public Adapter caseStatusElement(StatusElement object) {
				return createStatusElementAdapter();
			}
			@Override
			public Adapter caseFaultElement(FaultElement object) {
				return createFaultElementAdapter();
			}
			@Override
			public Adapter caseEventElement(EventElement object) {
				return createEventElementAdapter();
			}
			@Override
			public Adapter caseFBTypeElement(FBTypeElement object) {
				return createFBTypeElementAdapter();
			}
			@Override
			public Adapter caseFBTypeElementChild(FBTypeElementChild object) {
				return createFBTypeElementChildAdapter();
			}
			@Override
			public Adapter caseFBTypeProperty(FBTypeProperty object) {
				return createFBTypePropertyAdapter();
			}
			@Override
			public Adapter caseDataTypeMapping(DataTypeMapping object) {
				return createDataTypeMappingAdapter();
			}
			@Override
			public Adapter caseDataTypeMappingRule(DataTypeMappingRule object) {
				return createDataTypeMappingRuleAdapter();
			}
			@Override
			public Adapter caseDataTypeTargetElement(DataTypeTargetElement object) {
				return createDataTypeTargetElementAdapter();
			}
			@Override
			public Adapter caseDataTypeSourceElement(DataTypeSourceElement object) {
				return createDataTypeSourceElementAdapter();
			}
			@Override
			public Adapter caseDataTypePropertyElement(DataTypePropertyElement object) {
				return createDataTypePropertyElementAdapter();
			}
			@Override
			public Adapter caseEntityExpressionRef(EntityExpressionRef object) {
				return createEntityExpressionRefAdapter();
			}
			@Override
			public Adapter caseDataTypeAttribute(DataTypeAttribute object) {
				return createDataTypeAttributeAdapter();
			}
			@Override
			public Adapter caseFunctionBlockReference(FunctionBlockReference object) {
				return createFunctionBlockReferenceAdapter();
			}
			@Override
			public Adapter caseDataTypeReference(DataTypeReference object) {
				return createDataTypeReferenceAdapter();
			}
			@Override
			public Adapter caseStereoTypeReference(StereoTypeReference object) {
				return createStereoTypeReferenceAdapter();
			}
			@Override
			public Adapter caseStereoTypeElement(StereoTypeElement object) {
				return createStereoTypeElementAdapter();
			}
			@Override
			public Adapter caseStereoType(StereoType object) {
				return createStereoTypeAdapter();
			}
			@Override
			public Adapter caseAttribute(Attribute object) {
				return createAttributeAdapter();
			}
			@Override
			public Adapter caseNestedEntityExpression(NestedEntityExpression object) {
				return createNestedEntityExpressionAdapter();
			}
			@Override
			public Adapter caseEntityExpression(EntityExpression object) {
				return createEntityExpressionAdapter();
			}
			@Override
			public Adapter caseModel(Model object) {
				return createModelAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingModel
	 * @generated
	 */
	public Adapter createMappingModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule <em>Info Model Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule
	 * @generated
	 */
	public Adapter createInfoModelMappingRuleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelTargetElement <em>Info Model Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelTargetElement
	 * @generated
	 */
	public Adapter createInfoModelTargetElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement <em>Info Model Source Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement
	 * @generated
	 */
	public Adapter createInfoModelSourceElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelChild <em>Info Model Child</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelChild
	 * @generated
	 */
	public Adapter createInfoModelChildAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement <em>Info Model Fb Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement
	 * @generated
	 */
	public Adapter createInfoModelFbElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.InformationModelProperty <em>Information Model Property</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.InformationModelProperty
	 * @generated
	 */
	public Adapter createInformationModelPropertyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping <em>Function Block Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping
	 * @generated
	 */
	public Adapter createFunctionBlockMappingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule <em>Function Block Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule
	 * @generated
	 */
	public Adapter createFunctionBlockMappingRuleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockTargetElement <em>Function Block Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockTargetElement
	 * @generated
	 */
	public Adapter createFunctionBlockTargetElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement <em>Function Block Source Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement
	 * @generated
	 */
	public Adapter createFunctionBlockSourceElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockElement <em>Function Block Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockElement
	 * @generated
	 */
	public Adapter createFunctionBlockElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockElementAttribute <em>Function Block Element Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockElementAttribute
	 * @generated
	 */
	public Adapter createFunctionBlockElementAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockChildElement <em>Function Block Child Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockChildElement
	 * @generated
	 */
	public Adapter createFunctionBlockChildElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.OperationElement <em>Operation Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.OperationElement
	 * @generated
	 */
	public Adapter createOperationElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.ConfigurationElement <em>Configuration Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.ConfigurationElement
	 * @generated
	 */
	public Adapter createConfigurationElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.StatusElement <em>Status Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.StatusElement
	 * @generated
	 */
	public Adapter createStatusElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FaultElement <em>Fault Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FaultElement
	 * @generated
	 */
	public Adapter createFaultElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.EventElement <em>Event Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.EventElement
	 * @generated
	 */
	public Adapter createEventElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FBTypeElement <em>FB Type Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FBTypeElement
	 * @generated
	 */
	public Adapter createFBTypeElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FBTypeElementChild <em>FB Type Element Child</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FBTypeElementChild
	 * @generated
	 */
	public Adapter createFBTypeElementChildAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FBTypeProperty <em>FB Type Property</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FBTypeProperty
	 * @generated
	 */
	public Adapter createFBTypePropertyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeMapping <em>Data Type Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeMapping
	 * @generated
	 */
	public Adapter createDataTypeMappingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule <em>Data Type Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule
	 * @generated
	 */
	public Adapter createDataTypeMappingRuleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeTargetElement <em>Data Type Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeTargetElement
	 * @generated
	 */
	public Adapter createDataTypeTargetElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeSourceElement <em>Data Type Source Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeSourceElement
	 * @generated
	 */
	public Adapter createDataTypeSourceElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.DataTypePropertyElement <em>Data Type Property Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypePropertyElement
	 * @generated
	 */
	public Adapter createDataTypePropertyElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.EntityExpressionRef <em>Entity Expression Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityExpressionRef
	 * @generated
	 */
	public Adapter createEntityExpressionRefAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeAttribute <em>Data Type Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeAttribute
	 * @generated
	 */
	public Adapter createDataTypeAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockReference <em>Function Block Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockReference
	 * @generated
	 */
	public Adapter createFunctionBlockReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeReference <em>Data Type Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeReference
	 * @generated
	 */
	public Adapter createDataTypeReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.StereoTypeReference <em>Stereo Type Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.StereoTypeReference
	 * @generated
	 */
	public Adapter createStereoTypeReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.StereoTypeElement <em>Stereo Type Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.StereoTypeElement
	 * @generated
	 */
	public Adapter createStereoTypeElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.StereoType <em>Stereo Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.StereoType
	 * @generated
	 */
	public Adapter createStereoTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.Attribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.Attribute
	 * @generated
	 */
	public Adapter createAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression <em>Nested Entity Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression
	 * @generated
	 */
	public Adapter createNestedEntityExpressionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.EntityExpression <em>Entity Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityExpression
	 * @generated
	 */
	public Adapter createEntityExpressionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.model.Model <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.model.Model
	 * @generated
	 */
	public Adapter createModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //MappingAdapterFactory
