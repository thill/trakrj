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
