/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.oauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;
import org.eclipse.vorto.repository.sso.oauth.strategy.HydraTokenVerificationProvider;
import org.eclipse.vorto.repository.sso.oauth.strategy.PublicKeyHelper;
import org.eclipse.vorto.repository.sso.oauth.strategy.ResourceIdentificationHelper;
import org.eclipse.vorto.repository.sso.oauth.strategy.VerificationHelper;
import org.junit.Test;
import org.mockito.Mockito;

public class HydraTokenVerifierTest {

  @Test
  public void testResourceIdentifier() {
    Optional<String> resource = ResourceIdentificationHelper
        .identifyResource("/api/v1/models/vorto.private.erle:Datatype1:1.0.0");
    assertTrue(resource.isPresent());
    assertEquals("vorto.private.erle:Datatype1:1.0.0", resource.get());
    
    resource = ResourceIdentificationHelper
        .identifyResource("/api/v1/models/vorto.private.erle:Datatype1:1.0.0-Snapshot");
    assertTrue(resource.isPresent());
    assertEquals("vorto.private.erle:Datatype1:1.0.0-Snapshot", resource.get());

    resource = ResourceIdentificationHelper
        .identifyResource("/api/v1/models/vorto.private.erle:Datatype1:1.0.0/anyOtherApi");
    assertTrue(resource.isPresent());
    assertEquals("vorto.private.erle:Datatype1:1.0.0", resource.get());

    resource = ResourceIdentificationHelper.identifyResource("/api/v1/search/models");
    assertFalse(resource.isPresent());

    resource = ResourceIdentificationHelper.identifyResource("/api/v1/models/vorto.private.erle");
    assertTrue(resource.isPresent());
    assertEquals("vorto.private.erle", resource.get());

    resource = ResourceIdentificationHelper
        .identifyResource("/api/v1/models/vorto.private.erle/anyOtherApi");
    assertTrue(resource.isPresent());
    assertEquals("vorto.private.erle", resource.get());
  }

  private final String jwtToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6InB1YmxpYzoxYjI2ZDEwYi1iMTZjLTQ4MDQtYmM4MC1jMWUzOWJhNTZlMjAiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiJmNjAyNGYzMy03NzQwLTQ0MmUtYWVjMC1lNTU4MzI4OTA2OTAiLCJpYXQiOjE1MTcyNDEwMzEsImV4cCI6MTUxNzI0NDYzMSwibmJmIjoxNTE3MjQxMDMxLCJpc3MiOiJodHRwczovL2FjY2Vzcy5ib3NjaC1pb3Qtc3VpdGUuY29tL3YyIiwiYXVkIjpbXSwiY2xpZW50X2lkIjoiZDc1OGEzNWUtOTRlZi00NDNmLTk2MjUtN2YwMzA5MmUyMDA1Iiwic2NwIjpbInNlcnZpY2U6aW90LWh1Yjp0ZTE3YjcyMDIwMDQ3NDRmM2E4ZWQ3ZDM2OWFkMTYyNTFfaHViL2Z1bGwtYWNjZXNzIiwic2VydmljZTppb3QtdGhpbmdzLWV1LTE6ZTE3YjcyMDItMDA0Ny00NGYzLWE4ZWQtN2QzNjlhZDE2MjUxX3RoaW5ncy9mdWxsLWFjY2VzcyIsInNlcnZpY2U6dm9ydG86dm9ydG8ucHJpdmF0ZS5lcmxlL2Z1bGwtYWNjZXNzIl0sInN1YiI6ImQ3NThhMzVlLTk0ZWYtNDQzZi05NjI1LTdmMDMwOTJlMjAwNSIsImV4dCI6e319.iDo6FJJw3KUz8ExYesoZ6zS4hO_8LaO4Mr7LnSx3ZoeRj4ScwOkqbrG7zhzsvmaXPw72YlL_dR0n57XR_Imyw9p3xmcKJrDeyAYZclei2gi4oBFK2MhiWJ-RvwMITahL4pFdqfEZzfUpK7Ce_QC3hbh2c6ES9kBWUJk5vSaG2IloMe9IRCgU4vWaG412I7q8umXbsSMnjAr4KPHDh8FPWVd2rAPQ5RhA6xjuT830Vhq6e1bnM7nlMSYisYoQvkUDbVVYJq-YOCHHJ6aDi0K5NiK8mtsp_G7VIIgkBZhn38mmCnM3Lid44OnMAcqUfPdaDgUwxD0UEyVIqfOEqA7B0nGlFSXnbzgfTiMC9TERYEMSG2H1WdcaH7NpGmmxL1jwvTJLBmiSlOToakCeQP7Gz8gYeIsuf7Udazmw4jg90mTsARxoZPPrOfDiRsuzGoCCcJkSsxUHzHqvkefASWZY9jDnPwxCWiFnIZNoMzq4uTFHnVAE_bVAaVjuZn9OZ_wf0utY9Ds5fd_RQiBN9nMlf185hzsVpXtYFWX4_B_akzITjeb8kI0wefo_ow2Xmu-CcVMiMzRED6wnY5l4VYuIOaRVblE0g11tb3zvgzIXmKNqNbCpNxSCDfjEtvANqJs4b_fzPZeJZuLO4aAYuDwBWFcVOn8Tm-3ZZWlqXe5xY1o";

