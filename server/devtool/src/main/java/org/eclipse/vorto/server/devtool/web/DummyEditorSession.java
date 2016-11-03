package org.eclipse.vorto.server.devtool.controller.session;

import org.springframework.stereotype.Component;

@Component
public class DummyEditorSession implements IEditorSession {

	@Override
	public String getUser() {
		return "admin";
	}

}
