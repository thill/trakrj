/**
 * MIT License
 *
 * Copyright (c) 2019 Eric Thill
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.thill.trakrj.internal.load;

import io.thill.trakrj.conductor.DisabledConductor;
import io.thill.trakrj.conductor.DefaultConductor;
import io.thill.trakrj.logger.StderrStatLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class Config {

  public static final String SYSKEY_ENABLED = "trakrj.enabled";
  public static final String DEFAULT_ENABLED = "false";

  public static final String SYSKEY_CONFIG = "trakrj.config";
  public static final String DEFAULT_CONFIG = "trakrj.properties";

  public static final String SYSKEY_PRINT_CONFIG = "trakrj.config.print";
  public static final String CFGKEY_PRINT_CONFIG = "config.print";

  public static final String CFGPREFIX_CONDUCTOR = "conductor.";
  public static final String CFGPREFIX_LOGGER = "logger.";

  public static final String CFGKEY_IMPL = "impl";
  public static final String CFGKEY_CONDUCTOR_IMPL = CFGPREFIX_CONDUCTOR + CFGKEY_IMPL;
  public static final String CFGKEY_LOGGER_IMPL = CFGPREFIX_LOGGER + CFGKEY_IMPL;

  public static final String DISABLED_CONDUCTOR_IMPL = DisabledConductor.class.getName();
  public static final String DEFAULT_CONDUCTOR_IMPL = DefaultConductor.class.getName();
  public static final String DEFAULT_LOGGER_IMPL = StderrStatLogger.class.getName();

  public static final String CONDUCTOR_IMPL_DEFAULT = "default";
  public static final String CONDUCTOR_IMPL_DISABLED = "disabled";

  public static final String LOGGER_IMPL_SLF4J = "slf4j";
  public static final String LOGGER_IMPL_STDOUT = "stdout";
  public static final String LOGGER_IMPL_STDERR = "stderr";
  public static final String LOGGER_IMPL_MULTI = "multi";

  public static final String CFGKEY_LOGGER_NAME = "name";
  public static final String DEFAULT_LOGGER_NAME = "TrakrJ";

  public static Map<String, String> loadConfig() throws IOException {
    String configUrl = System.getProperty(SYSKEY_CONFIG, DEFAULT_CONFIG);

    InputStream in;

    // first, try loading as class resource
    in = Instantiate.class.getResourceAsStream(configUrl);
    if(in != null) {
      try {
        return loadConfig(in);
      } finally {
        in.close();
      }
    }

    // second, try loading as system resource
    in = ClassLoader.getSystemResourceAsStream(configUrl);
    if(in != null) {
      try {
        return loadConfig(in);
      } finally {
        in.close();
      }
    }

    // try loading from file
    // leading ~ should resolve to user.home when loading from File
    if(configUrl.startsWith("~")) {
      configUrl = System.getProperty("user.home") + configUrl.substring(1);
    }
    File file = new File(configUrl);
    if(file.isFile()) {
      in = new FileInputStream(file);
      try {
        return loadConfig(in);
      } finally {
        in.close();
      }
    }

    // if enabled syskey is set, use default config, otherwise use disabled config
    String enabled = System.getProperty(SYSKEY_ENABLED, DEFAULT_ENABLED);
    if("".equals(enabled) || "true".endsWith(enabled)) {
      return defaultConfig(DEFAULT_CONDUCTOR_IMPL);
    } else {
      return defaultConfig(DISABLED_CONDUCTOR_IMPL);
    }
  }

  private static Map<String, String> defaultConfig(String conductorImpl) {
    Map<String, String> config = new LinkedHashMap<>();
    config.put(CFGKEY_CONDUCTOR_IMPL, conductorImpl);
    return Collections.unmodifiableMap(config);
  }

  private static Map<String, String> loadConfig(InputStream in) throws IOException {
    Properties props = new Properties();
    props.load(in);
    Map<String, String> config = new LinkedHashMap<>();
    for(Map.Entry<Object, Object> e : props.entrySet()) {
      config.put(e.getKey().toString(), e.getValue().toString());
    }
    return Collections.unmodifiableMap(config);
  }

  public static void tryPrintConfig(Map<String, String> config) {
    if("true".equals(System.getProperty(SYSKEY_PRINT_CONFIG)) || "true".equals(config.get(CFGKEY_PRINT_CONFIG))) {
      System.err.println("trackrj config: " + config.toString());
    }
  }

  public static Map<String, String> subConfig(Map<String, String> config, String prefix, Collection<String> ignore) {
    Map<String, String> sub = new LinkedHashMap<>();
    for(Map.Entry<String, String> e : config.entrySet()) {
      if(e.getKey().startsWith(prefix)) {
        String key = e.getKey().substring(prefix.length());
        if(!ignore.contains(key)) {
          sub.put(key, e.getValue());
        }
      }
    }
    return Collections.unmodifiableMap(sub);
  }
}
