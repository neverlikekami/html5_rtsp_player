package com.kami.modbus.rt;

/** Utility class for Modbus RTU CRC16 (LSB first). */
public final class ModbusCrc16 {
  private ModbusCrc16() {}

  public static int compute(byte[] data, int offset, int length) {
    int crc = 0xFFFF;
    for (int i = 0; i < length; i++) {
      crc ^= (data[offset + i] & 0xFF);
      for (int j = 0; j < 8; j++) {
        if ((crc & 0x0001) != 0) {
          crc = (crc >>> 1) ^ 0xA001;
        } else {
          crc >>>= 1;
        }
      }
    }
    return crc & 0xFFFF;
  }

  public static boolean isValidFrame(byte[] adu, int length) {
    if (length < 4) return false;
    int expected = compute(adu, 0, length - 2);
    int lo = adu[length - 2] & 0xFF;
    int hi = adu[length - 1] & 0xFF;
    int actual = (hi << 8) | lo;
    return expected == actual;
  }
}
