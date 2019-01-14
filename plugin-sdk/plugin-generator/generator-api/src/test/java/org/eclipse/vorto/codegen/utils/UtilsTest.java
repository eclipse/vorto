package org.eclipse.vorto.codegen.utils;

import org.eclipse.vorto.core.api.model.ModelConversionUtils;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockFactoryImpl;
import org.eclipse.vorto.core.api.model.functionblock.impl.ReturnDictonaryTypeImpl;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.api.content.BooleanAttributeProperty;
import org.eclipse.vorto.repository.api.content.BooleanAttributePropertyType;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UtilsTest {
    ArrayList<FunctionblockModel> fbms = new ArrayList<>();

    @Before public void setup() {
        ModelWorkspaceReader.init();
        IModelWorkspace workspace = IModelWorkspace.newReader()
            .addFile(getClass().getClassLoader().getResourceAsStream("SomeFb.fbmodel"),
                ModelType.Functionblock)
            .addFile(getClass().getClassLoader().getResourceAsStream("SuperFb.fbmodel"),
                ModelType.Functionblock)
            .addFile(getClass().getClassLoader().getResourceAsStream("com.ipso.smartobjects_Push_button_0_0_1.fbmodel"),
                ModelType.Functionblock)
            .addFile(getClass().getClassLoader().getResourceAsStream("SuperSuperFb.fbmodel"),
                ModelType.Functionblock).read();
        fbms.add(ModelConversionUtils.convertToFlatHierarchy((FunctionblockModel) workspace.get().get(0)));
        fbms.add(ModelConversionUtils.convertToFlatHierarchy((FunctionblockModel) workspace.get().get(1)));
        fbms.add(ModelConversionUtils.convertToFlatHierarchy((FunctionblockModel) workspace.get().get(2)));
        fbms.add(ModelConversionUtils.convertToFlatHierarchy((FunctionblockModel) workspace.get().get(3)));


    }

    @Test public void wrapFunctionBlock() {
        InformationModel infomodel = Utils.wrapFunctionBlock(fbms.get(0));
        assertNull(infomodel.getCategory());
        assertEquals("SomeFbIM", infomodel.getName());
        assertEquals("iot", infomodel.getNamespace());
        assertEquals("", infomodel.getDescription());
        assertEquals("0.0.1", infomodel.getVersion());
        assertEquals("SomeFb", infomodel.getDisplayname());
    }

    @Test public void getReferencedEntities() {
        for(FunctionblockModel fbm: fbms){
            assertEquals(0, Utils.getReferencedEntities(fbm.getFunctionblock()).size());
        }

    }

    @Test public void getReferencedEnums() {
        for(FunctionblockModel fbm: fbms){
            assertEquals(0, Utils.getReferencedEnums(fbm.getFunctionblock()).size());
        }
    }

    @Test public void getReferencedTypesForType() {
        //Todo
    }

    @Test public void getReferencedTypesForProperty() {
        //Todo
    }

    @Test public void getReferencedTypesForFb() {
        assertEquals(1, Utils.getReferencedTypes(fbms.get(0).getFunctionblock()).size());
        assertEquals(0, Utils.getReferencedTypes(fbms.get(1).getFunctionblock()).size());
        assertEquals(0, Utils.getReferencedTypes(fbms.get(2).getFunctionblock()).size());
        assertEquals(0, Utils.getReferencedTypes(fbms.get(3).getFunctionblock()).size());

    }
}
