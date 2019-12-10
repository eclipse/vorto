package org.eclipse.vorto.repository.oauth;

import org.eclipse.vorto.repository.oauth.AbstractOAuthProviderConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

@Configuration
public class GithubOAuthProviderConfiguration extends AbstractOAuthProviderConfiguration {
  
  private static final String LOGOUT_URL = "/logout";
  
  @Value("#{servletContext.contextPath}")
  private String servletContextPath;

  
  public GithubOAuthProviderConfiguration(@Value("${github.oauth2.resource.userInfoUri}") String githubUserInfoEndpointUrl,
                                          @Value("${github.oauth2.client.clientId}") String githubClientId) {
    super(new UserInfoTokenServices(githubUserInfoEndpointUrl, githubClientId));
    
  }

  @Override
  public String getFilterProcessingUrl() {
    return "github/login";
  }

  @Override
  protected String getUserAttributeId() {
    return "login";
  }
  
  @Bean
  @ConfigurationProperties("github.oauth2.client")
  public AuthorizationCodeResourceDetails github() {
    return new AuthorizationCodeResourceDetails();
  }

  @Override
  protected AuthorizationCodeResourceDetails createDetails() {
    return github();
  }

  @Override
  public String getLogoutUrl() {
    return servletContextPath + LOGOUT_URL;
  }

  @Override
  public String getLogoHref() {
    return "webjars/repository-web/dist/images/github-social.png";
  }

}
