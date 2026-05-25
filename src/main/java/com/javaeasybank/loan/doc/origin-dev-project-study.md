# origin/dev 專案整體學習文件

> 基準分支：`origin/dev`  
> 基準版本：`c8b01fd85838b234ecda7415238b8ea8501f397a`  
> 整理日期：2026-05-22  
> 範圍：依 `origin/dev` 的檔案樹、`pom.xml`、`frontend/package.json` 與現有專案文件靜態整理。此文件刻意作為整體專案學習文件，不把 `feature-loan-V2` 的分支差異當成主軸。

## 1. 專案定位

Java Easy Bank 是一套銀行業務系統，分成客戶端與管理端。

| 使用端 | 對象 | 前端路徑 | 後端 API 型態 |
|---|---|---|---|
| 客戶端 | 一般客戶 | `/`, `/login`, `/user/**` | 多數為 `/api/customer/**` 或業務模組自有 API |
| 管理端 | 行員 / 管理者 / 資安角色 | `/admin/login`, `/admin/**` | 多數為 `/api/admin/**`, `/api/auth/**` |

整體模組：

| 模組 | 職責 |
|---|---|
| `common` | 共用設定、JWT、安全、例外、回應格式、檔案、Email、匯率 |
| `auth` | 管理端員工登入、角色、員工管理、系統日誌 |
| `customer` | 客戶註冊、登入、個人資料、密碼重設、登入紀錄、裝置管理 |
| `account` | 帳戶、開戶申請、轉帳、交易紀錄、換匯、電子存摺、常用帳號、預約轉帳 |
| `loan` | 貸款申請、聯繫、二次填單、風控送審、帳戶整合、還款 |
| `creditcard` | 信用卡卡別、申請、審核、發卡、交易、帳單、繳款 |
| `risk` | 黑名單、信用資料、風控事件、審核任務、風險檢查 |
| `notification` | 客戶站內通知、未讀狀態、通知偏好 |
| `frontend` | Vue 3 客戶端與管理端畫面 |

## 2. 技術棧

### 2.1 後端

| 類別 | 技術 | 使用方式 |
|---|---|---|
| 語言 | Java 21 | 主力後端語言 |
| 框架 | Spring Boot 4.0.5 | 專案啟動、Bean 管理、自動設定 |
| Web | Spring Web / MVC | REST API |
| ORM | Spring Data JPA / Hibernate | Entity / Repository / 查詢 |
| Security | Spring Security | 管理端 session、客戶端 JWT、API 權限 |
| Validation | Spring Validation | DTO 驗證 |
| DB | Microsoft SQL Server JDBC | SQL Server 連線 |
| DTO 工具 | Lombok | Getter / Setter / Constructor |
| Mapper | MapStruct | 信用卡 DTO / Entity 轉換 |
| JWT | JJWT 0.12.6 | 客戶端 token |
| Mail | Spring Boot Starter Mail + Thymeleaf | HTML 通知信 |
| PDF | OpenHTMLtoPDF, iText, OpenPDF | 電子存摺、契約、帳單 |
| CSV | OpenCSV | 系統日誌匯出 |
| Build | Maven | 後端建置與測試 |

### 2.2 前端

| 類別 | 技術 | 使用方式 |
|---|---|---|
| 框架 | Vue 3 | 單檔元件與 Composition API |
| Build | Vite | dev server / production build |
| Router | Vue Router | 客戶端與管理端路由 |
| Store | Pinia | auth / customerAuth 全域狀態 |
| UI | Ant Design Vue | 表格、表單、Modal、message |
| HTTP | Axios | API instance、token、錯誤攔截 |
| CSS | Tailwind CSS + 自訂 theme | 客戶端 / 管理端視覺 |
| 日期 | Day.js | 日期格式化 |
| 圖表 | Chart.js / vue-chartjs | 首頁與統計 |
| 匯出 | xlsx + file-saver | 前端 Excel 匯出 |
| Test | Vitest | 前端單元測試 |

## 3. 專案結構

```text
java-easy-bank/
├── pom.xml
├── src/main/java/com/javaeasybank/
│   ├── JavaEasyBankApplication.java
│   ├── common/
│   ├── auth/
│   ├── customer/
│   ├── account/
│   ├── loan/
│   ├── creditcard/
│   ├── notification/
│   └── risk/
├── src/main/resources/
│   ├── application.properties
│   ├── database/
│   ├── templates/mail/
│   ├── fonts/
│   ├── static/
│   └── doc/
└── frontend/
    ├── package.json
    └── src/
        ├── api/
        ├── assets/
        ├── layouts/
        ├── router/
        ├── stores/
        └── views/
```

