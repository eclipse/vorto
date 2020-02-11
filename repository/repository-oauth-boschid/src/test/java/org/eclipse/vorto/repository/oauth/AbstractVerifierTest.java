/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.oauth;

import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.vorto.repository.oauth.internal.PublicKeyHelper;
import org.mockito.Mockito;

public class AbstractVerifierTest {
  
  private final BigInteger modulo = new BigInteger("830538811735408314582621539553070162825329511473749520960071842512888154583661329578856501388691328580504356783061328026508874276684641498258143084761635564016833992631306882144953234242988258892822611594333968077284902649586411908621597793340752603085124372671862634388266258413310517372674079274553870093252755141756170435363293975128993814361895755615458479647933744804040235768892963491228647623874059178600644686133883859540472149350623949509708609454530204501429551624718164222191973302614187673550205993013414740838695954934129529036627714028824333507464386064437170296361570229468678245951548693673563799080713669059443095743583743206041381189780897243809909360939809588196952428535684629017896004490061234079460078438775041303425320658850058863736452913325588120245329910765148185775202514654754633362916460109862442581443593980114680213039061721167744518772243359704401228544384233413625885686192673912365645103750571555793433569142815053494544430670334028950249448318200460086175661007125340740752231580985757635832323998046254817494457485681348394265446594066165808012158600041431797148481761875796401724531491309057513937955912409183564779996713266437433533783642657738476318101266488457712636171984437330161744271904413");
  
  protected Supplier<Map<String, PublicKey>> publicKey() {
    return () -> {
      Map<String, PublicKey> pubKey = new HashMap<String, PublicKey>();
      pubKey.put("public:1b26d10b-b16c-4804-bc80-c1e39ba56e20", 
          PublicKeyHelper.toPublicKey(Base64.getUrlEncoder().encodeToString(modulo.toByteArray()), 
              "AQAB"));
      return pubKey;
    };
  }
  
  protected HttpServletRequest requestModel(String modelId) {
    HttpServletRequest httpRequest = Mockito.mock(HttpServletRequest.class);
    Mockito.when(httpRequest.getRequestURI()).thenReturn("/infomodelrepository/api/v1/models/" + modelId);
    Mockito.when(httpRequest.getContextPath()).thenReturn("/infomodelrepository");
    Mockito.when(httpRequest.getMethod()).thenReturn("GET");
    return httpRequest;
  }
  
}
