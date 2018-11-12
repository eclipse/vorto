package org.eclipse.vorto.repository.core.impl.diagnostics;

import java.util.Optional;

import javax.jcr.Node;
import javax.jcr.Property;

import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;

public class NodeDiagnosticUtils {
	
	public static Optional<ModelInfo> getModel(ModelParserFactory parserFactory, Node node) {
		try {
			Node contentNode = node.getNode("jcr:content");
			if (contentNode == null) {
				return Optional.empty();
			}
			
			Property contentProperty = contentNode.getProperty("jcr:data");
			if (contentProperty == null) {
				return Optional.empty();
			}		
		
			return Optional.of(parserFactory.getParser(node.getName()).parse(contentProperty.getBinary().getStream()));
		} catch(Exception e) {
			return Optional.empty();
		}
	}
	
}
