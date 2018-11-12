package org.eclipse.vorto.repository.core.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class RepositoryDiagnostics {
	
	@Autowired
	Collection<NodeDiagnosticTest> nodeDiagnosticTests = Collections.emptyList();
	
	private static Logger logger = Logger.getLogger(RepositoryDiagnostics.class);
	private static final String FILE_MIXIN = "nt:file";
	private static final String JCR_SYSTEM_NODE_PREFIX = "/jcr:system";

	public interface NodeDiagnosticTest extends Function<Node, Collection<Diagnostic>> {}
	
	public Collection<Diagnostic> diagnose(final Node node) {
		if (node == null) {
			return Collections.emptyList();
		}
		
		Collection<Diagnostic> diagnostics = Lists.newArrayList();
		try {
			NodeIterator iterator = node.getNodes();
			while(iterator.hasNext()) {
				Node childNode = iterator.nextNode();
				if (!isSystemNode(childNode)) {
					if (isModelNode(childNode)) {
						diagnostics.addAll(diagnoseNode(childNode));
					} else {
						diagnostics.addAll(diagnose(childNode));
					}
				}
			}
		} catch (RepositoryException e) {
			// TODO : Throw proper exception here
			throw new RuntimeException(e);
		}
		
		//TODO : make diagnostics unique 
		
		return diagnostics;
	}

	private boolean isSystemNode(final Node node) throws RepositoryException {
		return node.getPath().startsWith(JCR_SYSTEM_NODE_PREFIX);
	}
	
	private boolean isModelNode(final Node node) throws RepositoryException {
		return node.isNodeType(FILE_MIXIN) && ModelParserFactory.hasParserFor(node.getName());
	}

	private Collection<Diagnostic> diagnoseNode(final Node node) throws RepositoryException {
		logger.info("Diagnosing " + node.getPath());
		Collection<Diagnostic> diagnostics = Lists.newArrayList();
		nodeDiagnosticTests.forEach(nodeDiagnosticTest -> diagnostics.addAll(nodeDiagnosticTest.apply(node)));
		return diagnostics;
	}

	public void setNodeDiagnosticTests(Collection<NodeDiagnosticTest> nodeDiagnostics) {
		this.nodeDiagnosticTests = Objects.requireNonNull(nodeDiagnostics);
	}
}
