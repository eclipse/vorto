package org.eclipse.vorto.repository.oauth;

import java.security.Principal;
import java.util.Collection;
import org.springframework.security.core.Authentication;

public interface IOAuthProviderRegistry {

  /**
   * 
   * @param jwtToken
   * @return
   */
  IOAuthProvider getByToken(String jwtToken);
  
  /**
   * 
   * @param auth
   * @return
   */
  IOAuthProvider getByAuthentication(Authentication auth); 
  
  /**
   * 
   * @return
   */
  Collection<IOAuthProvider> list();
  
  /**
   * 
   * @param user
   * @return
   */
  IOAuthProvider getByPrincipal(Principal user);
  
  /**
   * 
   * @param provider
   */
  void registerOAuthProvider(IOAuthProvider provider);
  
  /**
   * 
   * @param id
   */
  void unregisterOAuthProvider(String id);

}
