package org.eclipse.vorto.repository.server.it;

import static org.eclipse.vorto.repository.account.Role.ADMIN;
import static org.eclipse.vorto.repository.account.Role.MODEL_CREATOR;
import static org.eclipse.vorto.repository.account.Role.MODEL_PROMOTER;
import static org.eclipse.vorto.repository.account.Role.MODEL_REVIEWER;
import static org.eclipse.vorto.repository.account.Role.USER;
import javax.annotation.PostConstruct;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Autowired
  private IUserAccountService accountService;
  
  public static final String USER_ADMIN = "user1";
  public static final String USER_STANDARD = "user2";
  public static final String USER_CREATOR = "user3";
 
  
  @PostConstruct
  public void createUsers() {
    accountService.create(USER_ADMIN,USER,ADMIN,MODEL_CREATOR,MODEL_PROMOTER,MODEL_REVIEWER);
    accountService.create(USER_STANDARD,USER);
    accountService.create(USER_CREATOR,USER,MODEL_CREATOR);
  }
}
