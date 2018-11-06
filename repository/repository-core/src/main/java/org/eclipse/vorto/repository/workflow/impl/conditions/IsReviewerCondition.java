package org.eclipse.vorto.repository.workflow.impl.conditions;

import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.model.IWorkflowCondition;

public class IsReviewerCondition implements IWorkflowCondition {

    private IUserRepository userRepository;

    public IsReviewerCondition(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean passesCondition(ModelInfo model, IUserContext user) {
        User foundUser = userRepository.findByUsername(user.getUsername());
        return foundUser != null ? foundUser.isReviewer() : false;
    }
}
