package org.eclipse.vorto.repository.internal.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Item;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.service.FatalModelRepositoryException;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.service.IRepositoryManager;
import org.eclipse.vorto.repository.service.ModelReferentialIntegrityException;
import org.modeshape.jcr.api.JcrTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultRepositoryManager implements IRepositoryManager {

	@Autowired
	private Session session;

	@Autowired
	private IModelRepository modelRepository;

	@Override
	public byte[] backup() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		((org.modeshape.jcr.api.Session) session).exportDocumentView("/", baos, false, false);
		baos.close();
		return baos.toByteArray();

	}

	@Override
	public void restore(InputStream inputStream) throws Exception {
		removeAll();
		((org.modeshape.jcr.api.Session) session).getWorkspace().importXML("/", inputStream,
				ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public IModelRepository getModelRepository() {
		return modelRepository;
	}

	public void setModelRepository(IModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	@Override
	public void removeModel(ModelId modelId) {
		try {
			ModelResource modelResource = this.modelRepository.getById(modelId);
			if (!modelResource.getReferencedBy().isEmpty()) {
				throw new ModelReferentialIntegrityException(
						"Cannot remove model because it is referenced by other model(s)",
						modelResource.getReferencedBy());
			}
			Item item = session.getItem(modelId.getFullPath());
			item.remove();
			session.save();
		} catch (RepositoryException e) {
			throw new FatalModelRepositoryException("Problem occured removing the model", e);
		}
	}

	private void removeAll() throws Exception {
		Set<String> rootNodes = new HashSet<>();
		for (ModelResource resource : this.modelRepository.search("*")) {
			final String org = resource.getId().getNamespace().substring(0,
					resource.getId().getNamespace().indexOf("."));
			rootNodes.add(org);
		}

		for (String rootNode : rootNodes) {
			try {
				Item item = session.getItem("/" + rootNode);
				item.remove();
			} catch (PathNotFoundException ex) {
			}
		}

		this.session.save();
	}

}
