package org.eclipse.vorto.codegen.examples.webui.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class CssTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		"style.css"
	}
	
	override getPath(InformationModel context) {
		"webdevice.example/src/main/resources/static/css";
	}
	
	override getContent(InformationModel context,InvocationContext invocationContext) {
		'''
		/*Button Customization*
		=========================*/
		.button-extra {
			margin-right: 4px;
		
		}
		.btn {
			text-transform: capitalize;
			background-color: #0081C7
		}
		
		/*Navigation Customization*
		=========================*/
		
		.navbar-default {
		  background-color: #0081c7;
		  border-color: #006ca6;
		}
		.navbar-default .navbar-brand {
		 color: #eeeeee;
		}
		.navbar-default .navbar-brand:hover,
		.navbar-default .navbar-brand:focus {
		  color: #5e5e5e;
		  background-color: transparent;
		}
		.navbar-default .navbar-text {
		  color: #eeeeee;
		}
		.navbar-default .navbar-nav > li > a {
		  color: #eeeeee;
		}
		.navbar-default .navbar-nav > li > a:hover,
		.navbar-default .navbar-nav > li > a:focus {
		  color: #f9f2f2;
		  background-color: transparent;
		}
		.navbar-default .navbar-nav > .active > a,
		.navbar-default .navbar-nav > .active > a:hover,
		.navbar-default .navbar-nav > .active > a:focus {
		  color: #555555;
		  background-color: #006ca6;
		}
		.navbar-default .navbar-nav > .disabled > a,
		.navbar-default .navbar-nav > .disabled > a:hover,
		.navbar-default .navbar-nav > .disabled > a:focus {
		  color: #cccccc;
		  background-color: transparent;
		}
		
		
		/*Panel Customization*
		=========================*/
		
		.panel-primary {
		  border-color: #0081c7;
		}
		.panel-primary > .panel-heading {
		  color: #ffffff;
		  background-color: #0081c7;
		  border-color: #0081c7;
		}
		.panel-primary > .panel-heading + .panel-collapse > .panel-body {
		  border-top-color: #0081c7;
		}
		.panel-primary > .panel-heading .badge {
		  color: #0081c7;
		  background-color: #ffffff;
		}
		.panel-primary > .panel-footer + .panel-collapse > .panel-body {
		  border-bottom-color: #0081c7;
		}
		
		/* Datatype Borders*
		=========================*/
		
		#datatypebox{
		    border: 1px solid #0081c7;
		    margin : 7px 2px 2px 2px ;
		    //width: 200px;
		    //height: 200px;
		}
		
		#datatypebox #datatypetitle{
		    text-align: left;
		    margin-top: -10px;
		    height: 20px;
		    line-height: 20px;
		    font-size: 15px;
		}
		
		#datatypebox #datatypetitle #datatypetitletext{
		    background-color: white;
		}
		'''
	}
	
} 