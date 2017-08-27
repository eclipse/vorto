package org.eclipse.vorto.server.devtool.modeshape.sequencer;

import java.io.IOException;

import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.modeshape.jcr.api.nodetype.NodeTypeManager;
import org.modeshape.jcr.api.sequencer.Sequencer;
import org.springframework.core.io.ClassPathResource;

public class ModelResourceSequencer extends Sequencer {

	@Override
	public void initialize(NamespaceRegistry registry, NodeTypeManager nodeTypeManager)
			throws RepositoryException, IOException {
		registerNodeTypes(new ClassPathResource("model-resource-sequencer.cnd").getInputStream(), nodeTypeManager,
				true);
		registerNodeTypes(new ClassPathResource("project-resource-sequencer.cnd").getInputStream(), nodeTypeManager,
				true);
	}

	@Override
	public boolean execute(Property property, Node node, Context context) throws Exception {

		return false;
	}

}
