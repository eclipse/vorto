package org.eclipse.vorto.editor.web.datatype

import com.google.inject.Injector
import javax.servlet.annotation.WebServlet
import org.eclipse.xtext.util.DisposableRegistry
import org.eclipse.xtext.web.servlet.HttpServiceContext
import org.eclipse.xtext.web.servlet.XtextServlet

@WebServlet(name = 'Datatype XtextServices', urlPatterns = '/datatype/xtext-service/*')
class DatatypeServlet extends XtextServlet {
	
	DisposableRegistry disposableRegistry
	
	Injector injector; 
		
	override init() {
		super.init()
		injector = new DatatypeWebSetup().createInjectorAndDoEMFRegistration()
		disposableRegistry = injector.getInstance(DisposableRegistry)
	}
	
	override destroy() {
		System.out.println("disposing datatype servlet")
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