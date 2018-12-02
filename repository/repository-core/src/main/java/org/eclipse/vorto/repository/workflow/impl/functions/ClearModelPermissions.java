package org.eclipse.vorto.repository.workflow.impl.functions;

import java.security.Principal;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;

import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.JcrModelRepository;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClearModelPermissions implements IWorkflowFunction {

	private JcrModelRepository modelRepository;

	private static final Logger logger = LoggerFactory.getLogger(ClearModelPermissions.class);

	public ClearModelPermissions(IModelRepository repository) {
		this.modelRepository = (JcrModelRepository) repository;
	}

	@Override
	public void execute(ModelInfo model, IUserContext user) {
		logger.info("Clearing permissions of model " + model.getId());

		final Session session = modelRepository.getSession();
		try {
			ModelIdHelper modelIdHelper = new ModelIdHelper(model.getId());

			String path = modelIdHelper.getFullPath();
			String[] privileges = new String[] { Privilege.JCR_READ, Privilege.JCR_WRITE,
					Privilege.JCR_MODIFY_ACCESS_CONTROL, Privilege.JCR_READ_ACCESS_CONTROL };
			AccessControlManager acm = session.getAccessControlManager();

			Privilege[] permissions = new Privilege[privileges.length];
			for (int i = 0; i < privileges.length; i++) {
				permissions[i] = acm.privilegeFromName(privileges[i]);
			}

			AccessControlList acl = null;
			AccessControlPolicyIterator it = acm.getApplicablePolicies(path);
			if (it.hasNext()) {
				acl = (AccessControlList) it.nextAccessControlPolicy();
			} else {
				acl = (AccessControlList) acm.getPolicies(path)[0];
			}

			acl.addAccessControlEntry(new Principal() {

				@Override
				public String getName() {
					return "admin";
				}
			}, permissions);

			acl.addAccessControlEntry(new Principal() {

				@Override
				public String getName() {
					return user.getUsername();
				}
			}, permissions);

			if (acm.hasPrivileges(path, permissions)) {
				acm.removePolicy(path, acl);
				session.save();
			}
		} catch (RepositoryException ex) {
			logger.error("Could not remove permissions from model", ex);
			throw new RuntimeException("Problem clearing permissions on model node", ex);
		} finally {
			session.logout();
		}

	}
}
