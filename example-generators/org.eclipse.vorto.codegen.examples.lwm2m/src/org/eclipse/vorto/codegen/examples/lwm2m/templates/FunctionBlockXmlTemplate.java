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
package org.eclipse.vorto.codegen.examples.lwm2m.templates;

import java.io.StringWriter;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.eclipse.emf.common.util.EList;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.mapping.IMapped;
import org.eclipse.vorto.codegen.examples.lwm2m.generated.LWM2M;
import org.eclipse.vorto.codegen.examples.lwm2m.generated.LWM2M.Object;
import org.eclipse.vorto.codegen.examples.lwm2m.generated.LWM2M.Object.Resources;
import org.eclipse.vorto.codegen.examples.lwm2m.generated.LWM2M.Object.Resources.Item;
import org.eclipse.vorto.codegen.examples.lwm2m.utils.ResourceIdComparator;
import org.eclipse.vorto.core.api.model.datatype.Constraint;
import org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType;
import org.eclipse.vorto.core.api.model.datatype.Presence;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.datatype.PropertyType;
import org.eclipse.vorto.core.api.model.datatype.impl.PrimitivePropertyTypeImpl;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.Fault;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Param;
import org.eclipse.vorto.core.api.model.functionblock.ReturnType;
import org.eclipse.vorto.core.api.model.functionblock.Status;

/**
 * Function Block XML generation template which contain the context specific outcome logic.
 * 
 *
 */
public class FunctionBlockXmlTemplate extends LWM2MConstants implements ITemplate<FunctionblockModel> {

   private static final String    STR_ABORT_GENERATOR                              = " - abort Generator!";

   private final SortedSet<Item>  resourceIdSet                                    = new TreeSet<Item>(
                                                                                      new ResourceIdComparator() );
   /**
    * Retrieves the content of the XML generation template.
    * 
    * @param model the {@link FunctionblockModel}
    * @return generated content for the specified model
    */
   public String getContent( final FunctionblockModel model, InvocationContext context) {
      final String name = model.getName(); // FB name

      try {
         final LWM2M lwm2m = new LWM2M();
         final List<Object> lwm2mObjects = lwm2m.getObject();

         final FunctionBlock functionblock = model.getFunctionblock();
         
         if (functionblock == null) {
        	 throw new RuntimeException("This model has no function block.");
         }

         final Object lwm2mObject = new Object();
         lwm2mObject.setObjectType( "MODefinition" ); // waiting for Issue in
                                                      // https://github.com/OpenMobileAlliance/OMA-LwM2M-Public-Review/issues
         lwm2mObject.setName( name );

         final IMapped<FunctionblockModel> mappedFunctionblock = context.getMappedElement(model, STEREOTYPE_OBJECT);
                  
         lwm2mObject.setObjectID(Integer.parseInt(mappedFunctionblock.getAttributeValue(OBJECT_ID, "0")));
         lwm2mObject.setObjectURN(mappedFunctionblock.getAttributeValue(OBJECT_URN, "TBD"));
         lwm2mObject.setMultipleInstances(mappedFunctionblock.getAttributeValue(ATTR_MULTIPLE_INSTANCES_MULTIPLE_VALUE, "Single"));
         lwm2mObject.setMandatory(mappedFunctionblock.getAttributeValue(ATTRIBUTE_MANDATORY, "Optional"));
         lwm2mObject.setDescription1( model.getDescription());
         lwm2mObject.setDescription2(mappedFunctionblock.getAttributeValue(ATTRIBUTE_DESCRIPTION2, ""));
         
         // handle config ------------------------------------------------------
         final Configuration configuration = functionblock.getConfiguration();
         if( configuration != null ) {
            final EList<Property> configProps = configuration.getProperties();
            // configuration ==> operations : RW (default)
            handleProperties( lwm2mObject, configProps, "RW", context);
         }

         // handle status ------------------------------------------------------
         final Status status = functionblock.getStatus();
         if( status != null ) {
            final EList<Property> statusProps = status.getProperties();
            // status ==> operations : R
            handleProperties( lwm2mObject, statusProps, "R", context);
         }

         // handle operations --------------------------------------------------
         handleOperations( functionblock, lwm2mObject,context );

         // handle fault -------------------------------------------------------
         final Fault fault = functionblock.getFault();
         if( fault != null ) {
            final String errMsg = "Unsupported element <fault>" + STR_ABORT_GENERATOR;
            throw new IllegalArgumentException( errMsg );
         }

         // handle events
         final EList<Event> events = functionblock.getEvents();
         if( !events.isEmpty() ) {
            final String errMsg = "Unsupported element <events>" + STR_ABORT_GENERATOR;
            throw new IllegalArgumentException( errMsg );
         }

         addSortedResourceIds( name, lwm2mObject );

         lwm2mObjects.add( lwm2mObject );

         return handleMarshalling( lwm2m, name );
      }
      catch( final RuntimeException e ) {
         throw e;
      }
   }

