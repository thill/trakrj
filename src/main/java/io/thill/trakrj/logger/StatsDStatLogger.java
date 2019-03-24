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

import io.thill.trakrj.Stat;
import io.thill.trakrj.Stat.StatType;
import io.thill.trakrj.Tracker;
import io.thill.trakrj.TrackerId;
import io.thill.trakrj.internal.exception.Exceptions;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author Eric Thill
 */
public class StatsDStatLogger implements StatLogger {

  private static final String CFGKEY_HOST = "hostname";
  private static final String CFGKEY_PORT = "port";
  private static final String CFGKEY_PACKET_SIZE = "packet.size";
  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 8125;
  private static final int DEFAULT_PACKET_SIZE = 500;
  private static final Charset UTF8 = Charset.forName("UTF-8");

  private String hostname;
  private int port;
  private int packetSize;
  private DatagramChannel socket;


  public StatsDStatLogger() {
    this(CFGKEY_HOST, DEFAULT_PORT, DEFAULT_PACKET_SIZE);
  }

  public StatsDStatLogger(String hostname, int port) {
    this(hostname, port, DEFAULT_PACKET_SIZE);
  }

  public StatsDStatLogger(String hostname, int port, int packetSize) {
    this.hostname = hostname;
    this.port = port;
    this.packetSize = packetSize;
  }

  @Override
  public void configure(Map<String, String> config) {
    hostname = config.getOrDefault(CFGKEY_HOST, DEFAULT_HOST);
    port = Integer.parseInt(config.getOrDefault(CFGKEY_PORT, Integer.toString(DEFAULT_PORT)));
    socket = null;
  }

  private void connect() throws IOException {
    socket = DatagramChannel.open();
    socket.connect(new InetSocketAddress(hostname, port));
  }

  @Override
  public void log(TrackerId id, Tracker tracker, long timestamp) {
    List<? extends Stat> stats = tracker.stats();
    try {
      // connect
      if(socket == null) {
        connect();
      }

      // send packet(s)
      StringBuilder packet = new StringBuilder();
      for(Stat s : stats) {
        if(s.type() != StatType.OBJECT && !s.isNull()) {
          StringBuilder statStr = new StringBuilder()
                  .append(id.display()).append(".").append(s.name());
          switch(s.type()) {
            case DOUBLE:
              statStr.append(s.doubleValue());
              break;
            case LONG:
              statStr.append(s.longValue());
              break;
          }
          statStr.append("|g");

          // flush packet if length would exceed packetSize
          if(packet.length() > 0 && packet.length() + statStr.length() + 1 > packetSize) {
            send(packet.toString());
            packet.setLength(0);
          }

          // append newline if this is not the first stat in the packet
          if(packet.length() > 0) {
            packet.append('\n');
          }

          // append stat to packet
          packet.append(statStr.length());
        }

        // send remaining packet
        if(packet.length() > 0) {
          send(packet.toString());
        }
      }
    } catch(IOException e) {
      Exceptions.logError("Could not send StatsD packet", e);
      socket = null;
    }
  }

  private void send(String str) throws IOException {
    final byte[] sendData = str.getBytes(UTF8);
    socket.write(ByteBuffer.wrap(sendData));
  }

}
