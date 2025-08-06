# 다양한 이큐먼트 인터페이스 템플릿

Spring Boot 기반 **다양한 PLC 장비와 통신할 수 있는 범용 인터페이스 템플릿** 프로젝트


## 🎯 Template Purpose

이 템플릿은 **다양한 제조사의 PLC 장비**와 통신할 수 있는 확장 가능한 아키텍처를 제공합니다:

- ✅ **Mitsubishi PLC** (Q3E Protocol)
- ✅ **Siemens PLC** (S7 Protocol) - 확장 가능
- ✅ **LS Electric PLC** - 확장 가능  
- ✅ **Allen-Bradley PLC** - 확장 가능
- ✅ **Custom Socket/Serial Communication** - 확장 가능

### Key Features
- 🔧 **Pluggable Interface Design**: 새로운 PLC 제조사 지원을 위한 인터페이스 기반 설계
- 🌐 **Multi-Protocol Support**: TCP/IP, Serial, Custom Protocol 지원
- 📊 **Flexible Data Mapping**: 다양한 데이터 포맷과 주소 체계 지원
- 🔄 **Event-Driven Architecture**: 실시간 장비 상태 변화 감지 및 처리
- 📈 **Scalable Scheduler**: 다중 PLC 동시 통신을 위한 스케줄러 시스템