   private void addSortedResourceIds( final String functionBlockName, final Object lwm2mObject ) {
      final Resources lwm2mResources = new Resources();
      final List<Item> resourceItems = lwm2mResources.getItem();

      if( resourceIdSet.isEmpty() ) {
         final String errMsg = "Empty Function Block <" + functionBlockName + "> (contains no resources)"
            + STR_ABORT_GENERATOR;
         throw new IllegalArgumentException( errMsg );
      }

      resourceItems.addAll( resourceIdSet );
      lwm2mObject.setResources( lwm2mResources );
   }

   private String handleMarshalling( final LWM2M lwm2m, final String functionBlockName ) {
      JAXBContext jaxbContext;
      String content = "";
      try {
         jaxbContext = JAXBContext.newInstance( LWM2M.class );
         Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
         jaxbMarshaller = jaxbContext.createMarshaller();
         jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
         jaxbMarshaller.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );

         jaxbMarshaller.setProperty( Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION,
            "http://openmobilealliance.org/tech/profiles/LWM2M.xsd" );

         final java.io.StringWriter sw = new StringWriter();
         jaxbMarshaller.marshal( lwm2m, sw );
         content = sw.toString();

      }
      catch( final Exception ex) {
         throw new IllegalStateException( "Marshal Exception for Function Block <" + functionBlockName + ">"
            + STR_ABORT_GENERATOR + "\n", ex);
      }
      return content;
   }

   private void handleOperations( final FunctionBlock functionblock, final Object lwm2mObject, final InvocationContext context) {
      final EList<Operation> operations = functionblock.getOperations();
      for( final Operation operation : operations ) {
         final Item item = new Item();

         handleMappingRulesForOperations( lwm2mObject, operation, item, context );

         // handle name
         final String operName = operation.getName();

         item.setName( operName ); // 1:1 mapping

         // handle operation ==> operations : E
         item.setOperations( "E" );

         // handle description
         String operDescr = operation.getDescription();

         if( null == operDescr ) {
            operDescr = ""; // as default
         }
         item.setDescription( operDescr );

         // handle operation parameters
         final EList<Param> params = operation.getParams();
         if( !params.isEmpty() ) {
            final String errMsg = "Unsupported operation <" + operName
               + "> with parameter(s) - Generator only supports One-way operations without parameter(s)!";
            throw new IllegalArgumentException( errMsg );
         }
         final ReturnType returnType = operation.getReturnType();
         if( returnType != null ) {
            final String errMsg = "Unsupported operation <" + operName
               + "> with return Type - Generator only supports One-way operations without parameter(s)!";
            throw new IllegalArgumentException( errMsg );
         }

         checkResourceIdConflictAndFillSet( item );
      }
   }

   /**
    * handle mapping rules of operations defined in
    * {@link FunctionBlockXmlTemplate#VORTO_IM_MAPPING_RULES_FILE_NAME} for
    * {@link FunctionBlockXmlTemplate#ATTRIBUTE_ID},
    * {@link FunctionBlockXmlTemplate#ATTRIBUTE_MULTIPLE_INSTANCES}
    * {@link FunctionBlockXmlTemplate#ATTRIBUTE_MANDATORY}
    * {@link FunctionBlockXmlTemplate#ATTRIBUTE_TYPE}
    * {@link FunctionBlockXmlTemplate#ATTRIBUTE_RANGE_ENUMERATION},
    * {@link FunctionBlockXmlTemplate#ATTRIBUTE_UNITS}.
    * 
    * @param lwm2mObject the LWM2M object
    * @param operation the current {@link Operation}
    * @param item the current {@link Item}
    */
   private void handleMappingRulesForOperations( final Object lwm2mObject, final Operation operation, final Item item, InvocationContext context ) {
      final IMapped<Operation> mappingRule = context.getMappedElement(operation, STEREOTYPE_RESOURCE);

      item.setID(Short.parseShort(mappingRule.getAttributeValue(ATTRIBUTE_ID, "0")));
      item.setMultipleInstances(mappingRule.getAttributeValue(ATTRIBUTE_MULTIPLE_INSTANCES, ATTR_MULTIPLE_INSTANCES_SINGLE_VALUE));
      item.setMandatory( mappingRule.getAttributeValue(ATTRIBUTE_MANDATORY, ATTR_MANDATORY_MANDATORY_VALUE) );
      item.setType( mappingRule.getAttributeValue(ATTRIBUTE_TYPE, "String")  );
      item.setRangeEnumeration( mappingRule.getAttributeValue(ATTRIBUTE_RANGE_ENUMERATION,""));
      item.setUnits( mappingRule.getAttributeValue(ATTRIBUTE_UNITS, "")  );
   }

   private void checkResourceIdConflictAndFillSet(final Item item) {
      if (!resourceIdSet.isEmpty() && resourceIdSet.contains(item)) {
         final String errMsg = "Resource ID <" + item.getID() + "> conflict for <" + item.getName() + ">"
		    + STR_ABORT_GENERATOR;
		 throw new IllegalArgumentException(errMsg);
      }
	  resourceIdSet.add(item);
   }

   private void handleProperties( final Object lwm2mObject, final EList<Property> properties, final String operation, final InvocationContext context) {
      for( final Property property : properties ) {
         final Item item = new Item();

         item.setOperations( operation );

         handleMappingRulesForProperties( lwm2mObject, property, item, context);

         // handle mandatory
         final Presence mandatory = property.getPresence();

         if( mandatory == null || !mandatory.isMandatory() ) {
            item.setMandatory( ATTR_MANDATORY_OPTIONAL_VALUE );		// default mapping
         }
         else {
            item.setMandatory( ATTR_MANDATORY_MANDATORY_VALUE );
         }

         // handle multiplicity
         final boolean multiplicity = property.isMultiplicity();

         if( multiplicity ) {
            item.setMultipleInstances( ATTR_MULTIPLE_INSTANCES_MULTIPLE_VALUE );
         }
         else {
            item.setMultipleInstances( ATTR_MULTIPLE_INSTANCES_SINGLE_VALUE );
         }

         // handle name
         final String statusPropertyName = property.getName();

         item.setName( statusPropertyName ); // 1:1 mapping

         handleRangeEnumeration( lwm2mObject, property, item );

         // handle type
         final PropertyType type = property.getType();
         if( type instanceof PrimitivePropertyTypeImpl ) {
            final String primitiveTypeStr = ( (PrimitivePropertyTypeImpl) type ).getType().toString();

            final String lwm2mTypeStr = propertyType2Lwm2mType( primitiveTypeStr );
            item.setType( lwm2mTypeStr );
         }
         else {
            final String errMsg = unsupportedValueForPropertyMsg( "PropertyType (Enum/Entity)", property, lwm2mObject );
            throw new IllegalArgumentException( errMsg );
         }

         // handle Description
         String statusPropDescr = property.getDescription();
         if( null == statusPropDescr ) {
            statusPropDescr = ""; // as default
         }
         item.setDescription( statusPropDescr );

         checkResourceIdConflictAndFillSet( item );
      }
   }

   private void handleRangeEnumeration( final Object lwm2mObject, final Property property, final Item item ) {
	   if (property.getConstraintRule() != null) {
	      final EList<Constraint> constraints = property.getConstraintRule().getConstraints();

      final StringBuilder rangeSb = new StringBuilder();
      for( final Constraint constraint : constraints ) {
         final ConstraintIntervalType type = constraint.getType();

         final String constraintValues = constraint.getConstraintValues();

         if( type == ConstraintIntervalType.MIN ) {
            rangeSb.append( constraintValues ).append( '-' );
         }
         else if( type == ConstraintIntervalType.MAX ) {
            rangeSb.append( constraintValues );
         }
         else if( type == ConstraintIntervalType.STRLEN ) {
            rangeSb.append( "0-" ).append( constraintValues ).append( " bytes" );
         }
         else {
            final String errMsg = unsupportedValueForPropertyMsg( "ConstraintIntervalType <" + type + ">", property,
               lwm2mObject );
            throw new IllegalArgumentException( errMsg );
         }
      }

      item.setRangeEnumeration( rangeSb.toString() );
   }
   }

   /**
    * handle mapping rules of properties defined in
    * {@link FunctionBlockXmlTemplate#VORTO_IM_MAPPING_RULES_FILE_NAME} for
    * {@link FunctionBlockXmlTemplate#ATTRIBUTE_ID} and
    * {@link FunctionBlockXmlTemplate#ATTRIBUTE_UNITS}.
    * 
    * @param lwm2mObject the LWM2M object
    * @param property the current {@link Property}
    * @param item the current {@link Item}
    */
   private void handleMappingRulesForProperties( final Object lwm2mObject, final Property property, final Item item, final InvocationContext context) {
      final IMapped<Property> propertyMapping = context.getMappedElement(property, STEREOTYPE_RESOURCE);
      
      item.setID(Short.parseShort(propertyMapping.getAttributeValue(ATTRIBUTE_ID, Integer.toString(resourceIdSet.size()))));
      item.setUnits(propertyMapping.getAttributeValue(ATTRIBUTE_UNITS, ""));
      item.setOperations(propertyMapping.getAttributeValue(ATTRIBUTE_OPERATIONS, item.getOperations()));
   }

   private String unsupportedValueForPropertyMsg( final String value, final Property property, final Object lwm2mObject ) {
      return "Unsupported " + value + " for property <" + property.getName() + "> of <" + lwm2mObject.getName() + ">"
         + STR_ABORT_GENERATOR;
   }

   private String propertyType2Lwm2mType( final String primitiveTypeStr ) {
      String lwm2mTypeStr = "";
      switch( primitiveTypeStr ) {
         case "boolean":
            lwm2mTypeStr = "Boolean";
            break;
         case "dateTime":
            lwm2mTypeStr = "Time";
            break;
         case "float":
         case "double":
            lwm2mTypeStr = "Float";
            break;
         case "int":
         case "byte":
         case "short":
         case "long":
            lwm2mTypeStr = "Integer";
            break;
         case "string":
            lwm2mTypeStr = "String";
            break;
         case "base64Binary":
            lwm2mTypeStr = "Opaque";
            break;
         default:
            final String errMsg = "Unknown PropertyType: " + primitiveTypeStr;
            throw new IllegalArgumentException( errMsg );
      }
      return lwm2mTypeStr;
   }   
}
