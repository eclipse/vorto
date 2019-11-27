package org.eclipse.vorto.plugins.generator.lambda.meta.plugins.plugins;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo;

public class GeneratorPluginInfoFactory {
  
  
  private static GeneratorPluginInfoFactory instance = null;
  
  private static final EclipseDittoPluginInfo eclipsedittoPlugin = new EclipseDittoPluginInfo();
  private static final EclipseHonoPluginInfo eclipsehonoPlugin = new EclipseHonoPluginInfo();
  private static final BoschIoTSuitePluginInfo boschiotsuitePlugin = new BoschIoTSuitePluginInfo();
  private static final OpenAPIPluginInfo openApiPlugin = new OpenAPIPluginInfo();
  
  
  private final static Map<String, GeneratorPluginInfo> infos = new HashMap<String, GeneratorPluginInfo>();
  
  static {
    infos.put(eclipsedittoPlugin.getInfo().getKey(),eclipsedittoPlugin.getInfo());
    infos.put(eclipsehonoPlugin.getInfo().getKey(),eclipsehonoPlugin.getInfo());
    infos.put(boschiotsuitePlugin.getInfo().getKey(),boschiotsuitePlugin.getInfo());
    infos.put(openApiPlugin.getInfo().getKey(), openApiPlugin.getInfo());
  }
  
  private GeneratorPluginInfoFactory() {
  }


  public static GeneratorPluginInfoFactory getInstance() {
    if (instance == null) {
      instance = new GeneratorPluginInfoFactory();
    }
    return instance;
  }
  public GeneratorPluginInfo getForPlugin(String pluginkey) {
    return infos.get(pluginkey);
  }
}
