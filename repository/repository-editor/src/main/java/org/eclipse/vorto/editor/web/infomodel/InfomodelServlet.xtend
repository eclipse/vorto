package org.eclipse.vorto.editor.web.infomodel

import com.google.inject.Injector
import javax.servlet.annotation.WebServlet
import org.eclipse.vorto.repository.core.IModelRepositoryFactory
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.util.DisposableRegistry
import org.eclipse.xtext.web.servlet.HttpServiceContext
import org.eclipse.xtext.web.servlet.XtextServlet

@WebServlet(name = 'Information Model XtextServices', urlPatterns = '/infomodel/xtext-service/*')
@FinalFieldsConstructor
class InfomodelServlet extends XtextServlet {
	
	DisposableRegistry disposableRegistry
	
	Injector injector; 
	
	val IModelRepositoryFactory repositoryFactory
	
	override init() {
		super.init()
		injector = new InfomodelWebSetup(repositoryFactory).createInjectorAndDoEMFRegistration()
		disposableRegistry = injector.getInstance(DisposableRegistry)
	}
	
	override destroy() {
		System.out.println("disposing infomodel servlet")
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