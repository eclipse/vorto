/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.modules.fbmodel

import org.eclipse.vorto.codegen.api.Generated
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask
import org.eclipse.vorto.codegen.api.IGeneratedWriter
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.Param
import org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam
import org.eclipse.vorto.core.api.model.functionblock.RefParam
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType
import org.eclipse.vorto.core.api.model.functionblock.ReturnType

class CXFCodeGeneratorTask implements ICodeGeneratorTask<FunctionblockModel> {
	
	private static final String JAXB_XML_FILENAME= "jaxb-bindings.xjb";
	private String cxfXmlFileName;
	private String cxfXsdFileName;
	
	private PropertyElementBuilder propBuilder = new PropertyElementBuilder;
	
	FbModelWrapper wrappedfbm;

	override generate(FunctionblockModel model, InvocationContext context, IGeneratedWriter outputter) {
		wrappedfbm = new FbModelWrapper(model);
		cxfXmlFileName = wrappedfbm.getUpperCaseFunctionblockName()+".xml";
		cxfXsdFileName = wrappedfbm.getUpperCaseFunctionblockName()+".xsd";
					
		outputter.write(new Generated( cxfXmlFileName , getSrcGenFolder, getXMLContent().toString()));
		outputter.write(new Generated( cxfXsdFileName , getSrcGenFolder, getXSDContent().toString()));
		outputter.write(new Generated( JAXB_XML_FILENAME, getSrcGenFolder, getBindXMLContent().toString()));
	}

	def String getSrcGenFolder() {
		return '''com.bosch.« wrappedfbm.functionBlockName.toLowerCase »-model/src-gen'''
	}

	def getXSDContent() {
		toXSD(wrappedfbm.model)
	}

	def getBindXMLContent() {
		createBindingsXml(wrappedfbm.model)
	}

	def getXMLContent() {
		var fbm = wrappedfbm.model
		toXML(wrappedfbm.model, fbm.functionblock.eContents.filter(typeof(Operation)))
	}


	private def createBindingsXml(FunctionblockModel model) '''
		<jaxb:bindings xmlns:jaxb="http://java.sun.com/xml/ns/jaxb" xmlns:xs="http://www.w3.org/2001/XMLSchema"
		xmlns:xjc="http://java.sun.com/xml/ns/jaxb/xjc" jaxb:extensionBindingPrefixes="xjc" jaxb:version="2.1">
			<jaxb:globalBindings>
				<xjc:serializable uid="1" />
				<xjc:superInterface name="java.io.Serializable" />
				<!-- use simple minded binding see http://weblogs.java.net/blog/2006/03/03/why-does-jaxb-put-xmlrootelement-sometimes-not-always -->
				<xjc:simple />
			</jaxb:globalBindings>
			<jaxb:bindings schemaLocation="../../generated-resources/OSGI-INF/functionblock/«wrappedfbm.
			getUpperCaseFunctionblockName()».xsd">
				<jaxb:bindings node="//xs:element[@name='«wrappedfbm.getLowerCaseFunctionblockName()»']">
					<jaxb:class name="«wrappedfbm.getUpperCaseFunctionblockName()»Properties"></jaxb:class>
				</jaxb:bindings>
			</jaxb:bindings>
		</jaxb:bindings>
	'''

	private def toXML(FunctionblockModel model, Iterable<Operation> operations) '''
		<?xml version='1.0' encoding='UTF-8'?>
		<tns:service eventConsumer="false" revision="$Rev:$"
			javaPackageName="«wrappedfbm.getJavaPackageName()»"
			name="«model.name»"
			namespace="«wrappedfbm.getFullNamespace»"
			xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
			<tns:documentation>«model.description»</tns:documentation>
			«FOR op : operations»
			«IF op.returnType == null»
			<tns:oneWayOperation name="«op.name»" empty="«paramCount(op)»">
					«IF op.description != null»
					<tns:documentation>«op.description»</tns:documentation>
					«ENDIF»
			</tns:oneWayOperation>
			«ELSE»
			<tns:requestResponseOperation name="«op.name»" empty="«paramCount(op)»">
				«IF op.description != null»
				<tns:documentation>«op.description»</tns:documentation>
				«ENDIF»
			</tns:requestResponseOperation>	
			«ENDIF»
			«ENDFOR»
		</tns:service>	
			'''

	private def String paramCount(Operation op) {
		if (op.params.empty)
			"true"
		else
			"false"
	}
	
