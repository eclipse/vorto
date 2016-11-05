package org.eclipse.vorto.server.devtool.web;

import org.springframework.stereotype.Component;

@Component
public class DummyEditorSession implements IEditorSession {

	@Override
	public String getUser() {
		return "admin";
	}

}
