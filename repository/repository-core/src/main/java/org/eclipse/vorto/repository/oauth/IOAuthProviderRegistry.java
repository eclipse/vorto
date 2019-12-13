package org.eclipse.vorto.repository.oauth;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import org.springframework.security.core.Authentication;

/**
 * 
 * Registry where all oauth providers can be registered or looked up
 * 
 * @author Alexander Edelmann (Robert Bosch (SEA) Pte. Ltd)
 *
 */
public interface IOAuthProviderRegistry {

  /**
   * Gets a provider by a given JSON web token
   * @param jwtToken
   * @return
   */
  IOAuthProvider getByToken(String jwtToken);
  
  /**
   * Gets a provider by a given authentication
   * @param auth
   * @return
   */
  IOAuthProvider getByAuthentication(Authentication authentication); 
  
  /**
   * Gets a provider with the given id
   * @param providerId
   * @return
   */
  Optional<IOAuthProvider> getById(String providerId);
  
  /**
   * Lists all registered providers
   * @return
   */
  Collection<IOAuthProvider> list();
  
  /**
   * Gets a provider for a given user object
   * @param user
   * @return
   */
  IOAuthProvider getByPrincipal(Principal user);
  
  /**
   * Registers a provider in the system
   * @param provider
   */
  void registerOAuthProvider(IOAuthProvider provider);
  
  /**
   * Unregisters a provider in the system
   * @param id
   */
  void unregisterOAuthProvider(String id);

}
