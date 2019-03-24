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
import io.thill.trakrj.internal.exception.Exceptions;
import io.thill.trakrj.internal.load.Config;
import io.thill.trakrj.internal.load.Instantiate;

import java.util.*;

/**
 * @author Eric Thill
 */
public class MultiStatLogger implements StatLogger {

  private StatLogger[] loggers;

  public MultiStatLogger() {
    loggers = new StatLogger[0];
  }

  public MultiStatLogger(StatLogger... loggers) {
    this.loggers = loggers;
  }

  @Override
  public void configure(Map<String, String> config) {
    if(config == null || config.isEmpty()) {
      return;
    }
    final Set<String> statLoggerNames = new LinkedHashSet<>();
    for(String key : config.keySet()) {
      statLoggerNames.add(key.split("\\.")[0]);
    }
    final List<StatLogger> loggers = new ArrayList<>();
    for(String name : statLoggerNames) {
      Map<String, String> sub = Config.subConfig(config, name + ".", Collections.emptyList());
      try {
        loggers.add(Instantiate.instantiateLogger(sub));
      } catch(Throwable t) {
        Exceptions.logError("Could not instantiate logger '" + name + "'", t);
      }
    }
    this.loggers = loggers.toArray(new StatLogger[loggers.size()]);
  }

  @Override
  public void log(TrackerId id, Tracker tracker, long timestamp) {
    for(int i = 0; i < loggers.length; i++) {
      loggers[i].log(id, tracker, timestamp);
    }
  }

}
