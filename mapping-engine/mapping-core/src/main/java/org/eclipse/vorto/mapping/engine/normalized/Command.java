/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.mapping.engine.normalized;

import java.util.HashMap;
import java.util.Map;

public class Command {

	private String cmdName;
	private String fbPropertyName;
	
	private Map<String,Object> params = new HashMap<>();
	
	
	public static CommandBuilder forFunctionBlockProperty(String fbProperty) {
		return new CommandBuilder(fbProperty);
	}
	
	public void putParam(String name, Object value) {
		this.params.put(name, value);
	}

	public String getCmdName() {
		return cmdName;
	}

	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getFbPropertyName() {
		return fbPropertyName;
	}

	public void setFbPropertyName(String fbPropertyName) {
		this.fbPropertyName = fbPropertyName;
	}

	@Override
	public String toString() {
		return "Command [cmdName=" + cmdName + ", fbPropertyName=" + fbPropertyName + ", params=" + params + "]";
	}
	
	public static class CommandBuilder {
		private Command cmd;
		public CommandBuilder(String fbProperty) {
			this.cmd = new Command();
			this.cmd.setFbPropertyName(fbProperty);
		}
		
		public CommandBuilder name(String name) {
			this.cmd.setCmdName(name);
			return this;
		}
		
		public CommandBuilder param(String name, Object value) {
			this.cmd.getParams().put(name, value);
			return this;
		}
		
		public Command build() {
			return cmd;
		}
	}
}
