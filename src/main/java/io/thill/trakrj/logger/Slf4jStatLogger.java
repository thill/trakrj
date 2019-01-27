package io.thill.trakrj.logger;

import io.thill.trakrj.Tracker;
import io.thill.trakrj.TrackerId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Eric Thill
 */
public class Slf4jStatLogger implements StatLogger {

  private final Logger logger = LoggerFactory.getLogger("TrakrJ");

  @Override
  public void configure(Map<String, String> config) {

  }

  @Override
  public void log(TrackerId id, Tracker tracker) {
    logger.info("{} - {}", id.display(), tracker.toString());
  }

}
