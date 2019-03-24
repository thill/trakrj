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
package io.thill.trakrj;

import io.thill.trakrj.conductor.Conductor;
import io.thill.trakrj.conductor.DisabledConductor;
import io.thill.trakrj.internal.exception.Exceptions;
import io.thill.trakrj.internal.load.Config;
import io.thill.trakrj.internal.load.Instantiate;
import io.thill.trakrj.logger.StatLogger;

import java.util.Map;

/**
 * The API entry point for TrakrJ. This class takes care of all static initialization of the {@link Stats} singleton from trakrj.properties. If
 * trakrj.properties is not found, all calls to the underlying conductor will return immediately without executing any logic.
 *
 * @author Eric Thill
 */
public class TrakrJ {

  private static final Stats STATS;

  static {
    Conductor conductor;
    try {
      Map<String, String> config = Config.loadConfig();
      Config.tryPrintConfig(config);
      StatLogger logger = Instantiate.instantiateLoggerFromSysConfig(config);
      conductor = Instantiate.instantiateConductorFromSysConfig(config, logger);
    } catch(Throwable t) {
      Exceptions.logError("Could not instantiate trackrj. All calls to " + Stats.class.getSimpleName() + " will no-op.\n", t);
      conductor = new DisabledConductor();
    }
    STATS = new Stats(conductor);
  }

  /**
   * Get the default stats instance.
   *
   * @return The stats instance
   */
  public static Stats stats() {
    return STATS;
  }

}
