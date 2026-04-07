package com.kami.modbus.rt;

import java.io.Closeable;
import java.io.IOException;

/**
 * Abstraction for COM port access on Supervisor runtime.
 * Real implementation should bind to Niagara-supported serial API or vetted JNI library.
 */
public interface SerialPortProvider extends Closeable {
  void open(String portName, int baudRate, int dataBits, int stopBits, String parity) throws IOException;

  int write(byte[] data, int off, int len) throws IOException;

  int read(byte[] buffer, int off, int len, int timeoutMs) throws IOException;

  boolean isOpen();
}
