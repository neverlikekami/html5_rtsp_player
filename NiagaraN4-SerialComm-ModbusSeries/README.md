# NiagaraN4-SerialComm-ModbusSeries

Niagara N4.13 Supervisor(Windows)에서 USB-RS485(COM Port) 기반 **Modbus RTU 통신**을 수행하고,
옵션으로 **ModbusTCP 브리지**를 지원하기 위한 독립 마스터 프로젝트입니다.

## 프로젝트 구조

```text
NiagaraN4-SerialComm-ModbusSeries/
  README.md
  .gitignore
  vendor/
    README.md
  kamiModbusRTU/
    README.md
    rt/src/com/kami/modbus/rt/*.java
    wb/src/com/kami/modbus/wb/*.java
    tools/IMPLEMENTATION_CHECKLIST.md
```

## 라이브러리 업로드 위치 (권장)

Niagara SDK/Baja 라이브러리는 아래 경로에 업로드하세요.

- `NiagaraN4-SerialComm-ModbusSeries/vendor/niagara/4.13/`

예시:

```text
NiagaraN4-SerialComm-ModbusSeries/vendor/niagara/4.13/
  modules/
    baja.jar
    nre.jar
    ...
  lib/
    ...
```

## 다음 단계

1. 라이브러리 업로드
2. `kamiModbusRTU` 빌드 스크립트(Gradle/Ant) 연결
3. `kamiModbusRTU-rt.jar`, `kamiModbusRTU-wb.jar` 생성
4. Supervisor `modules/`에 배포 후 검증

## 참고

기존 `html5_rtsp_player` 영역에서 분리해 `NiagaraN4-SerialComm-ModbusSeries`를 새로운 마스터 기준으로 사용합니다.
