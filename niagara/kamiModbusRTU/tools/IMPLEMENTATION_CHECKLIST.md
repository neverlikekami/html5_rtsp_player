# Implementation checklist (Niagara 4.13)

- [ ] Define module metadata for `kamiModbusRTU-rt` and `kamiModbusRTU-wb`
- [ ] Implement Baja types: `BKamiModbusNetwork`, `BKamiModbusDevice`, `BKamiModbusPoint`
- [ ] Add serial transport implementation with COM open/close and timeout handling
- [ ] Add Modbus function support: FC1/2/3/4/5/6/15/16
- [ ] Add register block optimizer (contiguous polling)
- [ ] Add alarm/fault states and diagnostics slots
- [ ] Add RTU->TCP bridge mode configuration
- [ ] Add Workbench views and import wizard
- [ ] Add integration tests on Windows Supervisor with USB-RS485 converter
- [ ] Package signed jars and deploy to Supervisor modules folder
