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
package org.eclipse.vorto.plugins.generator.lambda.meta.plugins.plugins;

import org.eclipse.vorto.codegen.bosch.BoschIoTSuiteGenerator;
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo;

public class BoschIoTSuitePluginInfo implements IPluginMeta {

  private static final String LOGO_144x144 = "iVBORw0KGgoAAAANSUhEUgAAAJAAAACQCAYAAADnRuK4AAAK+klEQVR4nO2beZBcVRXGf93TM5NJJjOZkABhEQgEBAJGAyIRFQtRUEAICJoS0biFAkVErEJRkVLRUiKWqFQEFEFBwlao7MoS9qzAhCVMWIQAYQhbttnbP777uK9fOjPdMxEyVd+vquut97537/vuueec9xqMMcYYY4wxxhhjjDHGGGOMMcYYY4wxxhhjjDHGGGOMMcYYY4wxxhhjjDHGGGOMMcYYY4wxxhhjjDHGGGOMMcYYY4wxxhhjjDHGGGOMMcYY846RY/biZH1L4MvA4cBWQDdQrLK+IjASaAfOAG5LHTsaOA+oB54P209vWEMR6kdAoRZuuBKebYOGUVXehnm7KITlFOBKYNImqncHYCalAtoN2C6sj0eC3VBAdfWQr4Gbroanl0Fjk0RlNksKwATgJmR1QNZhCVAD5AdRZwPwInB2Zn9WBX2lR4PlISfxtD0KzWOh2IfZfCkgS5GI50bgS8DKsJ2roq6BzMS/gSuIU1i0PsUijGiAXB5uvhralsLoMRbPMCDH7MULgKnIauwJvDbIupqArYH1SHj1SKCPDViyUAs1NXDzNfDUE9DYTPXul3knKADNYX0BgxfPB4G/AC1AV9iXD7/LgW+yMUXkcjp0y7XQ9pinrWFGHugJ67UMzucBeD8wEQloq/AbD2wBHIGs0cbp6YFV7ZrGLJ5hRYFoGWro3+cZiSxNEQniGWBpOLa6n3IriQ7zOOCrwN3APCA6zw2jYG1/1ZjNkcLAp7zFDGBOWM8BTyBBraqijh8C30CimkqxuIKmFlh4N7zyEtTVVVGV2RyoRkBjKLVQY9G0B9mQvJT0sW3DsgVooVBYwZo3YPnj0NMdprDiu8LxjlS5PLJyz1dxv5uCkcBPUf7qaeAHQOcAZXLh/LHAOuC/FZQZAewM9FLeVyygIOfVMsfyyE2YBDwJXE//zyNLDXAU8DjQWkW5ty5eKdnprTe13szGGZe6zuthuRroYmQjPPYwPPdUkjCsBS5BeahFwCPAo6hh96NMdmMV9zxU6lGa4xjgeKASEzkKJWWXAPcCe1VQZj/UxlbgIWBh+C1GfdAKnFym3A7APSjntivw/VBu1wqumVAL/Bj4eGrfMcCHKilcjQV6BTnciZBWEkdWKxLFSEpHUA6Yj16LQFaExSLU1iqMV7a5SBTIyLDsRaNkW+CUsO9bVdz3UOgGnkPpjZfp39dL0xCWLVSWSxsRljXhVy7oKDdwZgNvooe/OtRzPnALsA96ZgPRAUym1JjMQjnBeQMVrkZAlyMzl0eNfIEY9t+K/KExSFS58KtFo6g3W9lG6CNOXe3I71oU6r4EPZBDKRXQJOCjwDZAG3ADpaa+BnXwlHBPy4EHgWeJpn474BCUx3oWuA49kKStyf0U0OiciB7SgjJt6AXWhPX1xCg34RDgPWiwzAduR1bkWNSfH0ZTJaEtv0H9+kimnhzwPuArRGF3hO3jiYLYAUXFC1Lt3R2JdElq+wU0Q3wS2B74AHBiaOfycN7H0DvM9nBfq6oRUAdwXz/Hsw0cKj3I73gV+T6JsF5KnTMTjcL0FLoUZdPnh/2XohfEadrR1LISOAy4gOifgTp2JmpTYlEb0cM+IGyficR8Z6buZPBkGQtcBByZ2f9n4OvA3FT5hOXoAZajiF5B/Q74NApqEi5NrR8brvkRooBOQsJK+uUKZLnmoOlvDOqPqUQrdA7weZTvmwg8ABxaqYAOCpWtQyOyAVmdheH4eOBA5CPkkdVYukEt1TEh1L8aWYZCqPe41D1dFNYfRpZuOppu5iKndDqxk84B7kDTYCsabbsAV6HR2B7KzUTW6mT0dUIi3P3C9e8IbW1AEWVWQFmSh3Y+UTxz0XRzOPBFNEhOC8e2TpUd6DOE05H4/4ms7xLgb5QO5k42nHrXIuuY8AZRuOeh6e92Yv8eBXwG2CNV12+BP1YioK3CTW2Z2f85pOrXUcfMSR2bi5Q/VJpQR6fvcyyyQieF7R40Z98HrAC+h0bXwcSseNKOPHAhcG3YN53ob8wGfo6s3jjg1+HaieP8FOrYSci6NRH9tP5Yh0ZzIp47Ud80INHvBnwWObJvUjrdD/Q+Zw2yCpNDW6Yha3Y2EgKhvq5MuZ7Mvi5Kp9oGSts2A1m4HYlvGx4CZlUioAbKO3XNxDA+W8+mSOi8gvyNVjRHX4Dm/H8hy7FFOO9l4hz9YKr8vkgUS5BFmRl+oM9MjgZGh+0+lNwE+FWqjiZi21ahB7qOGDwMFJ6DRvokomO9KLX/ISSgRiT6wboBSQQH8gdvDNu3UV6E2X3ZKbeG+GxBgnkv8DM0vXWjwXVFJWF8N+U7ag1RtQ2ZY5si1O5E0+Aq9HDbwv4J4Zf4QlsgqwR6COn7Wwt8Co2gX6J5G+QMTgOWhe0cEinA/shajEDCSqagpEOTl8SVUiCKDyT+hJ3DspvKIqY0k4lRaZrbkZ8yI2zXIyuUtjCdxOBgY6TPHw/8HflaR6DBNw04vhIB1VK+wxpS+x9Aar8NPexry5xfCUViR49DDuYc1CmJL9ONhHFj2K5H/s2JxA5djyKpfdA8XodyM/enrjUaWQCQgL6NppI54f4vRiMviaiKqWV/ibp0G0CDKcnlgAR9GhrNU8O+O1GikEzZgRKC5xGtapqDiG1bT6loQVFpuu5sArOJmLMD+A8aVH0oUnwNWaJTKxlJXZSf6+uIKr0H+RzVU+xLf3FYIFqTetTZaXpQsuw15JcdhkbDkUQfowf4GvJlzkRh8yGZep5EQn8RWabTgXejVEXCLajtE8P2NmE5gjh9lkugFojfVzUSrfEX0PS7DaXT5MPoa4WEtOOc9TvTtCLn9koUOMwL9zULRZcXhvOuAb4b2nYVevF9AHBZqq53ZdqyHA2oJmSlZyOrfVdYHwP8Ari1EgG9CJwQLtyJHNF6FEIO9vMPUSxCTS3UFLSeyxWRr7MTMfrJhWu2IwuXjKwu5CPNQEIai7LWfyVGh2ch53p/9FA7kZ/0J+Q7gTp3AfAJ5Ow+iR7KPGR9zw1lk9co7cBPkDOZ9rkSOlHEtSOKWFaE/UvCfZyAcixdoT2XoSgoYVG4Zh456/1xHfL1zkBRYw16LmcRnfGXkUU6HzgVCelANC0lXECpdf4OEvmJoa1dyGr9CFn5fLjGH3LMXvwomv9vRXmNSpN+WVrCsj9RXYxyNKuAaeRyy2gYBddfDs8sg+aWoXz/HD4sGhLZOjZFnW8Xo9H03jHAOdV88tBAabif1NGT7B/s9z9ZpiPzdhdytCqjWITeXth9b2gcrReqg2dTPOhsHcNFPCBh9Cee5JxqyIonqeOt/XliCNfH4DtsLxQVTAb27ue8xLrFbG3HOpi0B4zbWh+WmWFFnhhJdVHdZwBp5iP/Yynl/YKEdEisMDKXg74+mLIfdHeFT1zNcKFANGtTUASwdhD13BB+/ZFDySiIoaDo6YbxE2DXPeGZNv03zAwL8sA/wvr2KGfSsvHTB00deum3b9i+h5j3gO5uaB4Du+wJXQNN42ZzooDyBaegPMBxKFy/F1mMajKu5Sgij30y+oQBFCmcW3JWLif/p2Od/htmhg0F9MHUwcDVyArtFH7/D95AIl040IlmeJBYmPkouTULheHjqOxFYSXUIb/qZuD36KM0Y4wxxhhjjDHGGGOMMcYYY4wxxhhjjDHGGGOMMcYYY4wxxhhjjDHGGGOMMcYYY4wxxhhjjDHGGGOMMcYYY4wxxhhjjDHGGGOMMcYYY4wxxhgzVP4HdhyXS9qKISwAAAAASUVORK5CYII=";
  private static final String LOGO_32x32 = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAVBSURBVFhHpVdZTFxVGP7ubMDMMGylbKUMBdLSxmohqfYBt1gbqhGJW0Owavpg6puJRp+MTUxsfOiDvigmTXxALaSVklgjaZP2QU1LXUrBtjAtlNIacBx2hmE2v//eO3QoMHMHvvAz957l/v/5l++co0QJEPLz+V9etPdPYHhqARaTAkWRntSwEI4gElXw7f4yPLHJqbcCf4758cLpWxBlNrOC1no39hTZoRrQPx5A05kh/D44zV6TNmM9WAhj9xYXLjZt1RuAzpuTaGgbAKz8PhfXut+Npm05UAKhcLSuzYNLt6mclklnRU4aPUKv6JNTQUhzKL56phT1bpf6LFANOBFnwHO6AR0DE9EXdcsyMyw4/2oVKrNtCEVSUy/RMjFmMk1Cl51m1jp0XB71Y99JD4I00G4xof35ctSVOKDs+e5G9LfhGZoewcz7u+CwLp2YCsRocaKYHua/CN/TqCwRlLKW3ujt8XlsL3Cg741tenNqmAyE8cnFUVzzzcMuLqZy5iECwQjqy104/PAGfeRyKKU04A4n1pY6cTkuaVLBKc8EXuq4JQmgxSIGesBmt2L8nYc0w1aAifmgTgqmGPMl4FSzuFqU2BjCOLFJOevDVoJulgxJbECvdx77TnlUeardg8NnRzAbDKt9knwiK8EsBuhdwgX2L66AeYeZhYjaFueXRHYCv/4zg65eH7o8UzjPkmrp82J0LqT3GkPrdR/800F0syIu3GXiEysHZgWo1ZFOsdBQujuPZWYza9MTmR7fp36DZWLlPH2qcQNUd4vbxHOs5Sk+hyKaGyfJfEGKSgJSfzHh+4Q/uMgpasJpf2q5qm3aT3KUZ6UhNzcdGxwWZFFq8jMWOaOuxIld5PVshxX5zvviJLE178iDIwG9K2Vfkwd8AewstuNKc2Ie8M2HVIoOM2EdDEM8ac3RQ9Ose8l6bb0aMW1kGcZwhFzx8YURZGRYcbrBjb2bXcY9IMhNtyCPq9rIDzzImHa+F1BZDsfIOJF45avBkAFXWYKfdY/iyx4v5V8c+2OMZTmn9zI1SEDnhqdxZnASnZ5JDEwE9J7ksOi/q0LOCTuP/02mYpLFCpqudXHH7DtYjU2ZVrR7xtHcMajuJ9JXxk1m6NAObWwSJPWAn6tLpzshQverwmetBLVoZ/DZEuujPLgTJkJSA+RktNIgOdUIywkCLLkQT0KqPZQ5JqNRGMqBZMijR8xqeKidISjNtGkdBpDUANmk/BHGX+IbE652OhBiWmgrfdadidC7jyD6Xg2iH9bi3MuVarsRJDXAwR3u9epcVBfaUVPsUKW60IGDbMs3UGbJYCgEx/duxpnGLehs0OQnPh97soT5tv4IGvqCJJvblYYS0qtImcu2uBEJvP4QvunzoeXqfxicNM4BgjgDYgSaOjpIPm/yzP/2yZs42j2mtxqDiTlG3VHusmt3p0s2G9mmWZo5smWvArKEClGpb6T0gFQPReh0rdhd5MCRx0vwAfPilaocvXU55nWu4G2IbxqHmIrstIs13HtvVm1YC9zMiY8eK8TRumLUFmTorUsRodJf7lIHdTvpsWKn5inlxHVf9DXGTg6UJbyQ/NxYCXeWjaWegke4GG09eibFTTUxOkEy5aGuO/iBSSqheroia5ErFD+Z5tHvb6BHrBM2YxxrWPOCaPyX1gJOl2oZYGX4xhcW86T/re2o4mYmUC+nPV4/Dvw4hGsjPCjKcXqdepdB3EMGNZGyW3lrPrD1fp4sXs8lRp9eGkUbr+f3ZoOwxrbedUJyO40rb6zI5g2JjJobnyPA/22d82ViOA/sAAAAAElFTkSuQmCC";

  private GeneratorPluginInfo info = null;


  public BoschIoTSuitePluginInfo() {
    BoschIoTSuiteGenerator bosch = new BoschIoTSuiteGenerator();

    info = bosch.getMeta();
    info.setImage144x144(LOGO_144x144);
    info.setImage32x32(LOGO_32x32);
  }

  @Override
  public GeneratorPluginInfo getInfo() {
    return info;
  }
}