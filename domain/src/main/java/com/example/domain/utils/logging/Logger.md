## Logger Usage Guide

`Logger`는 계층형 로그 생성을 위한 유틸리티 클래스로, Hilt로 싱글톤 객체로 관리되므로 어느 곳에서나 유일한 인스턴스로 사용할 수 있음.
단 Race condition 등이 방지된 상황에서 작동함을 전제로 두므로, 동시에 여러 곳에서 사용되는 시나리오는 피해야 함

### Core Principles

1. `suspend fun startLogging()` 호출 -> 초기화
2. Chian-based Logging : startLogging -> 이후 logEvent, appendChildLog 사용 
3. Batch save : 작성된 로그는 바로 DB에 저장되지 않으며, `suspend fun exportLogs()` 함수 호출 이후에 DB에 저장됨

---

### Key Function Descriptions

- `suspend fun startLogging()`
  - 새로운 로깅 세션 시작. 초기화 구문이므로 반드시 실행되어야 함
  - ViewModel의 init 구문에서 실행하는 것을 권고하며, 로깅 도중 호출될 경우 이전 세션의 저장되지 않은 로그는 사라짐

- `fun logEvent(...) : Logger`
  - top-level 로그 생성 (depth = 1)
  - `DomainLog`의 명세 중, 필요한 사항만 기입하도록 설정되어 있음 : `title`, `description`, `category`, `revealTime`

- `fun appendChildLog(...) : Logger`
  - 마지막으로 생성된 로그보다 한 단계 깊은 (depth = parent.depth + 1) 단계의 로그를 생성
  - 반드시 `startLogging`, `appendLogAtSameLevel` 이후 실행되어야 함

- `suspend fun appendLogAtSameLevel()`
  - 마지막으로 생성된 로그와 같은 단계의 로그를 생성
  - 직전 로그와 같은 부모를 공유함
  - 반드시 `startLogging`, `appendLogAtSameLevel` 이후 실행되어야 함

- `suspend fun exportLogs()`
  - `startLogging()` 이후 생성된 로그를 DB로 저장
  - 내부적으로 사용하는 버퍼 및 자원을 해체 로직 호출

---

### Notes

- `category` : `LogCategory` Type을 사용하여 로그의 종류를 설정할 수 있음
  - 작성 시점에서는 ADVENTURE 하나만 있으나, 추후 세분화 하거나 추가될 수 있음
- `revealTime` : 로그가 사용자에게 공개될 시간을 설정할 수 있음
  - `LogDao`의  /* with time constraint */ 이하 함수들 참고.
  - 작성 시점에서는 GetAllLog만 구현되어 있음 (추후 확장 예정)

---

### Complete Usage Example



```kotlin

@HiltViewModel
class SomeViewModel @Inject constructor(
    private val logger: Logger, // 1. Inject Logger via Hilt
    // ... other dependencies
) : BaseViewModel() {

    init {
        // 2. Start a logging session when the ViewModel is created
        launch { 
            logger.startLogging() 
            // ... other Initialize logics
        }
    }

    fun onSomeComplexProcess() {
        launch {
            // 3. Record logs (utilizing chaining)
            logger.logEvent(
                title = "Complex Process Start",
                description = "User initiated a complex process by clicking a button.",
                category = LogCategory.USER_ACTION,
                revealAfter = 5  // depth = 1
            ).appendChildLog(
                title = "Data Loading",
                description = "Fetching necessary data from the server.",
                category = LogCategory.NETWORK,
                revealAfter = 10  // depth = 2
            ).appendChildLog(
                title = "Data Processing",
                description = "Processing the fetched data.",
                category = LogCategory.SYSTEM,
                revealAfter = 15  // depth = 3
            ).appendLogAtSameLevel(
                title = "Data validation",
                description = "Check status of data.",
                category = LogCategory.SYSTEM,
                revealAfter = 20  // depth = 3
            )

            // A new event starts again with appendLog
            logger.logEvent(
                title = "Separate Event Occurred",
                description = "Another event occurred during the process.",
                category = LogCategory.SYSTEM,
                revealAfter = 30
            )

            // 4. Save all logs to the DB
            logger.exportLogs()
        }
    }
}
```