後端大多採用：

```text
module/
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── enums/
└── utils/
```

## 4. 啟動與運行

### 4.1 後端

常用指令：

```powershell
.\mvnw.cmd test
```

```powershell
.\mvnw.cmd spring-boot:run
```

設定重點：

| 項目 | 說明 |
|---|---|
| `application.properties` | 主要匯入本機 `application-local.properties` |
| DB | 預期使用 SQL Server |
| Server | 通常為 `localhost:8080` |
| Mail / JWT / DB credentials | 應放本機設定或部署環境，不應提交敏感資訊 |

### 4.2 前端

常用指令：

```powershell
cd frontend
npm.cmd install
npm.cmd run dev
```

```powershell
npm.cmd run build
npm.cmd run test:unit
```

前端入口：

| 檔案 | 用途 |
|---|---|
| `frontend/src/main.js` | 建立 Vue app，掛載 Pinia、Router、Ant Design Vue |
| `frontend/src/router/index.js` | 前端路由與守衛 |
| `frontend/src/api/axios.js` | API base URL、withCredentials、token、錯誤攔截 |
| `frontend/src/stores/auth.js` | 管理端登入狀態 |
| `frontend/src/stores/customerAuth.js` | 客戶端登入狀態與 JWT |

## 5. 認證與授權

| 使用端 | 認證方式 | 儲存位置 | 後端核心 |
|---|---|---|---|
| 管理端 | Session | `auth_user` + cookie | `AuthController`, `CustomUserDetailsService`, `SecurityConfig` |
| 客戶端 | JWT | `customer_user`, `customer_token` | `CustomerAuthController`, `JwtUtil`, `JwtAuthenticationFilter` |

### 5.1 管理端 session 流程

1. 前端呼叫 `POST /api/auth/login`。
2. Spring Security 驗證 email / password。
3. `CustomUserDetailsService` 從員工與角色資料表載入權限。
4. 後端將 SecurityContext 放入 session。
5. 前端存 `auth_user`。
6. 進入 `/admin/**` 時，路由守衛呼叫 `/api/auth/me` 確認 session。

### 5.2 客戶端 JWT 流程

1. 前端呼叫 `POST /api/customer/auth/login`。
2. 後端驗證 `CustomerAuth`。
3. `JwtUtil` 產生 token，帶 `role` 與 `customerId`。
4. 前端存 `customer_token`。
5. Axios request interceptor 加上 `Authorization: Bearer ...`。
6. `JwtAuthenticationFilter` 解析 token 並建立 `ROLE_CUSTOMER`。

## 6. Common 模組學習重點

| 檔案 | 功能 | 學習重點 |
|---|---|---|
| `SecurityConfig.java` | Security filter chain | API 白名單、角色限制、CORS、callback IP |
| `JwtAuthenticationFilter.java` | JWT request filter | token 解析與 SecurityContext |
| `JwtUtil.java` | token 工具 | 簽發、解析、過期驗證 |
| `ApiResponse.java` | 統一 API 回應 | `success`, `message`, `errorCode`, `data` |
| `PageResponse.java` | 統一分頁 response | 包裝 Spring Data Page |
| `GlobalExceptionHandler.java` | 全域例外處理 | 400 / 401 / 403 / 500 與錯誤格式 |
| `FileStorageService.java` | 檔案儲存 | uploads 路徑、檔名、刪除 |
| `EmailService.java` | Email 通知 | Thymeleaf template + JavaMailSender |
| `RestTemplateConfig.java` | HTTP client | 用 HttpClient 5 支援 PATCH |
| `WebConfig.java` | 靜態資源 | `/uploads/**` 對應本機檔案 |
| `ExchangeRateService.java` | 匯率 | 外部匯率或模擬匯率 |

## 7. 後端模組整理

### 7.1 Auth

