package org.eclipse.vorto.repository.web.account;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionTimeoutController {

  @GetMapping(path = "rest/config/session/timeout")
  public ResponseEntity<Integer> getSessionTimeout(HttpServletRequest request) {
    return new ResponseEntity<>(request.getSession().getMaxInactiveInterval(), HttpStatus.OK);
  }

}
