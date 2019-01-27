package io.thill.trakrj.internal.load;

import io.thill.trakrj.conductor.Conductor;
import io.thill.trakrj.logger.Slf4jStatLogger;
import io.thill.trakrj.logger.StatLogger;
import io.thill.trakrj.logger.StderrStatLogger;
import io.thill.trakrj.logger.StdoutStatLogger;

import java.util.Arrays;
import java.util.Map;

import static io.thill.trakrj.internal.load.Config.*;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class Instantiate {

  public static Conductor instantiateConductor(Map<String, String> config, StatLogger logger)
          throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException {
    String conductorClass = config.getOrDefault(CFGKEY_CONDUCTOR_IMPL, DEFAULT_CONDUCTOR_IMPL);
    conductorClass = mapConductorClass(conductorClass);
    Conductor conductor = (Conductor)Class.forName(conductorClass).newInstance();
    Map<String, String> conductorConfig = Config.subConfig(config, CFGPREFIX_CONDUCTOR, Arrays.asList(CFGKEY_IMPL));
    conductor.configure(conductorConfig, logger);
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

  public static StatLogger instantiateLogger(Map<String, String> config) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    String loggerClass = config.getOrDefault(CFGKEY_LOGGER_IMPL, DEFAULT_LOGGER_IMPL);
    loggerClass = mapLoggerClass(loggerClass);
    StatLogger logger = (StatLogger)Class.forName(loggerClass).newInstance();
    Map<String, String> loggerConfig = Config.subConfig(config, CFGPREFIX_LOGGER, Arrays.asList(CFGKEY_IMPL));
    logger.configure(loggerConfig);
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
      default:
        return loggerClass;
    }
  }

}
