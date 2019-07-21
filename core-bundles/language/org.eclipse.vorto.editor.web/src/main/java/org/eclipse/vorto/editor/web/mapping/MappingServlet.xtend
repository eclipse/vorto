package org.eclipse.vorto.editor.web.mapping

import com.google.inject.Injector
import javax.servlet.annotation.WebServlet
import org.eclipse.xtext.util.DisposableRegistry
import org.eclipse.xtext.web.servlet.HttpServiceContext
import org.eclipse.xtext.web.servlet.XtextServlet

@WebServlet(name = 'Mapping Model XtextServices', urlPatterns = '/mapping/xtext-service/*')
class MappingServlet extends XtextServlet {
	
	DisposableRegistry disposableRegistry
	
	Injector injector; 
	
	override init() {
		super.init()
		injector = new MappingWebSetup().createInjectorAndDoEMFRegistration()
		disposableRegistry = injector.getInstance(DisposableRegistry)
	}
	
	override destroy() {
		System.out.println("disposing mapping servlet")
		if (disposableRegistry !== null) {
			disposableRegistry.dispose()
			disposableRegistry = null
		}
		super.destroy()
	}
	
	override getInjector(HttpServiceContext serviceContext) {
		return injector
	}
	
}