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