| 分類 | 檔案 | 用途 |
|---|---|---|
| Controller | `AuthController.java` | 管理端登入、登出、session 檢查、員工操作 |
| Controller | `AuthActionLogController.java` | 系統日誌查詢與匯出 |
| Service | `AuthEmpService`, `AuthEmpServiceImpl` | 員工管理 |
| Service | `CustomUserDetailsService` | Spring Security 載入員工權限 |
| Service | `AuthActionLogService` | 操作日誌與 CSV 匯出 |
| Entity | `AuthEmp`, `AuthRole`, `AuthDept`, `AuthLoginLog`, `AuthActionLog` | 員工、角色、部門、登入與操作紀錄 |

學習流程：先看 `AuthController.login`，再看 `CustomUserDetailsService`，最後看 `SecurityConfig` 怎麼吃到 session。

### 7.2 Customer

| 分類 | 檔案 | 用途 |
|---|---|---|
| Controller | `CustomerAuthController.java` | 客戶註冊、登入、密碼重設、大頭照 |
| Controller | `CustomerController.java` | 管理端客戶 CRUD |
| Controller | `CustomerSecurityController.java` | 登入紀錄與裝置管理 |
| Service | `CustomerAuthServiceImpl.java` | 客戶登入、註冊、鎖定、重設密碼 |
| Service | `CustomerServiceImpl.java` | 客戶資料與開戶申請同步 |
| Service | `CustomerSecurityService.java` | 登入紀錄、裝置 |
| Service | `LoginAuditService.java` | 登入成功 / 失敗紀錄 |
| Entity | `CustomerProfile`, `CustomerAuth`, `CustomerLoginLog`, `CustomerDevice` | 客戶資料、認證、登入、裝置 |

學習流程：先看客戶註冊 / 登入，再看開戶申請如何同步到 `CustomerProfile`。

### 7.3 Account

| 分類 | 檔案 | 用途 |
|---|---|---|
| Controller | `AccountController.java` | 管理端帳戶 CRUD 與狀態 |
| Controller | `CustomerAccountController.java` | 客戶查帳戶、交易、電子存摺 |
| Controller | `AccountApplicationController.java` | 開戶申請 |
| Controller | `TransferController.java` | 轉帳 |
| Controller | `CashController.java` | 存提款 |
| Controller | `TransLogController.java` | 交易紀錄 |
| Controller | `AccountIntegrationController.java` | 貸款 / 信用卡整合用帳務 API |
| Service | `AccountService.java` | 帳戶建立與狀態 |
| Service | `AccountApplicationService.java` | 開戶申請與審核 |
| Service | `TransferService.java` | 行內 / 跨行轉帳 |
| Service | `AccountIntegrationService.java` | 貸款與信用卡帳務整合 |
| Service | `PassbookPdfService.java` | 電子存摺 PDF |
| Entity | `Account`, `TransLog`, `AccountApplication`, `FavoriteAccount`, `ScheduledTransfer`, `PendingTransfer` | 帳戶、交易、申請、常用帳號、預約 |

核心學習點：帳務交易用 `BigDecimal`、交易紀錄用 referenceId 串起同一筆業務、貸款與信用卡不直接改帳戶而走 `AccountIntegrationService`。

### 7.4 Loan

| 分類 | 檔案 | 用途 |
|---|---|---|
| Controller | `LoanApplicationController`, `LoanAdminController`, `LoanDocumentController`, `LoanAccountController`, `LoanAccountAdminController`, `LoanCallbackController` | 客戶、行員、callback API |
| Service | `LoanApplicationService`, `LoanDocumentService`, `LoanAccountService`, `LoanRepaymentService`, `LoanContractPdfService` | 申請、補件、建帳、還款、契約 |
| Client | `LoanRiskClient` | 呼叫風控 |
| Scheduler | `LoanRepaymentScheduler` | 逾期與提醒 |
| Entity | `LoanApplication`, `LoanContactLog`, `LoanReviewDetail`, `LoanDocument`, `LoanAccount`, `LoanRepayment` | 貸款完整生命週期資料 |

核心學習點：貸款是最適合練習跨模組流程的模組，會碰到 Security、JPA、Transaction、afterCommit、RestTemplate、Multipart、PDF、Email、Scheduler。

### 7.5 Credit Card