	private def toXSD(FunctionblockModel model) '''
		<?xml version="1.0" encoding="UTF-8"?>
		<xs:schema
		   xmlns:xs="http://www.w3.org/2001/XMLSchema"
		   xmlns:tns="«wrappedfbm.getFullNamespace()»"
		   targetNamespace="«wrappedfbm.getFullNamespace()»"
		   elementFormDefault="qualified">
		   «FOR reference : wrappedfbm.types»
		   «IF reference instanceof Entity»
		   <xs:complexType name="«reference.name»">
		      «IF (reference as Entity).properties != null && !(reference as Entity).properties.isEmpty»
		      <xs:sequence>
		         «FOR property : (reference as Entity).properties»
		         «propBuilder.buildProperty(property)»
		      	 «ENDFOR»
		      </xs:sequence>
		      «ENDIF»
		   </xs:complexType>
		   «ENDIF»
		   «IF reference instanceof Enum»
		   <xs:simpleType name="«reference.name»">
		      <xs:restriction base="xs:string">
		   	     «FOR l : (reference as Enum).enums»
		   	     <xs:enumeration value="«l.name»"/>
		   	     «ENDFOR»
		      </xs:restriction>
		   </xs:simpleType>
		   «ENDIF»
		   «ENDFOR»
		   «FOR operation : model.functionblock.eContents.filter(typeof(Operation))»
		   «IF operation.returnType != null»
		   <xs:element name="«operation.name»Reply">
		   		<xs:complexType>
		   			<xs:sequence>
		   				<xs:element name="«getOperationElementName(operation)»" «getDataType(operation.returnType)» «defineMultiplicity(
			operation.returnType.multiplicity)»/>
		   			</xs:sequence>
		   		</xs:complexType>
		   </xs:element>
		   «ENDIF»
		   «IF !operation.params.empty»
		   <xs:element name="«operation.name»">
		   		<xs:complexType>
		   			<xs:sequence>
		   				«FOR param : operation.params»
		   				<xs:element name="«param.name»" «getDataType(param)» «defineMultiplicity(param.multiplicity)»/>
		   				«ENDFOR»
		   			</xs:sequence>
		   		</xs:complexType>
		   </xs:element>
		   «ENDIF»
		   «ENDFOR»
		   <xs:element name="«wrappedfbm.getLowerCaseFunctionblockName()»">
		   		«IF model.description != null»
		   		<xs:annotation>
		   			<xs:documentation>«model.description»</xs:documentation>
		   		</xs:annotation>
		   		«ENDIF»
		   		<xs:complexType>
		   			<xs:sequence>
		   				<xs:element name="fault">
		   					«IF model.functionblock.fault != null»
		   					<xs:complexType>
		   						<xs:sequence>
		   							«FOR feature : model.functionblock.fault.properties»
		   							«propBuilder.buildProperty(feature)»
		   							«ENDFOR»
		   						</xs:sequence>
		   					</xs:complexType>
		   					«ENDIF»
		   				</xs:element>
		   				<xs:element name="configuration">
		   					«IF model.functionblock.configuration != null»
		   					<xs:complexType>
		   						<xs:sequence>
		   							«FOR feature : model.functionblock.configuration.properties»
		   							«propBuilder.buildProperty(feature)»
		   							«ENDFOR»
		   						</xs:sequence>
		   					</xs:complexType>
		   					«ENDIF»
		   				</xs:element>
		   				<xs:element name="status">
		   					«IF model.functionblock.status != null»
		   					<xs:complexType>
		   						<xs:sequence>
		   							«FOR feature : model.functionblock.status.properties»
		   							«propBuilder.buildProperty(feature)»
		   							«ENDFOR»
		   						</xs:sequence>
		   					</xs:complexType>
		   					«ENDIF»
		   				</xs:element>
		   			</xs:sequence>
		   			«IF model.displayname != null»
		   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="«model.displayname»"/>
		   			«ENDIF»
		   		</xs:complexType>
		   	</xs:element>
		</xs:schema>
		'''

	private def CharSequence getOperationElementName(Operation operation) {
		var returnType = operation.returnType
		if (returnType instanceof ReturnObjectType) {
			var returnTypeName = (returnType as ReturnObjectType).returnType.name	
			lowerCase(returnTypeName)
		} else
			(returnType as ReturnPrimitiveType).returnType + "Value"
	}

	private def CharSequence getDataType(Param param) {
		if (param instanceof RefParam) '''type="tns:«(param as RefParam).type.name»"''' else '''type="xs:«(param as PrimitiveParam).
			type»"'''
	}

	private def CharSequence getDataType(ReturnType returnType) {
		if (returnType instanceof ReturnObjectType) '''type="tns:«(returnType as ReturnObjectType).returnType.name»"''' else '''type="xs:«(returnType as ReturnPrimitiveType).
			returnType»"'''
	}

	private def defineMultiplicity(boolean multiplicity) {
		'''maxOccurs="«IF multiplicity»unbounded«ELSE»1«ENDIF»"'''
	}

	private def String lowerCase(String line) {
		return Character.toLowerCase(line.charAt(0)) + line.substring(1);
	}

	
	
}
