package org.eclipse.vorto.repository.web.api.v1;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Api(value = "/search", description = "Finds information models")
@RestController("modelSearchController")
@RequestMapping(value = "/api/v1/search/models")
public class ModelSearchController extends AbstractRepositoryController {

	@Autowired
	private IModelRepository modelRepository;
	
	@Value("${server.config.authenticatedSearchMode:#{false}}")
	private boolean authenticatedSearchMode = false;
	
	@ApiOperation(value = "Finds models by free-text search expressions")
	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("!@modelSearchController.isAuthenticatedSearchMode() || isAuthenticated()")
	public List<ModelInfo> searchByExpression(
			@ApiParam(value = "a free-text search expression", required = true) @RequestParam("expression") String expression)
			throws UnsupportedEncodingException {
		IUserContext userContext = UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName());
		List<ModelInfo> modelResources = modelRepository.search(URLDecoder.decode(expression, "utf-8"));
		return modelResources.stream()
				.filter(model -> isReleasedOrDeprecated(model)
						|| isUser(SecurityContextHolder.getContext().getAuthentication())
						|| isAdmin(SecurityContextHolder.getContext().getAuthentication()))
				.map(resource -> ModelDtoFactory.createDto(resource, userContext)).collect(Collectors.toList());
	}

	private boolean isAdmin(Authentication authentication) {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	private boolean isUser(Authentication authentication) {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities.contains(new SimpleGrantedAuthority("ROLE_USER"));
	}

	private boolean isReleasedOrDeprecated(ModelInfo model) {
		return SimpleWorkflowModel.STATE_RELEASED.getName().equals(model.getState())
				|| SimpleWorkflowModel.STATE_DEPRECATED.getName().equals(model.getState());
	}
	
	public boolean isAuthenticatedSearchMode() {
		return authenticatedSearchMode;
	}

	public void setAuthenticatedSearchMode(boolean authenticatedSearchMode) {
		this.authenticatedSearchMode = authenticatedSearchMode;
	}
}
