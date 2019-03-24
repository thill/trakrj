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
package io.thill.trakrj.logger;

import io.thill.trakrj.Tracker;
import io.thill.trakrj.TrackerId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.thill.trakrj.internal.load.Config.*;

/**
 * @author Eric Thill
 */
public class Slf4jStatLogger implements StatLogger {

  private Logger logger;

  public Slf4jStatLogger() {
    this(DEFAULT_LOGGER_NAME);
  }

  public Slf4jStatLogger(String loggerName) {
    logger = LoggerFactory.getLogger(loggerName);
  }

  @Override
  public void configure(Map<String, String> config) {
    String loggerName = config.getOrDefault(CFGKEY_LOGGER_NAME, DEFAULT_LOGGER_NAME);
    logger = LoggerFactory.getLogger(loggerName);
  }

  @Override
  public void log(TrackerId id, Tracker tracker, long timestamp) {
    logger.info("{} - {}", id.display(), tracker.toString());
  }

}
