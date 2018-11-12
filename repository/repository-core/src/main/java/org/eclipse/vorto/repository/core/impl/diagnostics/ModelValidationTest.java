package org.eclipse.vorto.repository.core.impl.diagnostics;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.impl.RepositoryDiagnostics.NodeDiagnosticTest;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class ModelValidationTest implements NodeDiagnosticTest {

	@Autowired
	private ModelParserFactory modelParserFactory;
	
	private static Logger logger = Logger.getLogger(ModelValidationTest.class);
	
	@Override
	public Collection<Diagnostic> apply(Node node) {
		Objects.requireNonNull(node);
		
		Collection<Diagnostic> diagnostics = Lists.newArrayList();
		
		try {
			Node contentNode = node.getNode("jcr:content");
			if (contentNode == null) {
				diagnostics.add(new Diagnostic(ModelIdHelper.fromPath(node.getPath()), "Model node is empty. No <jcr:content>."));
			}
			
			Property contentProperty = contentNode.getProperty("jcr:data");
			if (contentProperty == null) {
				diagnostics.add(new Diagnostic(ModelIdHelper.fromPath(node.getPath()), "Model node has no file. No <jcr:data> property."));
			}
			
			try {
				logger.debug("Validating \n" + IOUtils.toString(contentProperty.getBinary().getStream()));
				modelParserFactory.getParser(node.getName()).parse(contentProperty.getBinary().getStream());
			} catch(ValidationException e) {
				diagnostics.add(new Diagnostic(ModelIdHelper.fromPath(node.getPath()), e.getMessage()));
			}
			
		} catch (RepositoryException | IOException e) {
			throw new NodeDiagnosticException("Exception while trying to validate node.", e);
		}
		
		return diagnostics;
	}

	public void setModelParserFactory(ModelParserFactory modelParserFactory) {
		this.modelParserFactory = modelParserFactory;
	}
}
