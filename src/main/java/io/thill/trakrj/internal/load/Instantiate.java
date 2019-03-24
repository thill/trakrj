/**
 * Copyright (c) 2019 Eric Thill
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package io.thill.trakrj.internal.load;

import io.thill.trakrj.conductor.Conductor;
import io.thill.trakrj.logger.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static io.thill.trakrj.internal.load.Config.*;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class Instantiate {

  private static final String PREFIX_ALL = "";

  public static Conductor instantiateConductorFromSysConfig(Map<String, String> sysConfig, StatLogger logger)
          throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException {
    Map<String, String> conductorConfig = Config.subConfig(sysConfig, CFGPREFIX_CONDUCTOR, Collections.emptyList());
    return instantiateConductor(conductorConfig, logger);
  }

  public static Conductor instantiateConductor(Map<String, String> conductorConfig, StatLogger logger)
          throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException {
    String conductorClass = conductorConfig.getOrDefault(CFGKEY_IMPL, DEFAULT_CONDUCTOR_IMPL);
    conductorClass = mapConductorClass(conductorClass);
    Conductor conductor = (Conductor)Class.forName(conductorClass).newInstance();
    conductor.configure(Config.subConfig(conductorConfig, PREFIX_ALL, Arrays.asList(CFGKEY_IMPL)), logger);
    return conductor;
  }

  private static String mapConductorClass(String conductorClass) {
    switch(conductorClass) {
      case CONDUCTOR_IMPL_DEFAULT:
        return DEFAULT_CONDUCTOR_IMPL;
      case CONDUCTOR_IMPL_DISABLED:
        return DISABLED_CONDUCTOR_IMPL;
      default:
        return conductorClass;
    }
  }

  public static StatLogger instantiateLoggerFromSysConfig(Map<String, String> sysConfig) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    Map<String, String> loggerConfig = Config.subConfig(sysConfig, CFGPREFIX_LOGGER, Collections.emptyList());
    return instantiateLogger(loggerConfig);
  }

  public static StatLogger instantiateLogger(Map<String, String> loggerConfig) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    String loggerClass = loggerConfig.getOrDefault(CFGKEY_IMPL, DEFAULT_LOGGER_IMPL);
    loggerClass = mapLoggerClass(loggerClass);
    StatLogger logger = (StatLogger)Class.forName(loggerClass).newInstance();
    logger.configure(Config.subConfig(loggerConfig, PREFIX_ALL, Arrays.asList(CFGKEY_IMPL)));
    return logger;
  }

  private static String mapLoggerClass(String loggerClass) {
    switch(loggerClass) {
      case LOGGER_IMPL_SLF4J:
        return Slf4jStatLogger.class.getName();
      case LOGGER_IMPL_STDERR:
        return StderrStatLogger.class.getName();
      case LOGGER_IMPL_STDOUT:
        return StdoutStatLogger.class.getName();
      case LOGGER_IMPL_MULTI:
        return MultiStatLogger.class.getName();
      case LOGGER_IMPL_STATSD:
        return StatsDStatLogger.class.getName();
      default:
        return loggerClass;
    }
  }

}
