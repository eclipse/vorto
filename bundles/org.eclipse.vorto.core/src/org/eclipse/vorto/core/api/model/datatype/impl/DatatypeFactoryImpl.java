/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
/**
 */
package org.eclipse.vorto.core.api.model.datatype.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttribute;
import org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttributeType;
import org.eclipse.vorto.core.api.model.datatype.ComplexPrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.Constraint;
import org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType;
import org.eclipse.vorto.core.api.model.datatype.ConstraintRule;
import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory;
import org.eclipse.vorto.core.api.model.datatype.DatatypePackage;
import org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteral;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttributeType;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.Presence;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.datatype.PropertyAttribute;
import org.eclipse.vorto.core.api.model.datatype.PropertyAttributeType;
import org.eclipse.vorto.core.api.model.datatype.PropertyType;
import org.eclipse.vorto.core.api.model.datatype.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DatatypeFactoryImpl extends EFactoryImpl implements DatatypeFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DatatypeFactory init() {
		try {
			DatatypeFactory theDatatypeFactory = (DatatypeFactory)EPackage.Registry.INSTANCE.getEFactory(DatatypePackage.eNS_URI);
			if (theDatatypeFactory != null) {
				return theDatatypeFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DatatypeFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DatatypeFactoryImpl() {
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
			case DatatypePackage.ENTITY: return createEntity();
			case DatatypePackage.PROPERTY: return createProperty();
			case DatatypePackage.PRIMITIVE_PROPERTY_TYPE: return createPrimitivePropertyType();
			case DatatypePackage.OBJECT_PROPERTY_TYPE: return createObjectPropertyType();
			case DatatypePackage.PRESENCE: return createPresence();
			case DatatypePackage.CONSTRAINT: return createConstraint();
			case DatatypePackage.ENUM: return createEnum();
			case DatatypePackage.ENUM_LITERAL: return createEnumLiteral();
			case DatatypePackage.TYPE: return createType();
			case DatatypePackage.PROPERTY_TYPE: return createPropertyType();
			case DatatypePackage.BOOLEAN_PROPERTY_ATTRIBUTE: return createBooleanPropertyAttribute();
			case DatatypePackage.ENUM_LITERAL_PROPERTY_ATTRIBUTE: return createEnumLiteralPropertyAttribute();
			case DatatypePackage.CONSTRAINT_RULE: return createConstraintRule();
			case DatatypePackage.COMPLEX_PRIMITIVE_PROPERTY_TYPE: return createComplexPrimitivePropertyType();
			case DatatypePackage.DICTIONARY_PROPERTY_TYPE: return createDictionaryPropertyType();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case DatatypePackage.PRIMITIVE_TYPE:
				return createPrimitiveTypeFromString(eDataType, initialValue);
			case DatatypePackage.CONSTRAINT_INTERVAL_TYPE:
				return createConstraintIntervalTypeFromString(eDataType, initialValue);
			case DatatypePackage.BOOLEAN_PROPERTY_ATTRIBUTE_TYPE:
				return createBooleanPropertyAttributeTypeFromString(eDataType, initialValue);
			case DatatypePackage.ENUM_LITERAL_PROPERTY_ATTRIBUTE_TYPE:
				return createEnumLiteralPropertyAttributeTypeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case DatatypePackage.PRIMITIVE_TYPE:
				return convertPrimitiveTypeToString(eDataType, instanceValue);
			case DatatypePackage.CONSTRAINT_INTERVAL_TYPE:
				return convertConstraintIntervalTypeToString(eDataType, instanceValue);
			case DatatypePackage.BOOLEAN_PROPERTY_ATTRIBUTE_TYPE:
				return convertBooleanPropertyAttributeTypeToString(eDataType, instanceValue);
			case DatatypePackage.ENUM_LITERAL_PROPERTY_ATTRIBUTE_TYPE:
				return convertEnumLiteralPropertyAttributeTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Entity createEntity() {
		EntityImpl entity = new EntityImpl();
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Property createProperty() {
		PropertyImpl property = new PropertyImpl();
		return property;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PrimitivePropertyType createPrimitivePropertyType() {
		PrimitivePropertyTypeImpl primitivePropertyType = new PrimitivePropertyTypeImpl();
		return primitivePropertyType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ObjectPropertyType createObjectPropertyType() {
		ObjectPropertyTypeImpl objectPropertyType = new ObjectPropertyTypeImpl();
		return objectPropertyType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Presence createPresence() {
		PresenceImpl presence = new PresenceImpl();
		return presence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Constraint createConstraint() {
		ConstraintImpl constraint = new ConstraintImpl();
		return constraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.vorto.core.api.model.datatype.Enum createEnum() {
		EnumImpl enum_ = new EnumImpl();
		return enum_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumLiteral createEnumLiteral() {
		EnumLiteralImpl enumLiteral = new EnumLiteralImpl();
		return enumLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Type createType() {
		TypeImpl type = new TypeImpl();
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PropertyType createPropertyType() {
		PropertyTypeImpl propertyType = new PropertyTypeImpl();
		return propertyType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BooleanPropertyAttribute createBooleanPropertyAttribute() {
		BooleanPropertyAttributeImpl booleanPropertyAttribute = new BooleanPropertyAttributeImpl();
		return booleanPropertyAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumLiteralPropertyAttribute createEnumLiteralPropertyAttribute() {
		EnumLiteralPropertyAttributeImpl enumLiteralPropertyAttribute = new EnumLiteralPropertyAttributeImpl();
		return enumLiteralPropertyAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConstraintRule createConstraintRule() {
		ConstraintRuleImpl constraintRule = new ConstraintRuleImpl();
		return constraintRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComplexPrimitivePropertyType createComplexPrimitivePropertyType() {
		ComplexPrimitivePropertyTypeImpl complexPrimitivePropertyType = new ComplexPrimitivePropertyTypeImpl();
		return complexPrimitivePropertyType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DictionaryPropertyType createDictionaryPropertyType() {
		DictionaryPropertyTypeImpl dictionaryPropertyType = new DictionaryPropertyTypeImpl();
		return dictionaryPropertyType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PrimitiveType createPrimitiveTypeFromString(EDataType eDataType, String initialValue) {
		PrimitiveType result = PrimitiveType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPrimitiveTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConstraintIntervalType createConstraintIntervalTypeFromString(EDataType eDataType, String initialValue) {
		ConstraintIntervalType result = ConstraintIntervalType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertConstraintIntervalTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BooleanPropertyAttributeType createBooleanPropertyAttributeTypeFromString(EDataType eDataType, String initialValue) {
		BooleanPropertyAttributeType result = BooleanPropertyAttributeType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertBooleanPropertyAttributeTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumLiteralPropertyAttributeType createEnumLiteralPropertyAttributeTypeFromString(EDataType eDataType, String initialValue) {
		EnumLiteralPropertyAttributeType result = EnumLiteralPropertyAttributeType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEnumLiteralPropertyAttributeTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DatatypePackage getDatatypePackage() {
		return (DatatypePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DatatypePackage getPackage() {
		return DatatypePackage.eINSTANCE;
	}

} //DatatypeFactoryImpl