  private final BigInteger modulo = new BigInteger("830538811735408314582621539553070162825329511473749520960071842512888154583661329578856501388691328580504356783061328026508874276684641498258143084761635564016833992631306882144953234242988258892822611594333968077284902649586411908621597793340752603085124372671862634388266258413310517372674079274553870093252755141756170435363293975128993814361895755615458479647933744804040235768892963491228647623874059178600644686133883859540472149350623949509708609454530204501429551624718164222191973302614187673550205993013414740838695954934129529036627714028824333507464386064437170296361570229468678245951548693673563799080713669059443095743583743206041381189780897243809909360939809588196952428535684629017896004490061234079460078438775041303425320658850058863736452913325588120245329910765148185775202514654754633362916460109862442581443593980114680213039061721167744518772243359704401228544384233413625885686192673912365645103750571555793433569142815053494544430670334028950249448318200460086175661007125340740752231580985757635832323998046254817494457485681348394265446594066165808012158600041431797148481761875796401724531491309057513937955912409183564779996713266437433533783642657738476318101266488457712636171984437330161744271904413");
  
  private final String publicKeyMod = Base64.getUrlEncoder().encodeToString(modulo.toByteArray());
  
  private final String publicKeyExp = "AQAB";
  
  private Supplier<Map<String, PublicKey>> publicKey() {
    return () -> {
      Map<String, PublicKey> pubKey = new HashMap<String, PublicKey>();
      pubKey.put("public:1b26d10b-b16c-4804-bc80-c1e39ba56e20", PublicKeyHelper.toPublicKey(publicKeyMod, publicKeyExp));
      return pubKey;
    };
  }
  
  @Test
  public void verifyJwtToken() {
    assertTrue(VerificationHelper.verifyJwtToken(publicKey().get().get("public:1b26d10b-b16c-4804-bc80-c1e39ba56e20"), 
        JwtToken.instance(jwtToken).get()));
  }
  
  @Test
  public void verifyHydraVerifierValid() {
    IUserAccountService userAccountService = Mockito.mock(IUserAccountService.class);
    Mockito.when(userAccountService.exists("d758a35e-94ef-443f-9625-7f03092e2005")).thenReturn(true);
    
    HttpServletRequest httpRequest = Mockito.mock(HttpServletRequest.class);
    Mockito.when(httpRequest.getRequestURI()).thenReturn("/infomodelrepository/api/v1/models/vorto.private.erle:Datatype1:1.0.0");
    Mockito.when(httpRequest.getContextPath()).thenReturn("/infomodelrepository");
    Mockito.when(httpRequest.getMethod()).thenReturn("GET");
    
    HydraTokenVerificationProvider verifier = new HydraTokenVerificationProvider(publicKey(), userAccountService);
    
    assertTrue(verifier.verify(httpRequest, JwtToken.instance(jwtToken).get()));
  }
  
}
