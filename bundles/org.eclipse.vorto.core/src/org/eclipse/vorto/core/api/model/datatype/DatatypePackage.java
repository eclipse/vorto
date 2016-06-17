/**
 */
package org.eclipse.vorto.core.api.model.datatype;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.vorto.core.api.model.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.datatype.DatatypeFactory
 * @model kind="package"
 * @generated
 */
public interface DatatypePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "datatype";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/vorto/metamodel/Datatype";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "datatype";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DatatypePackage eINSTANCE = org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.TypeImpl <em>Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.TypeImpl
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getType()
	 * @generated
	 */
	int TYPE = 8;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE__NAME = ModelPackage.MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE__NAMESPACE = ModelPackage.MODEL__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE__VERSION = ModelPackage.MODEL__VERSION;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE__REFERENCES = ModelPackage.MODEL__REFERENCES;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE__DESCRIPTION = ModelPackage.MODEL__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Displayname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE__DISPLAYNAME = ModelPackage.MODEL__DISPLAYNAME;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE__CATEGORY = ModelPackage.MODEL__CATEGORY;

	/**
	 * The number of structural features of the '<em>Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_FEATURE_COUNT = ModelPackage.MODEL_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.EntityImpl <em>Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.EntityImpl
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getEntity()
	 * @generated
	 */
	int ENTITY = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__NAME = TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__NAMESPACE = TYPE__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__VERSION = TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__REFERENCES = TYPE__REFERENCES;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__DESCRIPTION = TYPE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Displayname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__DISPLAYNAME = TYPE__DISPLAYNAME;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__CATEGORY = TYPE__CATEGORY;

	/**
	 * The feature id for the '<em><b>Super Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__SUPER_TYPE = TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY__PROPERTIES = TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_FEATURE_COUNT = TYPE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.PropertyImpl <em>Property</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.PropertyImpl
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getProperty()
	 * @generated
	 */
	int PROPERTY = 1;

	/**
	 * The feature id for the '<em><b>Presence</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__PRESENCE = 0;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__MULTIPLICITY = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__NAME = 2;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__DESCRIPTION = 3;

	/**
	 * The feature id for the '<em><b>Constraint Rule</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__CONSTRAINT_RULE = 4;

	/**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__TYPE = 5;

	/**
	 * The feature id for the '<em><b>Property Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__PROPERTY_ATTRIBUTES = 6;

	/**
	 * The number of structural features of the '<em>Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.PropertyTypeImpl <em>Property Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.PropertyTypeImpl
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getPropertyType()
	 * @generated
	 */
	int PROPERTY_TYPE = 9;

	/**
	 * The number of structural features of the '<em>Property Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_TYPE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.PrimitivePropertyTypeImpl <em>Primitive Property Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.PrimitivePropertyTypeImpl
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getPrimitivePropertyType()
	 * @generated
	 */
	int PRIMITIVE_PROPERTY_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_PROPERTY_TYPE__TYPE = PROPERTY_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Primitive Property Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_PROPERTY_TYPE_FEATURE_COUNT = PROPERTY_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.ObjectPropertyTypeImpl <em>Object Property Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.ObjectPropertyTypeImpl
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getObjectPropertyType()
	 * @generated
	 */
	int OBJECT_PROPERTY_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_PROPERTY_TYPE__TYPE = PROPERTY_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Object Property Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_PROPERTY_TYPE_FEATURE_COUNT = PROPERTY_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.PresenceImpl <em>Presence</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.PresenceImpl
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getPresence()
	 * @generated
	 */
	int PRESENCE = 4;

	/**
	 * The feature id for the '<em><b>Mandatory</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRESENCE__MANDATORY = 0;

	/**
	 * The number of structural features of the '<em>Presence</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRESENCE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.ConstraintImpl <em>Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.ConstraintImpl
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getConstraint()
	 * @generated
	 */
	int CONSTRAINT = 5;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Constraint Values</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__CONSTRAINT_VALUES = 1;

	/**
	 * The number of structural features of the '<em>Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.EnumImpl <em>Enum</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.EnumImpl
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getEnum()
	 * @generated
	 */
	int ENUM = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM__NAME = TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM__NAMESPACE = TYPE__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM__VERSION = TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM__REFERENCES = TYPE__REFERENCES;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM__DESCRIPTION = TYPE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Displayname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM__DISPLAYNAME = TYPE__DISPLAYNAME;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM__CATEGORY = TYPE__CATEGORY;

	/**
	 * The feature id for the '<em><b>Enums</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM__ENUMS = TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Enum</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_FEATURE_COUNT = TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.EnumLiteralImpl <em>Enum Literal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.EnumLiteralImpl
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getEnumLiteral()
	 * @generated
	 */
	int ENUM_LITERAL = 7;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_LITERAL__NAME = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_LITERAL__DESCRIPTION = 1;

	/**
	 * The number of structural features of the '<em>Enum Literal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_LITERAL_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.PropertyAttribute <em>Property Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.PropertyAttribute
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getPropertyAttribute()
	 * @generated
	 */
	int PROPERTY_ATTRIBUTE = 10;

	/**
	 * The number of structural features of the '<em>Property Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_ATTRIBUTE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.BooleanPropertyAttributeImpl <em>Boolean Property Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.BooleanPropertyAttributeImpl
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getBooleanPropertyAttribute()
	 * @generated
	 */
	int BOOLEAN_PROPERTY_ATTRIBUTE = 11;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOLEAN_PROPERTY_ATTRIBUTE__TYPE = PROPERTY_ATTRIBUTE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOLEAN_PROPERTY_ATTRIBUTE__VALUE = PROPERTY_ATTRIBUTE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Boolean Property Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOLEAN_PROPERTY_ATTRIBUTE_FEATURE_COUNT = PROPERTY_ATTRIBUTE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.EnumLiteralPropertyAttributeImpl <em>Enum Literal Property Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.EnumLiteralPropertyAttributeImpl
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getEnumLiteralPropertyAttribute()
	 * @generated
	 */
	int ENUM_LITERAL_PROPERTY_ATTRIBUTE = 12;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_LITERAL_PROPERTY_ATTRIBUTE__TYPE = PROPERTY_ATTRIBUTE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_LITERAL_PROPERTY_ATTRIBUTE__VALUE = PROPERTY_ATTRIBUTE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Enum Literal Property Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_LITERAL_PROPERTY_ATTRIBUTE_FEATURE_COUNT = PROPERTY_ATTRIBUTE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.ConstraintRuleImpl <em>Constraint Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.ConstraintRuleImpl
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getConstraintRule()
	 * @generated
	 */
	int CONSTRAINT_RULE = 13;

	/**
	 * The feature id for the '<em><b>Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_RULE__CONSTRAINTS = 0;

	/**
	 * The number of structural features of the '<em>Constraint Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_RULE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.PrimitiveType <em>Primitive Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.PrimitiveType
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getPrimitiveType()
	 * @generated
	 */
	int PRIMITIVE_TYPE = 14;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType <em>Constraint Interval Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getConstraintIntervalType()
	 * @generated
	 */
	int CONSTRAINT_INTERVAL_TYPE = 15;


	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttributeType <em>Boolean Property Attribute Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttributeType
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getBooleanPropertyAttributeType()
	 * @generated
	 */
	int BOOLEAN_PROPERTY_ATTRIBUTE_TYPE = 16;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttributeType <em>Enum Literal Property Attribute Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttributeType
	 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getEnumLiteralPropertyAttributeType()
	 * @generated
	 */
	int ENUM_LITERAL_PROPERTY_ATTRIBUTE_TYPE = 17;

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.datatype.Entity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Entity
	 * @generated
	 */
	EClass getEntity();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.datatype.Entity#getSuperType <em>Super Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Super Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Entity#getSuperType()
	 * @see #getEntity()
	 * @generated
	 */
	EReference getEntity_SuperType();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.datatype.Entity#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Entity#getProperties()
	 * @see #getEntity()
	 * @generated
	 */
	EReference getEntity_Properties();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.datatype.Property <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Property
	 * @generated
	 */
	EClass getProperty();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.datatype.Property#getPresence <em>Presence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Presence</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Property#getPresence()
	 * @see #getProperty()
	 * @generated
	 */
	EReference getProperty_Presence();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.datatype.Property#isMultiplicity <em>Multiplicity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Multiplicity</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Property#isMultiplicity()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_Multiplicity();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.datatype.Property#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Property#getName()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.datatype.Property#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Property#getDescription()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_Description();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.datatype.Property#getConstraintRule <em>Constraint Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Constraint Rule</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Property#getConstraintRule()
	 * @see #getProperty()
	 * @generated
	 */
	EReference getProperty_ConstraintRule();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.datatype.Property#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Property#getType()
	 * @see #getProperty()
	 * @generated
	 */
	EReference getProperty_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.datatype.Property#getPropertyAttributes <em>Property Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Property Attributes</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Property#getPropertyAttributes()
	 * @see #getProperty()
	 * @generated
	 */
	EReference getProperty_PropertyAttributes();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType <em>Primitive Property Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Primitive Property Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
	 * @generated
	 */
	EClass getPrimitivePropertyType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType#getType()
	 * @see #getPrimitivePropertyType()
	 * @generated
	 */
	EAttribute getPrimitivePropertyType_Type();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType <em>Object Property Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Object Property Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
	 * @generated
	 */
	EClass getObjectPropertyType();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType#getType()
	 * @see #getObjectPropertyType()
	 * @generated
	 */
	EReference getObjectPropertyType_Type();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.datatype.Presence <em>Presence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Presence</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Presence
	 * @generated
	 */
	EClass getPresence();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.datatype.Presence#isMandatory <em>Mandatory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mandatory</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Presence#isMandatory()
	 * @see #getPresence()
	 * @generated
	 */
	EAttribute getPresence_Mandatory();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.datatype.Constraint <em>Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constraint</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Constraint
	 * @generated
	 */
	EClass getConstraint();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.datatype.Constraint#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Constraint#getType()
	 * @see #getConstraint()
	 * @generated
	 */
	EAttribute getConstraint_Type();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.datatype.Constraint#getConstraintValues <em>Constraint Values</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Constraint Values</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Constraint#getConstraintValues()
	 * @see #getConstraint()
	 * @generated
	 */
	EAttribute getConstraint_ConstraintValues();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.datatype.Enum <em>Enum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Enum
	 * @generated
	 */
	EClass getEnum();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.datatype.Enum#getEnums <em>Enums</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Enums</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Enum#getEnums()
	 * @see #getEnum()
	 * @generated
	 */
	EReference getEnum_Enums();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.datatype.EnumLiteral <em>Enum Literal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Literal</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.EnumLiteral
	 * @generated
	 */
	EClass getEnumLiteral();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.datatype.EnumLiteral#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.EnumLiteral#getName()
	 * @see #getEnumLiteral()
	 * @generated
	 */
	EAttribute getEnumLiteral_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.datatype.EnumLiteral#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.EnumLiteral#getDescription()
	 * @see #getEnumLiteral()
	 * @generated
	 */
	EAttribute getEnumLiteral_Description();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.datatype.Type <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.Type
	 * @generated
	 */
	EClass getType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.datatype.PropertyType <em>Property Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.PropertyType
	 * @generated
	 */
	EClass getPropertyType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.datatype.PropertyAttribute <em>Property Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.PropertyAttribute
	 * @generated
	 */
	EClass getPropertyAttribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttribute <em>Boolean Property Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Boolean Property Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttribute
	 * @generated
	 */
	EClass getBooleanPropertyAttribute();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttribute#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttribute#getType()
	 * @see #getBooleanPropertyAttribute()
	 * @generated
	 */
	EAttribute getBooleanPropertyAttribute_Type();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttribute#isValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttribute#isValue()
	 * @see #getBooleanPropertyAttribute()
	 * @generated
	 */
	EAttribute getBooleanPropertyAttribute_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute <em>Enum Literal Property Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Literal Property Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute
	 * @generated
	 */
	EClass getEnumLiteralPropertyAttribute();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute#getType()
	 * @see #getEnumLiteralPropertyAttribute()
	 * @generated
	 */
	EAttribute getEnumLiteralPropertyAttribute_Type();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute#getValue()
	 * @see #getEnumLiteralPropertyAttribute()
	 * @generated
	 */
	EReference getEnumLiteralPropertyAttribute_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.datatype.ConstraintRule <em>Constraint Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constraint Rule</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.ConstraintRule
	 * @generated
	 */
	EClass getConstraintRule();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.datatype.ConstraintRule#getConstraints <em>Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Constraints</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.ConstraintRule#getConstraints()
	 * @see #getConstraintRule()
	 * @generated
	 */
	EReference getConstraintRule_Constraints();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.vorto.core.api.model.datatype.PrimitiveType <em>Primitive Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Primitive Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.PrimitiveType
	 * @generated
	 */
	EEnum getPrimitiveType();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType <em>Constraint Interval Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Constraint Interval Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType
	 * @generated
	 */
	EEnum getConstraintIntervalType();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttributeType <em>Boolean Property Attribute Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Boolean Property Attribute Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttributeType
	 * @generated
	 */
	EEnum getBooleanPropertyAttributeType();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttributeType <em>Enum Literal Property Attribute Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Enum Literal Property Attribute Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttributeType
	 * @generated
	 */
	EEnum getEnumLiteralPropertyAttributeType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DatatypeFactory getDatatypeFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.EntityImpl <em>Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.EntityImpl
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getEntity()
		 * @generated
		 */
		EClass ENTITY = eINSTANCE.getEntity();

		/**
		 * The meta object literal for the '<em><b>Super Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY__SUPER_TYPE = eINSTANCE.getEntity_SuperType();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY__PROPERTIES = eINSTANCE.getEntity_Properties();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.PropertyImpl <em>Property</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.PropertyImpl
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getProperty()
		 * @generated
		 */
		EClass PROPERTY = eINSTANCE.getProperty();

		/**
		 * The meta object literal for the '<em><b>Presence</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROPERTY__PRESENCE = eINSTANCE.getProperty_Presence();

		/**
		 * The meta object literal for the '<em><b>Multiplicity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPERTY__MULTIPLICITY = eINSTANCE.getProperty_Multiplicity();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPERTY__NAME = eINSTANCE.getProperty_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPERTY__DESCRIPTION = eINSTANCE.getProperty_Description();

		/**
		 * The meta object literal for the '<em><b>Constraint Rule</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROPERTY__CONSTRAINT_RULE = eINSTANCE.getProperty_ConstraintRule();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROPERTY__TYPE = eINSTANCE.getProperty_Type();

		/**
		 * The meta object literal for the '<em><b>Property Attributes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROPERTY__PROPERTY_ATTRIBUTES = eINSTANCE.getProperty_PropertyAttributes();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.PrimitivePropertyTypeImpl <em>Primitive Property Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.PrimitivePropertyTypeImpl
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getPrimitivePropertyType()
		 * @generated
		 */
		EClass PRIMITIVE_PROPERTY_TYPE = eINSTANCE.getPrimitivePropertyType();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRIMITIVE_PROPERTY_TYPE__TYPE = eINSTANCE.getPrimitivePropertyType_Type();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.ObjectPropertyTypeImpl <em>Object Property Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.ObjectPropertyTypeImpl
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getObjectPropertyType()
		 * @generated
		 */
		EClass OBJECT_PROPERTY_TYPE = eINSTANCE.getObjectPropertyType();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OBJECT_PROPERTY_TYPE__TYPE = eINSTANCE.getObjectPropertyType_Type();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.PresenceImpl <em>Presence</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.PresenceImpl
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getPresence()
		 * @generated
		 */
		EClass PRESENCE = eINSTANCE.getPresence();

		/**
		 * The meta object literal for the '<em><b>Mandatory</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRESENCE__MANDATORY = eINSTANCE.getPresence_Mandatory();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.ConstraintImpl <em>Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.ConstraintImpl
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getConstraint()
		 * @generated
		 */
		EClass CONSTRAINT = eINSTANCE.getConstraint();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONSTRAINT__TYPE = eINSTANCE.getConstraint_Type();

		/**
		 * The meta object literal for the '<em><b>Constraint Values</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONSTRAINT__CONSTRAINT_VALUES = eINSTANCE.getConstraint_ConstraintValues();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.EnumImpl <em>Enum</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.EnumImpl
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getEnum()
		 * @generated
		 */
		EClass ENUM = eINSTANCE.getEnum();

		/**
		 * The meta object literal for the '<em><b>Enums</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENUM__ENUMS = eINSTANCE.getEnum_Enums();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.EnumLiteralImpl <em>Enum Literal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.EnumLiteralImpl
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getEnumLiteral()
		 * @generated
		 */
		EClass ENUM_LITERAL = eINSTANCE.getEnumLiteral();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENUM_LITERAL__NAME = eINSTANCE.getEnumLiteral_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENUM_LITERAL__DESCRIPTION = eINSTANCE.getEnumLiteral_Description();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.TypeImpl <em>Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.TypeImpl
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getType()
		 * @generated
		 */
		EClass TYPE = eINSTANCE.getType();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.PropertyTypeImpl <em>Property Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.PropertyTypeImpl
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getPropertyType()
		 * @generated
		 */
		EClass PROPERTY_TYPE = eINSTANCE.getPropertyType();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.PropertyAttribute <em>Property Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.PropertyAttribute
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getPropertyAttribute()
		 * @generated
		 */
		EClass PROPERTY_ATTRIBUTE = eINSTANCE.getPropertyAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.BooleanPropertyAttributeImpl <em>Boolean Property Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.BooleanPropertyAttributeImpl
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getBooleanPropertyAttribute()
		 * @generated
		 */
		EClass BOOLEAN_PROPERTY_ATTRIBUTE = eINSTANCE.getBooleanPropertyAttribute();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BOOLEAN_PROPERTY_ATTRIBUTE__TYPE = eINSTANCE.getBooleanPropertyAttribute_Type();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BOOLEAN_PROPERTY_ATTRIBUTE__VALUE = eINSTANCE.getBooleanPropertyAttribute_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.EnumLiteralPropertyAttributeImpl <em>Enum Literal Property Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.EnumLiteralPropertyAttributeImpl
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getEnumLiteralPropertyAttribute()
		 * @generated
		 */
		EClass ENUM_LITERAL_PROPERTY_ATTRIBUTE = eINSTANCE.getEnumLiteralPropertyAttribute();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENUM_LITERAL_PROPERTY_ATTRIBUTE__TYPE = eINSTANCE.getEnumLiteralPropertyAttribute_Type();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENUM_LITERAL_PROPERTY_ATTRIBUTE__VALUE = eINSTANCE.getEnumLiteralPropertyAttribute_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.impl.ConstraintRuleImpl <em>Constraint Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.ConstraintRuleImpl
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getConstraintRule()
		 * @generated
		 */
		EClass CONSTRAINT_RULE = eINSTANCE.getConstraintRule();

		/**
		 * The meta object literal for the '<em><b>Constraints</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONSTRAINT_RULE__CONSTRAINTS = eINSTANCE.getConstraintRule_Constraints();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.PrimitiveType <em>Primitive Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.PrimitiveType
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getPrimitiveType()
		 * @generated
		 */
		EEnum PRIMITIVE_TYPE = eINSTANCE.getPrimitiveType();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType <em>Constraint Interval Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getConstraintIntervalType()
		 * @generated
		 */
		EEnum CONSTRAINT_INTERVAL_TYPE = eINSTANCE.getConstraintIntervalType();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttributeType <em>Boolean Property Attribute Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttributeType
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getBooleanPropertyAttributeType()
		 * @generated
		 */
		EEnum BOOLEAN_PROPERTY_ATTRIBUTE_TYPE = eINSTANCE.getBooleanPropertyAttributeType();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttributeType <em>Enum Literal Property Attribute Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttributeType
		 * @see org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl#getEnumLiteralPropertyAttributeType()
		 * @generated
		 */
		EEnum ENUM_LITERAL_PROPERTY_ATTRIBUTE_TYPE = eINSTANCE.getEnumLiteralPropertyAttributeType();

	}

} //DatatypePackage
