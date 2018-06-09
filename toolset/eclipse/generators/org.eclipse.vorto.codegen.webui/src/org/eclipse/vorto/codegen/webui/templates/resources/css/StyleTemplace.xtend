package org.eclipse.vorto.codegen.webui.templates.resources.css

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class StyleTemplace implements IFileTemplate<InformationModel>{
	
	override getFileName(InformationModel context) {
		'''style.css'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static/css'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''		
		.tiny-box {
			box-shadow:1px 1px 1px 2px rgba(0,0,0,.1);
			padding:0 5px;
			color:#333;
			margin:5px auto;
			min-height:114px;
			background:#fff;
			height:130px;
			width:154px;
			text-align:center
		}
		.tiny-box-icon {
			height:80px;
			width:90px;
			text-align:center;
			font-size:45px;
			line-height:70px;
			background-color:transparent!important
		}
		.tiny-box.selected,.tiny-box:hover {
			border:4px solid #c2e1f5;
			cursor:pointer
		}
		.tiny-box-name,.tiny-box-number {
			margin:0 5px;
			height:30px
		}
		.tiny-box-name span,.tiny-box-number span {
			display:inline-block;
			vertical-align:middle;
			line-height:.9em
		}
		.tiny-box-name span {
			font-weight:600
		}
		.tiny-box-number span {
			color:#005691
		}
		
		p.breakeWordWithDots {
		    overflow: hidden;
		    display: inline-block;
		    text-overflow: ellipsis;
		    white-space: nowrap;
		    direction: rtl;
		}
		'''
	}
}