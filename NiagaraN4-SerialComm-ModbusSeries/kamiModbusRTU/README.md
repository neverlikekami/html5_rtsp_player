# kamiModbusRTU (Niagara 4.13) - Starter Blueprint

이 폴더는 **Niagara N4 Supervisor(Windows)** 에서 USB-RS485(가상 COM 포트)를 통해 Modbus RTU 통신을 수행하고,
필요 시 Modbus TCP 로 브리지할 수 있는 커스텀 모듈의 **설계/코드 스캐폴드**를 제공합니다.

> 이 저장소에는 Niagara SDK/Baja 라이브러리가 포함되어 있지 않아 즉시 `.jar`를 빌드할 수 없습니다.
> 실제 `kamiModbusRTU-rt.jar`, `kamiModbusRTU-wb.jar` 생성은 Niagara 4.13 SDK 환경에서 수행해야 합니다.

## 목표
- `kamiModbusRTU-rt.jar`: 런타임 드라이버
  - COM 포트 열기/닫기, RTU 프레임 송수신, CRC 검증
  - 폴링, 타임아웃, 재시도, 상태 포인트
  - Supervisor에서 다중 장치(unit id) 지원
- `kamiModbusRTU-wb.jar`: 워크벤치 UI/프로비저닝 지원
  - Device Manager 뷰, 포인트 매핑, Discover/Batch Add
- 확장 기능
  - RTU 요청을 ModbusTCP(502)로 매핑하는 브리지 모드

## 권장 아키텍처
1. **Serial Layer**
   - `SerialPortProvider` 인터페이스
   - 구현체 2개
     - 순수 COM 직접 접근(Windows): `ComSerialProvider`
     - (옵션) 외부 게이트웨이 사용: `TcpBridgeProvider`
2. **Protocol Layer**
   - `ModbusRtuFrameCodec` (PDU + CRC16)
   - `ModbusTransactionManager` (요청/응답 매칭, timeout/retry)
3. **Niagara Driver Layer**
   - `BKamiModbusNetwork`, `BKamiModbusDevice`, `BKamiModbusPoint`
   - Poll Scheduler + COV 유사 최적화(연속 레지스터 묶음)
4. **RTU→TCP Bridge Layer (옵션)**
   - RTU ADU <-> TCP MBAP 변환
   - gateway endpoint(호스트/포트)별 connection pool

## 모듈 구성 제안
- module name: `kamiModbusRTU`
- rt module: `kamiModbusRTU-rt`
- wb module: `kamiModbusRTU-wb`

### 디렉터리
- `rt/src/com/kami/modbus/rt/` : 런타임 코드
- `wb/src/com/kami/modbus/wb/` : 워크벤치 확장 코드
- `tools/` : 빌드/패키징 보조 스크립트

## Niagara 4.13 SDK에서 빌드 절차(요약)
1. Niagara SDK 4.13 설치 및 `niagara_home` 설정
2. 이 폴더를 Niagara 모듈 워크스페이스로 복사
3. `module.xml`, `build.gradle`(또는 vendor build 스크립트)에서 Baja 의존성 연결
4. 서명 인증서/개발키 설정
5. `rt`, `wb` 각각 빌드하여 jar 생성
6. Supervisor `modules/` 배포 후 재시작

## 필수 운영 포인트
- 포트 충돌 감지: 이미 점유된 COM 포트 즉시 에러/재시도 백오프
- 통신 품질 메트릭: `LastOkTs`, `TimeoutCount`, `CrcErrorCount`, `ReconnectCount`
- 장치 오프라인 정책: n회 실패 시 fault, 자동 복구
- 시리얼 파라미터 핫리로드 시 안전한 재연결

## 주의
- Niagara 기본 Modbus 드라이버의 “모든 기능” 복제는 버전별/라이선스별 차이가 있어
  우선순위를 정해 단계적으로 구현해야 합니다(읽기/쓰기 FC 1/2/3/4/5/6/15/16 → batch optimize → discover UX).
