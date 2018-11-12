package org.eclipse.vorto.repository.core.impl.diagnostics;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.impl.RepositoryDiagnostics.NodeDiagnosticTest;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class MetadataIntegrityTest implements NodeDiagnosticTest {

	@Autowired
	private ModelParserFactory modelParserFactory;
	
	@Override
	public Collection<Diagnostic> apply(Node node) {
		return NodeDiagnosticUtils.getModel(modelParserFactory, node).map(modelInfo -> {
			Collection<Diagnostic> diagnostics = Lists.newArrayList();
			
			checkNodePropertyError(node, "vorto:name", modelInfo.getId().getName())
				.ifPresent(diagnostic -> diagnostics.add(diagnostic));
			
			checkNodePropertyError(node, "vorto:namespace", modelInfo.getId().getNamespace())
				.ifPresent(diagnostic -> diagnostics.add(diagnostic));
			
			checkNodePropertyError(node, "vorto:version", modelInfo.getId().getVersion())
				.ifPresent(diagnostic -> diagnostics.add(diagnostic));
			
			checkNodePropertyError(node, "vorto:description", modelInfo.getDescription())
				.ifPresent(diagnostic -> diagnostics.add(diagnostic));
			
			checkNodePropertyError(node, "vorto:displayname", modelInfo.getDisplayName())
				.ifPresent(diagnostic -> diagnostics.add(diagnostic));
						
			return diagnostics;
		}).orElse(Collections.emptyList());
	}

	private Optional<Diagnostic> checkNodePropertyError(final Node node, final String nodePropertyName, final String expectedValue) {
		ModelId modelId = null;
		try {
			modelId = ModelIdHelper.fromPath(node.getPath());
			if (!node.getProperty(nodePropertyName).getString().equals(expectedValue)) {
				String message = new StringBuilder("Expected value for node property '")
						.append(nodePropertyName)
						.append("' is '")
						.append(expectedValue)
						.append("' but is '")
						.append(node.getProperty(nodePropertyName).getString())
						.append("'")
						.toString();
				
				return Optional.of(new Diagnostic(modelId, message));
			}
			
			return Optional.empty();
		} catch (RepositoryException e) {
			return Optional.of(new Diagnostic(modelId, "Got exception while checking node '" + nodePropertyName + "' : " + e.getMessage()));
		}
	}
}
