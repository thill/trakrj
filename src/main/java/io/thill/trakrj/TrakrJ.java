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
      StatLogger logger = Instantiate.instantiateLogger(config);
      conductor = Instantiate.instantiateConductor(config, logger);
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
