package io.thill.trakrj.internal.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Internal class. Public methods may change or be removed without warning.
 *
 * @author Eric Thill
 */
public class Exceptions {

  private static final Logger LOGGER = LoggerFactory.getLogger("TrakrJ");

  public static String throwableToString(Throwable t) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    t.printStackTrace(pw);
    return sw.toString();
  }

  public static void logWarn(String message) {
    if(LOGGER.isErrorEnabled()) {
      LOGGER.warn(message);
    } else {
      System.err.println("TrakrJ: WARN - " + message);
    }
  }

  public static void logError(String message) {
    if(LOGGER.isErrorEnabled()) {
      LOGGER.error(message);
    } else {
      System.err.println("TrakrJ: ERROR - " + message);
    }
  }

  public static void logError(String message, Throwable t) {
    if(LOGGER.isErrorEnabled()) {
      LOGGER.error(message, t);
    } else {
      System.err.println("TrakrjJ: ERROR - " + message + "\n" + Exceptions.throwableToString(t));
    }
  }

}