| 分類 | 檔案 | 用途 |
|---|---|---|
| Controller | `CardTypeController`, `CardTypeAdminController` | 信用卡卡別 |
| Controller | `CardApplicationController`, `CardApplicationAdminController`, `CardApplicationItemAdminController` | 信用卡申請與明細審核 |
| Controller | `CardController`, `CardAdminController` | 卡片查詢與管理 |
| Controller | `CardTxnController`, `CardTxnAdminController` | 信用卡交易 |
| Controller | `CardBillController`, `CardBillAdminController` | 帳單 |
| Controller | `CardPaymentController`, `LinePayController` | 繳款與 LinePay |
| Service | `CardAppService`, `CardReviewService`, `CardTxnService`, `BillService`, `CardBillPDFService`, `CardPaymentService` | 申請、審核、交易、帳單、PDF、繳款 |
| Mapper | `creditcard/mapper/*` | MapStruct DTO 轉換 |
| Entity | `CreditCard`, `CardType`, `CardTransaction`, `CardBill`, `CardApplication`, `CardApplicationItem`, `CardAccount`, `Merchant` | 信用卡資料模型 |

核心學習點：這個模組最適合看 MapStruct、Specification 查詢、PDF 帳單、前後台列表。

### 7.6 Risk

| 分類 | 檔案 | 用途 |
|---|---|---|
| Controller | `BlackListController`, `CreditScoreController`, `ReviewTaskController`, `RiskCheckController`, `RiskEventController`, `RiskReviewController` | 黑名單、信用資料、審查任務、事件、審核 |
| Service | `BlackListService`, `CreditScoreService`, `ReviewTaskService`, `RiskCheckService`, `RiskEventService`, `RiskReviewService`, `CallbackService` | 風控業務 |
| Entity | `Blacklist`, `CustomerCreditInfo`, `ReviewTask`, `RiskEventLog` | 黑名單、信用資訊、審核任務、事件紀錄 |
| Repository | `JpaSpecificationExecutor` 類 repository | 動態查詢 |
| Utils | `CreditMockUtils`, `MaskSensitiveUtils` | mock 與遮罩 |

核心學習點：風控適合看分頁查詢、事件紀錄、審核任務、enum 分類。

### 7.7 Notification

| 分類 | 檔案 | 用途 |
|---|---|---|
| Controller | `NotificationController.java` | 客戶查通知、已讀、未讀數 |
| Controller | `NotificationPreferenceController.java` | 通知偏好 |
| Service | `NotificationServiceImpl.java` | 建立與查詢通知 |
| Service | `NotificationPreferenceServiceImpl.java` | 預設偏好與安全類不可關閉 |
| Entity | `Notification`, `NotificationPreference` | 站內通知與偏好 |
| Enum | `NotificationType` | 通知類型 |

核心學習點：通知模組是「跨模組事件落點」，帳戶、貸款、信用卡、登入安全都會呼叫它。

## 8. 前端整理

### 8.1 Layout 與路由

| 檔案 | 用途 |
|---|---|
| `UserLayout.vue` | 客戶端主版面，含通知鈴鐺、導覽、登入狀態 |
| `AdminLayout.vue` | 管理端主版面，含 sidebar、登入員工、權限導覽 |
| `router/index.js` | route table、登入守衛、角色守衛 |

### 8.2 Store

| Store | 用途 |
|---|---|
| `auth.js` | 管理端員工 session 狀態 |
| `customerAuth.js` | 客戶端 JWT 狀態 |
| `counter.js` | 範例 / 非核心 |

### 8.3 API 檔

| API 檔 | 對應功能 |
|---|---|
| `auth.js` | 管理端登入、員工、日誌 |
| `customerAuth.js` | 客戶登入、註冊、個資、安全 |
| `customer.js` | 管理端客戶 |
| `account.js` | 管理端帳戶 / 交易 |
| `customerAccount.js` | 客戶帳戶、轉帳、電子存摺 |
| `accountApplication.js` | 開戶申請 |
| `favoriteAccount.js` | 常用帳號 |
| `scheduledTransfer.js` | 預約轉帳 |
| `notification.js` | 站內通知 |
| `notificationPreference.js` | 通知偏好 |
| `card*.js`, `userCard*.js` | 信用卡前後台 API |
| `axios.js` | 共用 instance 與攔截器 |

### 8.4 畫面分類

