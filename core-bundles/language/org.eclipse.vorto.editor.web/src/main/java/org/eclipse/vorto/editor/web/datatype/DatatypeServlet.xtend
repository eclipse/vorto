package org.eclipse.vorto.editor.web.datatype

import javax.servlet.annotation.WebServlet
import org.eclipse.xtext.web.servlet.XtextServlet

@WebServlet(name = 'Datatype XtextServices', urlPatterns = '/datatype/xtext-service/*')
class DatatypeServlet extends XtextServlet {
	
}