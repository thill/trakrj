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

  @Override
  public void close() {
    for(int i = 0; i < loggers.length; i++) {
      try {
        loggers[i].close();
      } catch(Throwable t) {
        Exceptions.logError("Error closing " + loggers[i].getClass().getSimpleName(), t);
      }
    }
  }
}
