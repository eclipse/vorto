package org.eclipse.vorto.codegen.webui.templates.service.bosch.auth

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class IMUserInfoTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''IMUserInfo.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch/permissions'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch.permissions;
		
		import java.util.Collection;
		
		import org.springframework.security.core.GrantedAuthority;
		import org.springframework.security.core.userdetails.UserDetails;
		
		import com.bosch.im.api2.jwt.IAuthorizationToken;
		
		public class IMUserInfo implements UserDetails {
		
			private static final long serialVersionUID = 1L;
				
			private String username;
			private String tenantName;
			private String password;
			private IAuthorizationToken authorizationToken;
		
			public IMUserInfo(String username, String tenantName, String password, IAuthorizationToken authorizationToken) {
				this.username = username;
				this.tenantName = tenantName;
				this.password = password;
				this.authorizationToken = authorizationToken;
			}
		
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return null;
			}
			
			public String getUsername() {
				return username;
			}
			
			public String getTenantName() {
				return tenantName;
			}
				
			public boolean isAccountNonExpired() {
				return false;
			}
		
			public boolean isAccountNonLocked() {
				return false;
			}
		
			public boolean isCredentialsNonExpired() {
				return false;
			}
		
			public boolean isEnabled() {
				return false;
			}
		
			@Override
			public String getPassword() {
				return password;
			}
			
			public IAuthorizationToken getAuthorizationToken() {
				return this.authorizationToken;
			}	
		}

		
		'''
	}
	
}