| 類別 | 代表畫面 |
|---|---|
| 客戶登入 / 註冊 | `UserLoginView`, `RegisterView`, `ResetPasswordView` |
| 客戶首頁 / 個資 | `UserHomeView`, `UserProfileView` |
| 客戶帳戶 | `UserAccountsView`, `EPassbookView`, `UserTransactionsView` |
| 客戶交易 | `TransferView`, `ExchangeView`, `ScheduledTransferView`, `FavoriteAccountsView` |
| 客戶安全 | `SecurityLoginRecordsView`, `SecurityDevicesView`, `SecurityPasswordSettingsView` |
| 客戶貸款 | `LoanApplyView`, `LoanStatusView`, `LoanAccountView`, `LoanRepaymentView` |
| 客戶信用卡 | `CardTypeListView`, `CardApplicationForm`, `CardTxnView`, `CardBillView`, `CardView` |
| 管理端帳戶 | `AccountListView`, `AccountApplicationListView`, `TransLogView` |
| 管理端貸款 | `LoanApplicationView`, `LoanAccountAdminView`, loan modals |
| 管理端信用卡 | `CardApplicationList`, `CardApplicationDetailView`, `CardBillView`, `CardTxnView`, `CardView` |
| 管理端風控 | `BlackListView`, `RiskEventView`, `ReviewTask`, `CreditList`, `CreditDetail` |
| 管理端人員 / 顧客 | `EmployeeListView`, `EmployeeCreateView`, `CustomerListView`, `CustomerCreateView` |
| 管理端系統 | `SystemLogView`, `AdminHomeView` |

## 9. 資料庫腳本

| 檔案 | 用途 |
|---|---|
| `auth_init.sql`, `auth_insert.sql`, `auth_drop.sql` | 管理端 Auth |
| `customer_init.sql`, `customer_insert.sql`, `customer_drop.sql` | 客戶資料與客戶登入 |
| `account_init.sql`, `account_mockdata.sql` | 帳戶與交易 |
| `loan_init.sql`, `loan_mockdata.sql`, `loan_document_batch_migration.sql` | 貸款 |
| `card_init.sql`, `card_mockdata.sql` | 信用卡 |
| `risk_init.sql`, `risk_mockdata.sql` | 風控 |
| `ddl_PendingTransfer.sql` | 預約 / pending transfer 補充 |

建議閱讀 SQL 時，先看各模組 `*_init.sql`，再看 mock data。不要一開始只看 Entity，因為實際 demo 資料與部分固定業務帳號會寫在 SQL 裡。

## 10. 重要運行流程

### 10.1 客戶登入

1. `UserLoginView.vue` 呼叫 `customerAuth.js`。
2. 後端進 `CustomerAuthController.login`。
3. `CustomerAuthServiceImpl` 驗證帳密。
4. `JwtUtil` 發 token。
5. 前端存入 `customer_token`。
6. 後續 request 由 `axios.js` 自動帶 token。

### 10.2 管理端登入

1. `AdminLoginView.vue` 呼叫 `auth.js`。
2. 後端進 `AuthController.login`。
3. Spring Security 建 session。
4. 前端存 `auth_user`。
5. 路由守衛進 `/admin/**` 時打 `/api/auth/me`。

### 10.3 開戶申請

1. 客戶在 `AccountApplicationView.vue` 填表與上傳證件。
2. 後端 `AccountApplicationController` 接收 multipart。
3. `AccountApplicationService.submit` 建立申請。
4. 管理端在 `AccountApplicationListView.vue` 審核。
5. 核准後建立 `Account`。
6. 同步開戶申請資料到 `CustomerProfile`。

### 10.4 轉帳

1. 客戶在 `TransferView.vue` 發起轉帳。
2. 後端 `TransferController` 呼叫 `TransferService.transfer`。
3. 驗證帳戶、餘額、幣別、行內 / 跨行規則。
4. 寫入 `TransLog`，同一筆交易以 `referenceId` 串起。
5. 通知模組建立轉帳通知。

### 10.5 貸款

1. 客戶申請。
2. 行員聯繫與二次填單。
3. 送風控。
4. 風控 callback。
5. 核准後帳戶整合建帳撥款。
6. 客戶還款。
7. 排程掃逾期與提醒。

### 10.6 信用卡

1. 客戶申請信用卡。
2. 管理端審核申請與項目。
3. 核准後發卡。
4. 客戶刷卡產生交易。
5. 帳單產生與 PDF 寄送。
6. 客戶繳款或帳務整合處理結算。

### 10.7 風控

