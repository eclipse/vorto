package org.eclipse.vorto.codegen.examples.webui.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class PageTemplate implements IFileTemplate<FunctionblockModel> {

	override getFileName(FunctionblockModel context) {
		return context.name.toLowerCase + ".html"
	}

	override getPath(FunctionblockModel context) {
		return "webdevice.example/src/main/resources/static/pages";
	}

	override getContent(FunctionblockModel context,InvocationContext invocationContext) {
		'''
			<fieldset class="scheduler-border">
				<div class="col-md-12" style="text-align:center;">
				«FOR operation : context.functionblock.operations»
					<button ng-click="«operation.name»()" class="btn btn-primary button-extra">«operation.name»</button>
			  	«ENDFOR»
			  	</div>
			</fieldset>
			<!-- Configuration -->
			<div class="panel panel-primary">
			  <div class="panel-heading">
			    <h3 class="panel-title">
			      Configuration
			    </h3>
			  </div>
			  <fieldset class="scheduler-border">
			  	«IF context.functionblock.configuration != null»
			  		<br>
			  			<div class="col-md-12">
			  				«FOR configProperty : context.functionblock.configuration.properties»
			  					«IF configProperty.type instanceof PrimitivePropertyType»
			  						<div class="col-md-6">
			  							<label class="control-label input-label" style="width:40%;">«configProperty.name»</label>
			  							<input type="text" name="«configProperty.name»" class="ng-pristine ng-valid"/>
			  						</div>
			  					«ENDIF»
			  				«ENDFOR»
			  				<button ng-click="saveConfig();" class="btn btn-primary button-extra pull-right">Save</button>
			  			</div>    
			  	«ENDIF»
			  	</fieldset>
			  	<br/>
			  	
			</div>
			
			<!-- Status -->
			<div class="panel panel-primary">
			  <div class="panel-heading">
			    <h3 class="panel-title">
			      Status
			    </h3>
			  </div>
			  <fieldset class="scheduler-border">
			  	<br>
			  	«IF context.functionblock.status != null»
			  		«FOR statusProperty : context.functionblock.status.properties»
			  			«writeStatusProperty(context,statusProperty ,statusProperty.name)»
			  		«ENDFOR»
			  	«ENDIF»
			  </fieldset>
			</div>
			
			<!-- Fault -->
			<div class="panel panel-primary">
			  <div class="panel-heading">
			    <h3 class="panel-title">
			      Fault
			    </h3>
			  </div>
			  <fieldset class="scheduler-border">
			  </fieldset>
			</div>
		'''
	}

	def writeStatusProperty(FunctionblockModel fbModel, Property prop, String propertyValueAccessor) {
		if (prop.type instanceof ObjectPropertyType) {
			var objectPropertyType = prop.type as ObjectPropertyType

			if (objectPropertyType.type instanceof Entity) {
				var entity = objectPropertyType.type as Entity
				var result = new StringBuilder();
				for (Property _prop : entity.properties) {	
					if (_prop.type instanceof ObjectPropertyType) {
						result.append(writeStatusProperty(fbModel, _prop,propertyValueAccessor+"."+_prop.name))
					} else {
						result.append(generateInputFieldForStatus(fbModel, _prop,propertyValueAccessor+"."+_prop.name))
					}
				}
				return result.toString
			} else {
				return "";
			}
		} else {
			return generateInputFieldForStatus(fbModel, prop, propertyValueAccessor)
		}
	}

	def generateInputFieldForStatus(FunctionblockModel fbModel,
		Property property, String propertyValueAccessor) {
		'''
			<div class="col-md-6">
				<label class="control-label input-label" style="width:40%;">«property.name»</label>
				<input readonly type="text" name="«property.name»" value="{{«fbModel.name»Status.«propertyValueAccessor»}}" class="ng-pristine ng-valid"/>
			</div>
		'''
	}

}