## Table of Contents
- [Quick Start](#quick-start)
- [Architecture](#architecture)
- [Configuration](#configuration)
- [Database](#database)
- [PLC Communication](#plc-communication)
- [API Documentation](#api-documentation)
- [Development Guide](#development-guide)
- [Troubleshooting](#troubleshooting)

## Quick Start

### Prerequisites
- JDK 21 or higher
- PostgreSQL 12+
- Gradle 8.6+

### Getting Started

1. **Clone Repository**
```bash
git clone <repository-url>
cd equipment-interface-template
```

2. **Database Setup**
```bash
# PostgreSQL 데이터베이스 생성
createdb orca_db
```

3. **Configuration**
```bash
# application.yml 설정 확인
vi src/main/resources/application.yml
```

4. **Run Application**
```bash
./gradlew bootRun
# or
gradle bootRun
# or IDE에서 MainApplication.java 실행
```

## Architecture

### System Overview
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Upper Layer   │    │   Base Layer    │    │  Lower Layer    │
│  (API Interface)│◄──►│ (Core Control) │◄──►│ (PLC Interface) │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
  ┌─────────────┐      ┌─────────────────┐    ┌─────────────────┐
  │ REST API    │      │ Equipment       │    │ Multi-PLC       │
  │ WebSocket   │      │ Controller      │    │ Interface       │
  │ Orca Adapter│      │ Event System    │    │ (Pluggable)     │
  └─────────────┘      └─────────────────┘    └─────────────────┘
```

### PLC Interface Template Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                    Lower Layer (PLC Interface)             │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌────── │
│  │ Mitsubishi  │  │  Siemens    │  │ LS Electric │  │ ...   │
│  │ Controller  │  │ Controller  │  │ Controller  │  │ Custom│
│  │             │  │             │  │             │  │       │
│  │ • Q3E       │  │ • S7        │  │ • XGT       │  │ • TCP │
│  │ • ASCII/BIN │  │ • TCP/IP    │  │ • Serial    │  │ • UDP │
│  │ • Socket    │  │ • MPI       │  │ • Ethernet  │  │ • ...  │
│  └─────────────┘  └─────────────┘  └─────────────┘  └────── │
├─────────────────────────────────────────────────────────────┤
│                 Common PLC Interface (IMplcManage)         │
│  • readProcess() • writeProcess() • init() • clean()       │
└─────────────────────────────────────────────────────────────┘
```

### Multi-Layer Controller Architecture
- **Upper Controller**: 외부 시스템 API 인터페이스 (REST, WebSocket, Orca Adapter)
- **Base Controller**: 핵심 설비 제어 로직 및 이벤트 처리 (제조사 독립적)
- **Lower Controller**: 다양한 PLC 통신 어댑터 (제조사별 구현체)

### Pluggable PLC Interface Design
템플릿은 새로운 PLC 제조사 지원을 위한 확장 가능한 구조를 제공합니다:

```java
// 공통 인터페이스
public interface IMplcManage {
    void init(String cfgCode, CommunicateFormat dataType);
    void readProcess(Object socketObject, int readAddress, Map<Integer, Data> readData);
    void writeProcess(Object socketObject, String deviceCode, int writeStartAddr, int[] writeData);
    void clean(String cfgCode, CommunicateFormat dataType);
}

// 제조사별 구현체 예시
@Service
public class MitsubishiPlcService implements IMplcManage { ... }

@Service  
public class SiemensPlcService implements IMplcManage { ... }

@Service
public class LSElectricPlcService implements IMplcManage { ... }
```

## Configuration

### Check-List

1. **Application Name**
   - `application.yml` : `spring.application.name`

2. **Database Configuration**
   - 다중 데이터소스 설정 (A, B)
   - Connection Pool 설정

3. **PLC Communication**
   - MD_DEVICE_CTRL 테이블 설정
   - MD_DEVICE_CTRL_MELSEC 테이블 설정

4. **Scheduler Configuration**
   - Task 주기 설정
   - Thread Pool 설정

### Environment Variables
```properties
# Database
SPRING_DATASOURCE_A_URL=jdbc:postgresql://localhost:5432/orca_db_a
SPRING_DATASOURCE_B_URL=jdbc:postgresql://localhost:5432/orca_db_b

# PLC Settings
PLC_HOST_IP=192.168.3.49
PLC_HOST_PORT=3000

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_ORCA=DEBUG
```

## Database

### Multi-DataSource Support
- **DataSource A**: 메인 업무 데이터
- **DataSource B**: 보조 데이터 (로그, 통계 등)

### Key Tables Structure

#### Device Control Configuration
```sql
-- 디바이스 제어기 정보 (PLC 독립적)
MD_DEVICE_CTRL (
    cfg_code VARCHAR,     -- 제어기 코드 (고유 식별자)
    cfg_name VARCHAR,     -- 제어기 명
    cfg_model VARCHAR,    -- 제어기 모델 (1001: Mitsubishi, 1002: Siemens, ...)
    ctrl_ip VARCHAR,      -- IP 주소
    ctrl_port INTEGER,    -- 포트
    inf_type VARCHAR,     -- 인터페이스 타입 (1: Socket, 2: Serial, ...)
    inf_model VARCHAR,    -- 인터페이스 모델 (C: Client, S: Server)
    use_yn VARCHAR        -- 사용여부
);

-- PLC별 확장 설정 테이블 (필요에 따라 생성)
MD_DEVICE_CTRL_MELSEC    -- Mitsubishi PLC 설정
MD_DEVICE_CTRL_SIEMENS   -- Siemens PLC 설정  
MD_DEVICE_CTRL_LS        -- LS Electric PLC 설정
...
```

> **Note**: 실제 PLC 설정 테이블은 프로젝트 요구사항에 따라 다를 수 있습니다. 위 구조는 템플릿 예시이며, 실제 구현 시 해당 PLC 제조사의 사양에 맞게 조정하시기 바랍니다.

## PLC Communication Template

### Multi-Vendor PLC Support Architecture

이 템플릿은 다양한 PLC 제조사를 지원하기 위한 확장 가능한 구조를 제공합니다:

#### Supported PLC Types (확장 가능)
```java
public enum EMdDeviceCtrlModel {
    M_PLC("1001"),    // Mitsubishi PLC (Q3E Protocol)
    S_PLC("1002"),    // Siemens PLC (S7 Protocol)  
    L_PLC("1003"),    // LS Electric PLC (XGT Protocol)
    AB_PLC("1004"),   // Allen-Bradley PLC (확장 예정)
    CUSTOM("9999"),   // Custom Protocol
    NONE("0000")
}
```

#### Communication Interface Types
```java
public enum EMdDeviceCtrlInfType {
    SOCKET("1"),      // TCP/IP Socket
    SERIAL("2"),      // Serial Communication (RS232/RS485)
    MODBUS("3"),      // Modbus Protocol (확장 예정)
    OPCUA("4"),       // OPC UA (확장 예정)
    ETC("0"),         // Custom Interface
    NONE("9")
}
```

### Generic Data Processing Flow
```
DB 설정 조회 → 프로토콜 선택 → PLC 통신 → 데이터 파싱 → 표준 형식 변환 → 이벤트 발행
```

### PLC Service Router
```java
@Service
public class PlcRelayService {
    
    public IMplcManage getPlcService(String cfgCode) {
        // cfg_code에 따른 동적 서비스 라우팅
        return switch (cfgCode) {
            case "mitsubishi_001" -> mitsubishiPlcService;
            case "siemens_001" -> siemensPlcService;
            case "ls_001" -> lsElectricPlcService;
            default -> throw new UnsupportedPlcException(cfgCode);
        };
    }
}
```

### Data Structure Template
```java
// PLC 독립적인 공통 데이터 구조
Map<Integer, GenericPlcData> readData = {
    address1 → GenericPlcData(value, format, metadata),
    address2 → GenericPlcData(value, format, metadata),
    // ... 주소별 데이터
}
```

> **Note**: 실제 PLC 통신 프로토콜과 데이터 구조는 제조사별로 다를 수 있습니다. 이 템플릿은 범용적인 인터페이스를 제공하며, 구체적인 구현은 해당 PLC의 사양서에 따라 개발하시기 바랍니다.

## ENVIRONMENTS

- **Spring Boot**: 3.4.2
- **OpenJDK**: 21
- **Gradle**: 8.6
- **PostgreSQL**: 12+
- **Lombok**: Latest
- **MyBatis**: 3.x
- **HikariCP**: Connection Pooling

### External Libraries
- **Multi-PLC Support**: 다양한 PLC 통신 라이브러리 지원
  - altiall-plc-mitsubishi (Mitsubishi PLC)
  - 기타 PLC 라이브러리 (프로젝트별 추가)
- **Socket Communication**: altiall-socket-java
- **Scheduler**: altiall-scheduler  
- **Converter**: altiall-convert

## Project Structure

```
├── src/main/java/com/orca
│   ├── common                    -- 공통 모듈
│   │   ├── constants            -- 상수 정의
│   │   ├── enums               -- Enum 타입 정의
│   │   ├── exception           -- 예외 처리
│   │   ├── filter              -- 서블릿 필터
│   │   ├── interceptor         -- Spring 인터셉터
│   │   └── utils               -- 유틸리티 클래스
│   ├── config                   -- 설정 클래스
│   │   ├── core                -- 핵심 설정 (Web, Message, etc.)
│   │   ├── datasource          -- 데이터소스 설정
│   │   ├── mybatis             -- MyBatis 설정
│   │   ├── scheduler           -- 스케줄러 설정
│   │   ├── websocket           -- WebSocket 설정
│   │   └── security            -- 보안 설정
│   ├── controller              -- 컨트롤러 계층
│   │   ├── base                -- 핵심 설비 제어 컨트롤러 (PLC 독립적)
│   │   ├── lower               -- 하위 디바이스 컨트롤러 (PLC별 구현)
│   │   │   ├── melsecplc       -- Mitsubishi PLC 컨트롤러
│   │   │   ├── siemensplc      -- Siemens PLC 컨트롤러 (확장 예정)
│   │   │   ├── lsplc           -- LS Electric PLC 컨트롤러 (확장 예정)
│   │   │   └── customprotocol  -- Custom Protocol 컨트롤러 (확장 예정)
│   │   ├── upper               -- 상위 인터페이스 컨트롤러
│   │   └── web                 -- 웹 페이지 컨트롤러
│   ├── repository              -- 데이터 접근 계층
│   │   ├── dto                 -- 데이터 전송 객체
│   │   │   ├── domain          -- 복합 도메인 객체
│   │   │   └── entity          -- 엔티티 객체
│   │   └── mapper              -- MyBatis 매퍼
│   │       ├── a               -- 데이터소스 A 매퍼
│   │       └── b               -- 데이터소스 B 매퍼
│   ├── service                 -- 서비스 계층
│   ├── AppStartEventListener   -- 애플리케이션 시작 이벤트
│   ├── AppStopEventListener    -- 애플리케이션 종료 이벤트
│   └── MainApplication         -- 메인 클래스
├── src/main/resources
│   ├── mapper                  -- MyBatis XML 매퍼
│   │   ├── a                   -- 데이터소스 A 매퍼 XML
│   │   └── b                   -- 데이터소스 B 매퍼 XML
│   ├── messages               -- 다국어 메시지
│   ├── static                 -- 정적 리소스
│   └── templates              -- Thymeleaf 템플릿
├── src/test/java              -- 테스트 코드
├── build.gradle
└── settings.gradle
```

### Package Naming Convention
> Spring Boot 패키지명 네이밍은 소문자, 단어는 점(.) 으로 구분하는 것이 표준이며, 보통 도메인 기준으로 나눈다

## API Documentation

### Swagger UI
- **URL**: http://localhost:8080/swagger-ui.html
- **Description**: Interactive API documentation

### ReDoc
- **URL**: http://localhost:8080/redoc
- **Description**: Clean API documentation

### Test Pages
- **Main Page**: http://localhost:8080
- **Description**: Thymeleaf 기반 테스트 페이지

## Development Guide

### Adding New PLC Support

새로운 PLC 제조사 지원을 추가하는 방법:

#### 1. Enum 정의 추가
```java
// EMdDeviceCtrlModel.java
public enum EMdDeviceCtrlModel {
    // 기존 정의들...
    NEW_PLC("1005"),    // 새로운 PLC 모델 추가
}
```

#### 2. 서비스 구현체 작성
```java
@Service
@Slf4j
@RequiredArgsConstructor
public class NewPlcService implements IMplcManage {
    
    @Override
    public void init(String cfgCode, CommunicateFormat dataType) {
        // PLC별 초기화 로직 구현
    }
    
    @Override
    public void readProcess(Object socketObject, int readAddress, Map<Integer, PlcData> readData) {
        // PLC별 읽기 처리 로직 구현
    }
    
    @Override
    public void writeProcess(Object socketObject, String deviceCode, int writeStartAddr, int[] writeData) {
        // PLC별 쓰기 처리 로직 구현  
    }
}
```

#### 3. 라우터에 서비스 등록
```java
// PlcRelayService.java
@Service
@RequiredArgsConstructor
public class PlcRelayService {
    
    private final NewPlcService newPlcService;  // 새 서비스 주입
    
    public IMplcManage getPlcService(String cfgCode) {
        return switch (cfgCode) {
            // 기존 케이스들...
            case "newplc_001" -> newPlcService;  // 새 서비스 추가
            default -> null;
        };
    }
}
```

#### 4. DB 설정 테이블 생성 (필요시)
```sql
-- 새로운 PLC 전용 설정 테이블 (예시)
CREATE TABLE MD_DEVICE_CTRL_NEWPLC (
    cfg_code VARCHAR PRIMARY KEY,
    protocol_version VARCHAR,
    custom_param1 VARCHAR,
    custom_param2 INTEGER,
    -- PLC별 특화 설정 필드들...
);
```

### Event-Driven Architecture
```java
// Base → Lower 이벤트 (제어 명령)
EqControlEvent: 설비 제어 명령 전달 (PLC 독립적)

// Lower → Base 이벤트 (상태 보고)
EqInjectionEvent: 설비 상태 변화 보고 (PLC 독립적)
```

### Scheduler System
- **EquipmentController**: 메인 설비 제어 스케줄러 (PLC 독립적)
- **Various PLC Controllers**: PLC별 통신 스케줄러 (동적 생성)
- **주기**: 1초 (설정 가능)
- **동적 등록**: `AppStartEventListener`에서 DB 설정 기반으로 자동 생성

### Custom PLC Service Template
```java
// 새로운 PLC 지원을 위한 템플릿
public interface IMplcManage {
    void init(String cfgCode, CommunicateFormat dataType);
    void clean(String cfgCode, CommunicateFormat dataType);
    
    // 핵심 통신 메서드
    void readProcess(Object socketObject, int readAddress, Map<Integer, PlcData> readData) throws Exception;
    void writeProcess(Object socketObject, String deviceCode, int writeStartAddr, int[] writeData) throws Exception;
    
    // PLC별 특화 기능
    void featureProcess(Object socketObject) throws Exception;
}
```

## Common Enums

설비 관련 공통 Enum 정의는 다음 링크 참조:
[Equipment Enum Spreadsheet](https://docs.google.com/spreadsheets/d/1hNaViiZmocJwpExquFMetk87blXg344loKUXGhFHeYA/edit?usp=drive_link)

### Key Enums
- `EMdEqType`: 설비 타입 (PICK, ROUTE, SORT, STORAGE)
- `EMdDeviceCtrlModel`: 제어기 모델 (M_PLC, S_PLC, L_PLC)
- `EUseYn`: 사용 여부 (Y/N)

## Logging Configuration

### Log Levels
- **ROOT**: INFO
- **com.eqca**: DEBUG
- **PLC Communication**: TRACE (상세 비트 분석)

### Log Pattern Example
```
[P1-01] DB 설정 로딩 단계 - schedulerId: mplc1
[P2-03] PLC 통신 시작 - 192.168.3.49:3000
[P4-05] 데이터 파싱 완료 - Map<Integer, MelsecData> 15개
[P5-01] 비트 분석 - 총 240비트, 설정비트: 0개 (0.0%)
```

## Troubleshooting

### Common Issues

#### 1. PLC 연결 실패
```bash
# 네트워크 연결 확인
ping [PLC_IP_ADDRESS]
telnet [PLC_IP_ADDRESS] [PLC_PORT]

# 방화벽 확인
sudo ufw status
```

#### 2. 데이터베이스 연결 실패
```bash
# PostgreSQL 서비스 상태 확인
sudo systemctl status postgresql

# 연결 테스트
psql -h localhost -p 5432 -U [username] -d [database_name]
```

#### 3. 새로운 PLC 지원 추가 시 주의사항
- `EMdDeviceCtrlModel` enum에 새 PLC 모델 추가
- `PlcRelayService`에 새 서비스 라우팅 추가
- `AppStartEventListener`에서 새 PLC 컨트롤러 생성 로직 추가
- PLC별 특화 설정 테이블 생성 (필요시)

#### 4. 스케줄러 동작 안 함  
- `MD_DEVICE_CTRL` 테이블의 `use_yn` 확인
- 로그에서 스케줄러 등록 여부 확인
- PLC별 서비스 구현체 Bean 등록 확인

### Debug Mode
```yaml
logging:
  level:
    com.eqca: DEBUG
    com.eqca.controller.lower.melsecplc: TRACE
```

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

- **Issues**: GitHub Issues
- **Documentation**: Wiki
- **Contact**: [Your Contact Information]