1. 業務模組送出風控 request。
2. 風控查信用資料、黑名單或事件。
3. 建立審核任務或事件紀錄。
4. 審核完成後 callback 業務模組。

### 10.8 通知

1. 業務模組發生事件。
2. 呼叫 `NotificationService` 建站內通知。
3. 若需要 email，呼叫 `EmailService`。
4. 前端 `UserLayout.vue` 顯示未讀數與通知列表。

## 11. 技術使用方法與學習時機

| 技術 | 何時學 | 先看哪裡 |
|---|---|---|
| Spring Boot | 想知道專案怎麼啟動 | `JavaEasyBankApplication.java` |
| Spring MVC | 想知道 API 怎麼接 request / response | 任一 controller |
| Spring Security | 想知道權限、登入、角色 | `SecurityConfig.java`, `JwtAuthenticationFilter.java` |
| JPA | 想知道資料表如何變物件 | Entity + Repository |
| `@Query` / Specification | 想學複雜查詢 | `TransLogRepository`, `CardTxnSpecification`, `CardBillSpecification` |
| `@Transactional` | 想學多表一致性 | `AccountIntegrationService`, `LoanApplicationService` |
| `RestTemplate` | 想學後端打後端 API | `LoanRiskClient`, `TransferRiskClient`, `LoginRiskClient` |
| Multipart | 想學上傳檔案 | `AccountApplicationController`, `LoanDocumentController` |
| Thymeleaf + Mail | 想學寄 HTML 信 | `EmailService`, `templates/mail` |
| PDF | 想學輸出文件 | `PassbookPdfService`, `LoanContractPdfService`, `CardBillPDFService` |
| MapStruct | 想學 DTO 轉換 | `creditcard/mapper` |
| Vue Composition API | 想學前端狀態與畫面互動 | 任一 `.vue` 的 `<script setup>` |
| Pinia | 想學登入狀態共用 | `stores/auth.js`, `stores/customerAuth.js` |
| Axios interceptor | 想學 token 與錯誤攔截 | `frontend/src/api/axios.js` |
| Ant Design Vue | 想學後台 UI 表格 / modal | `admin/*.vue` |
| Vitest / JUnit / Mockito | 想學測試 | `src/test/java`, `customerAuth.spec.js` |

## 12. 後續閱讀路線

| 目標 | 建議順序 |
|---|---|
| 看懂整體架構 | `pom.xml` -> `JavaEasyBankApplication` -> `SecurityConfig` -> `frontend/src/main.js` -> `router/index.js` |
| 看懂登入 | `AuthController` / `CustomerAuthController` -> `JwtUtil` -> `JwtAuthenticationFilter` -> frontend stores |
| 看懂帳戶 | `AccountController` -> `AccountService` -> `AccountRepository` -> `Account` |
| 看懂交易 | `TransferController` -> `TransferService` -> `TransLogRepository` -> `TransLog` |
| 看懂貸款 | `LoanApplicationController` -> `LoanApplicationService` -> `LoanRiskClient` -> `AccountIntegrationService` |
| 看懂信用卡 | `CardApplicationController` -> `CardAppService` -> `CardReviewService` -> `CardTxnService` -> `BillService` |
| 看懂風控 | `RiskReviewController` -> `RiskReviewService` -> `ReviewTaskService` -> `RiskEventService` |
| 看懂前端 | `main.js` -> `router/index.js` -> `api/axios.js` -> 對應 view |

## 13. origin/dev 注意事項

以下是根據 `origin/dev` 既有文件與程式結構整理的閱讀提醒：

1. `SessionUtil` 偏舊式 session 工具，現在主要看 Spring Security 與 JWT。
2. 部分 API 直接回傳 Spring Data `Page`，部分包成 `PageResponse`，前端解包時要注意格式。
3. 信用卡 API 有些路徑不是 `/api` 前綴，閱讀 axios 與 controller 時要特別看 base URL。
4. `SecurityConfig` 中部分 seed / 開發 API 是否開放，要依正式環境再檢查。
5. JWT secret 有預設值，正式部署應以環境設定覆蓋。
6. 預約轉帳與部分風控 handler 在既有文件中被標成後續可補強區，學習時可以先理解設計，再確認是否已落地。
7. 文件與程式可能會因分支差異而不同；若你正在 `feature-loan-V2` 工作，貸款細節請看同目錄的 `feature-loan-v2-loan-module-study.md`。

