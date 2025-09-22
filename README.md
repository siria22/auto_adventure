## Android Project Template

- Siria22's Android Template
- Last update: 2025-09-16

## Structure

### Clean Architecture

- App
  - data
  - domain
  - presentation

### MVI(Model - View - Intent) Pattern

- ${NAME}Argument.kt: 화면의 상태(State), 사용자 입력(Intent), 그리고 ViewModel로부터 전달되는 일회성 이벤트(Event)를 정의합니다.
  - State: UI가 그려져야 할 상태를 정의하는 데이터 클래스. (e.g., Init, Loading, Success, Error)
  - Intent: 사용자의 행동이나 화면의 생명주기 이벤트를 정의. (e.g., OnButtonClicked, OnScreenResumed)
  - Event: 화면에 한 번만 표시되어야 하는 이벤트. (e.g., ShowSnackbar, NavigateToDetail)
- ${NAME}ViewModel.kt: Intent를 받아 비즈니스 로직을 처리하고, 그 결과로 State를 변경하거나 Event를 발생시킵니다.
- ${NAME}Data.kt: 화면을 구성하는 데 필요한 데이터 중, State와는 별개로 관리되는 UI용 데이터를 정의합니다. (e.g., LazyPagingItems, List<String>)
- ${NAME}Screen.kt: State와 Data를 기반으로 UI를 렌더링하고, 사용자 입력을 받아 Intent를 ViewModel로 전달합니다.
- ${NAME}Destination.kt: Jetpack Navigation Compose의 NavGraphBuilder 확장 함수로, ViewModel과 화면을 연결하고 의존성을 주입하는 역할을 합니다.

## Libs & Dependencies

- 모든 의존성은 `libs.versions.toml`에서 관리

### **Android & Jetpack Core**

| 라이브러리            | 버전       | 주요 사용 모듈                      | 설명                                                       |
|:-----------------|:---------|:------------------------------|:---------------------------------------------------------|
| Core KTX         | `1.17.0` | `app`, `data`, `presentation` | Kotlin 확장 기능을 제공하여 API를 간결하게 사용하도록 돕습니다.                 |
| AppCompat        | `1.7.1`  | `app`, `data`, `presentation` | 다양한 Android 버전에서 일관된 UI와 동작을 제공합니다.                      |
| Lifecycle        | `2.9.3`  | `app`, `presentation`         | ViewModel, LifecycleOwner 등 생명주기 인식 컴포넌트를 관리합니다.         |
| Activity Compose | `1.11.0` | `app`                         | `ComponentActivity`에서 Jetpack Compose를 호스팅할 수 있도록 지원합니다. |

### **UI (Jetpack Compose)**

| 라이브러리       | 버전           | 주요 사용 모듈              | 설명                                         |
|:------------|:-------------|:----------------------|:-------------------------------------------|
| Compose BOM | `2025.09.00` | `app`, `presentation` | 여러 Compose 라이브러리의 호환되는 버전을 관리합니다.          |
| Material 3  | `1.3.2`      | `app`, `presentation` | 최신 Material Design 시스템을 구현한 UI 컴포넌트 모음입니다. |
| Navigation  | `2.9.4`      | `app`, `presentation` | Compose 애플리케이션 내 화면 전환과 탐색을 관리합니다.         |
| Coil        | `2.7.0`      | `presentation`        | 이미지 로딩 및 캐싱을 위한 경량 라이브러리입니다.               |
| Accompanist | `0.37.3`     | `presentation`        | 권한 요청 등 Compose에서 아직 지원하지 않는 기능을 보완합니다.    |

### **Hilt**

| 라이브러리                   | 버전       | 주요 사용 모듈                      | 설명                                                   |
|:------------------------|:---------|:------------------------------|:-----------------------------------------------------|
| Hilt Android            | `2.57.1` | `app`, `data`, `presentation` | Android 클래스에 대한 의존성 주입을 간소화합니다.                      |
| Hilt Navigation Compose | `1.3.0`  | `presentation`                | Compose Navigation 그래프에서 ViewModel을 주입할 수 있도록 지원합니다. |
| Hilt Core               | `2.57.1` | `domain`                      | 순수 Kotlin/Java 모듈에서 Hilt를 사용하기 위한 핵심 라이브러리입니다.       |

### **Data & Networking**

| 라이브러리           | 버전      | 주요 사용 모듈 | 설명                                              |
|:----------------|:--------|:---------|:------------------------------------------------|
| Ktor            | `3.3.0` | `data`   | Kotlin 기반의 비동기 네트워킹 프레임워크로, API 통신에 사용됩니다.      |
| Room            | `2.8.0` | `data`   | 로컬 데이터베이스를 쉽게 사용하기 위한 ORM 라이브러리입니다.             |
| DataStore       | `1.1.7` | `data`   | 프로토콜 버퍼를 사용하여 데이터를 비동기적으로 저장하는 라이브러리입니다.        |
| Security Crypto | `1.1.0` | `data`   | 암호화된 `SharedPreferences`를 생성하여 데이터를 안전하게 저장합니다. |

### **Test**

| 라이브러리           | 버전       | 주요 사용 모듈                                | 설명                                         |
|:----------------|:---------|:----------------------------------------|:-------------------------------------------|
| JUnit           | `4.13.2` | `app`, `data`, `domain`, `presentation` | Java 및 Kotlin 코드의 단위 테스트를 위한 표준 프레임워크입니다.  |
| Espresso        | `3.7.0`  | `app`, `data`                           | Android UI 테스트를 자동화하는 데 사용됩니다.             |
| Compose UI Test | `-`      | `app`                                   | Jetpack Compose UI의 동작을 검증하기 위한 테스트 도구입니다. |


## TODO

### CheckList

- AndroidManifest.xml
  - 필요한 권한, 하드웨어 접근 요청, API key 등 확인 

- 이름 변경
  - build.gradle.kts (:app) -> namespace
  - SiriaTemplateTheme
  - SiriaTemplateColorScheme
  - Strings.xml : "app_name"
  - settings.gradle.kts : "rootProject.name"

- libs.version.toml: 모든 의존성 버전 확인 후 조율

- AppKeyProvider, CryptoManager
  - Secret 관리 정책에 따라 TODO 구현


(* 일부 내용은 Windsurf - Gemini 2.5 Pro로 작성됨 *)