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

import javax.xml.bind.annotation.XmlType.DEFAULT;
import java.util.Date;
import java.util.Map;

import static io.thill.trakrj.internal.load.Config.*;

/**
 * @author Eric Thill
 */
public class StderrStatLogger implements StatLogger {

  private String loggerName;

  public StderrStatLogger() {
    this(DEFAULT_LOGGER_NAME);
  }

  public StderrStatLogger(String loggerName) {
    this.loggerName = loggerName;
  }

  @Override
  public void configure(Map<String, String> config) {
    loggerName = config.getOrDefault(CFGKEY_LOGGER_NAME, DEFAULT_LOGGER_NAME);
  }

  @Override
  public void log(TrackerId id, Tracker tracker, long timestamp) {
    System.err.println(new Date(timestamp) + " - " + loggerName + " - " + id.display() + " - " + tracker.toString());
  }

}
