package org.eclipse.vorto.editor.web.functionblock

import com.google.inject.Injector
import javax.servlet.annotation.WebServlet
import org.eclipse.xtext.util.DisposableRegistry
import org.eclipse.xtext.web.servlet.HttpServiceContext
import org.eclipse.xtext.web.servlet.XtextServlet

@WebServlet(name = 'Functionblock XtextServices', urlPatterns = '/functionblock/xtext-service/*')
class FunctionblockServlet extends XtextServlet {
	
	DisposableRegistry disposableRegistry
	
	Injector injector; 
	
	override init() {
		super.init()
		injector = new FunctionblockWebSetup().createInjectorAndDoEMFRegistration()
		disposableRegistry = injector.getInstance(DisposableRegistry)
	}
	
	override destroy() {
		System.out.println("disposing functionblock servlet")
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