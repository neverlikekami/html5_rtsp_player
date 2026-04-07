package com.kami.modbus.rt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Minimal RTU->TCP bridge helper.
 * Converts RTU ADU(unitId+pdu+crc) to TCP ADU(MBAP+unitId+pdu), excluding CRC.
 */
public class ModbusRtuTcpBridge {
  private final String host;
  private final int port;
  private int txId = 1;

  public ModbusRtuTcpBridge(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public byte[] execute(byte[] rtuAdu, int timeoutMs) throws IOException {
    if (rtuAdu == null || rtuAdu.length < 4) {
      throw new IOException("Invalid RTU ADU");
    }

    int unitId = rtuAdu[0] & 0xFF;
    int pduLen = rtuAdu.length - 3; // unit + pdu + 2crc => pdu = len-3

    byte[] tcp = new byte[7 + 1 + pduLen];
    int tid = txId++ & 0xFFFF;
    tcp[0] = (byte) ((tid >>> 8) & 0xFF);
    tcp[1] = (byte) (tid & 0xFF);
    tcp[2] = 0;
    tcp[3] = 0;
    int length = 1 + pduLen;
    tcp[4] = (byte) ((length >>> 8) & 0xFF);
    tcp[5] = (byte) (length & 0xFF);
    tcp[6] = (byte) unitId;
    System.arraycopy(rtuAdu, 1, tcp, 7, pduLen);

    try (Socket s = new Socket()) {
      s.connect(new InetSocketAddress(host, port), timeoutMs);
      s.setSoTimeout(timeoutMs);
      OutputStream out = s.getOutputStream();
      InputStream in = s.getInputStream();

      out.write(tcp);
      out.flush();

      byte[] mbap = in.readNBytes(7);
      if (mbap.length != 7) throw new IOException("Short MBAP response");
      int respLen = ((mbap[4] & 0xFF) << 8) | (mbap[5] & 0xFF);
      byte[] body = in.readNBytes(respLen);
      if (body.length != respLen) throw new IOException("Short Modbus body");
      return body; // unitId + pdu
    }
  }
}
