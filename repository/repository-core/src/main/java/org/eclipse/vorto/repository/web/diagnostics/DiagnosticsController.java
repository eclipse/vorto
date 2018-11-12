package org.eclipse.vorto.repository.web.diagnostics;

import java.util.Collection;

import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.impl.JcrModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/{tenant}/diagnostics")
public class DiagnosticsController {

	@Autowired
	private JcrModelRepository modelRepo;
	
	@RequestMapping(method = RequestMethod.GET)
	public Collection<Diagnostic> diagnose() {
		return modelRepo.diagnose();
	}
	
}
