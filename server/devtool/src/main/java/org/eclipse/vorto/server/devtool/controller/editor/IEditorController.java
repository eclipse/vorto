package org.eclipse.vorto.server.devtool.controller.editor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.vorto.http.model.ModelResource;

public interface IEditorController {

	public void linkEditor(String resourceId, String namespace, String name, String version, HttpServletRequest request,
			HttpServletResponse response);

	List<ModelResource> searchByExpression(String expression);

}
