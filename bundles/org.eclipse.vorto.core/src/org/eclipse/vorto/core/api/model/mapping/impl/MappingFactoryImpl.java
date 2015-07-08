/**
 */
package org.eclipse.vorto.core.api.model.mapping.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.vorto.core.api.model.mapping.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MappingFactoryImpl extends EFactoryImpl implements MappingFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MappingFactory init() {
		try {
			MappingFactory theMappingFactory = (MappingFactory)EPackage.Registry.INSTANCE.getEFactory(MappingPackage.eNS_URI);
			if (theMappingFactory != null) {
				return theMappingFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MappingFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case MappingPackage.MAPPING_MODEL: return createMappingModel();
			case MappingPackage.RULE: return createRule();
			case MappingPackage.INFORMATION_MODEL_ELEMENT: return createInformationModelElement();
			case MappingPackage.FUNCTION_BLOCK_ELEMENT: return createFunctionBlockElement();
			case MappingPackage.OPERATION_ELEMENT: return createOperationElement();
			case MappingPackage.CONFIGURATION_ELEMENT: return createConfigurationElement();
			case MappingPackage.STATUS_ELEMENT: return createStatusElement();
			case MappingPackage.FAULT_ELEMENT: return createFaultElement();
			case MappingPackage.EVENT_ELEMENT: return createEventElement();
			case MappingPackage.TARGET_ELEMENT: return createTargetElement();
			case MappingPackage.STEREO_TYPE: return createStereoType();
			case MappingPackage.ATTRIBUTE: return createAttribute();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingModel createMappingModel() {
		MappingModelImpl mappingModel = new MappingModelImpl();
		return mappingModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rule createRule() {
		RuleImpl rule = new RuleImpl();
		return rule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InformationModelElement createInformationModelElement() {
		InformationModelElementImpl informationModelElement = new InformationModelElementImpl();
		return informationModelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlockElement createFunctionBlockElement() {
		FunctionBlockElementImpl functionBlockElement = new FunctionBlockElementImpl();
		return functionBlockElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationElement createOperationElement() {
		OperationElementImpl operationElement = new OperationElementImpl();
		return operationElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConfigurationElement createConfigurationElement() {
		ConfigurationElementImpl configurationElement = new ConfigurationElementImpl();
		return configurationElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatusElement createStatusElement() {
		StatusElementImpl statusElement = new StatusElementImpl();
		return statusElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FaultElement createFaultElement() {
		FaultElementImpl faultElement = new FaultElementImpl();
		return faultElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventElement createEventElement() {
		EventElementImpl eventElement = new EventElementImpl();
		return eventElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TargetElement createTargetElement() {
		TargetElementImpl targetElement = new TargetElementImpl();
		return targetElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StereoType createStereoType() {
		StereoTypeImpl stereoType = new StereoTypeImpl();
		return stereoType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Attribute createAttribute() {
		AttributeImpl attribute = new AttributeImpl();
		return attribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingPackage getMappingPackage() {
		return (MappingPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MappingPackage getPackage() {
		return MappingPackage.eINSTANCE;
	}

} //MappingFactoryImpl
