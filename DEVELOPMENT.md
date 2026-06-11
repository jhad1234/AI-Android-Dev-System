# 👨‍💻 دليل التطوير الشامل

## 1️⃣ معايير الكود (Code Standards)

### Kotlin Style Guide

#### ✅ **التسميات الصحيحة:**
```kotlin
// Classes - PascalCase
class KernelCore { }
data class CommandEntity { }
sealed class Agent { }

// Functions - camelCase
fun processCommand(input: String): Result<String>
fun validateInput(cmd: String): Boolean
fun executeTask(task: Task): Deferred<String>

// Constants - UPPER_SNAKE_CASE
const val MAX_RETRIES = 3
const val DEFAULT_TIMEOUT = 5000L

// Variables - camelCase
var commandInput = ""
val agentList: List<Agent> = emptyList()
```

#### ✅ **التنسيق:**
```kotlin
// استخدم 4 مسافات للمحاذاة
class MyClass {
    fun myMethod() {
        // تنسيق صحيح
    }
}

// استخدم سطور فارغة لفصل الأقسام
class CompleteClass {
    private val logger = Logger()

    fun method1() { }

    fun method2() { }
}
```

#### ✅ **التوثيق:**
```kotlin
/**
 * معالج الأوامر الرئيسي في النواة المركزية
 * 
 * يقوم بتحليل الأمر المُدخل والتحقق من صحته
 * ثم توزيعه على الوكلاء المناسبين
 * 
 * @param input الأمر النصي من المستخدم
 * @param context سياق التنفيذ
 * @return النتيجة أو الخطأ الذي حدث
 * @throws IllegalArgumentException إذا كان الإدخال غير صحيح
 */
suspend fun processCommand(
    input: String,
    context: ExecutionContext
): Result<String> {
    // التنفيذ
}
```

---

## 2️⃣ بنية الملفات

### تنظيم ملف واحد:
```kotlin
package com.ai.os.core

// الاستيرادات (Imports)
import android.content.Context
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

// الثوابت والمتغيرات العامة
const val TAG = "KernelCore"
private const val LOG_PREFIX = "[KERNEL]"

// الفئة الرئيسية
class KernelCore @Inject constructor(
    private val governor: AIGovernor,
    private val bus: MessageBus
) {
    // المتغيرات الخاصة
    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state

    // الدوال العامة
    suspend fun process(cmd: String): Result<String> {
        // التنفيذ
    }

    // الدوال الخاصة
    private suspend fun validate(cmd: String): Boolean {
        // التحقق
    }

    // الفئات الداخلية
    sealed class State {
        object Idle : State()
        data class Processing(val task: Task) : State()
        data class Complete(val result: String) : State()
    }
}
```

---

## 3️⃣ نمط MVVM

### ViewModel الصحيح:
```kotlin
@HiltViewModel
class ConsoleViewModel @Inject constructor(
    private val kernelCore: KernelCore,
    private val repository: CommandRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // الحالة (State)
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()
    
    private val _logs = MutableStateFlow("")
    val logs: StateFlow<String> = _logs.asStateFlow()
    
    // الأحداث (Events)
    fun executeCommand(cmd: String) {
        viewModelScope.launch {
            _uiState.value = UIState.Processing
            
            try {
                val result = kernelCore.process(cmd)
                _logs.value += "Result: $result\n"
                _uiState.value = UIState.Success
                
                // حفظ الأمر
                repository.saveCommand(cmd, result)
            } catch (e: Exception) {
                _uiState.value = UIState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    // الحالات المحتملة
    sealed class UIState {
        object Loading : UIState()
        object Success : UIState()
        data class Error(val message: String) : UIState()
        object Processing : UIState()
    }
}
```

### Repository:
```kotlin
class CommandRepository @Inject constructor(
    private val commandDao: CommandDao,
    private val apiClient: BackendApiClient
) {
    
    suspend fun saveCommand(cmd: String, result: String) {
        val entity = CommandEntity(
            id = UUID.randomUUID().toString(),
            command = cmd,
            result = result,
            timestamp = System.currentTimeMillis()
        )
        commandDao.insert(entity)
        
        // حفظ على الخادم أيضاً
        apiClient.saveCommand(entity)
    }
    
    suspend fun getCommands(): List<CommandEntity> {
        return commandDao.getAllCommands()
    }
}
```

---

## 4️⃣ الاختبارات

### Unit Tests:
```kotlin
@RunWith(RobolectricTestRunner::class)
class KernelCoreTest {
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    private lateinit var kernelCore: KernelCore
    private val governorMock: AIGovernor = mockk()
    private val busMock: MessageBus = mockk()
    
    @Before
    fun setUp() {
        kernelCore = KernelCore(governorMock, busMock)
    }
    
    @Test
    fun `process command successfully`() = runTest {
        // Arrange
        val input = "hello world"
        coEvery { governorMock.validate(input) } returns true
        
        // Act
        val result = kernelCore.process(input)
        
        // Assert
        assertTrue(result.isSuccess)
        assertEquals("Result...", result.getOrNull())
    }
    
    @Test
    fun `reject invalid command`() = runTest {
        // Arrange
        val input = ""
        coEvery { governorMock.validate(input) } returns false
        
        // Act
        val result = kernelCore.process(input)
        
        // Assert
        assertTrue(result.isFailure)
    }
}
```

### Instrumented Tests:
```kotlin
@RunWith(AndroidJUnit4::class)
class ConsoleUITest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun testCommandInputAndExecution() {
        composeTestRule.setContent {
            MainConsoleScreen()
        }
        
        // اكتب أمر
        composeTestRule
            .onNodeWithTag("command_input")
            .performTextInput("hello")
        
        // اضغط زر التنفيذ
        composeTestRule
            .onNodeWithTag("execute_button")
            .performClick()
        
        // تحقق من النتيجة
        composeTestRule
            .onNodeWithText("Result:")
            .assertIsDisplayed()
    }
}
```

---

## 5️⃣ نمط Dependency Injection

### Module Hilt:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Singleton
    @Provides
    fun provideKernelCore(governor: AIGovernor): KernelCore {
        return KernelCore(governor)
    }
    
    @Singleton
    @Provides
    fun provideMessageBus(): MessageBus {
        return MessageBus()
    }
    
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ai_os_db"
        ).build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://localhost:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Singleton
    @Provides
    fun provideBackendApiClient(retrofit: Retrofit): BackendApiClient {
        return retrofit.create(BackendApiClient::class.java)
    }
}
```

---

## 6️⃣ سير العمل (Git Workflow)

### الفروع:
```
main                           (الإنتاج)
│
└─ develop                     (التطوير الرئيسي)
   │
   ├─ feature/new-agent        (ميزات جديدة)
   ├─ bugfix/fix-bug           (إصلاح أخطاء)
   ├─ refactor/improve-code    (تحسين الكود)
   └─ docs/update-readme       (توثيق)
```

### Commit Messages:
```
feat: إضافة وكيل جديد للبرمجة
fix: إصلاح خطأ في معالج الأوامر
docs: تحديث دليل المعمارية
test: إضافة اختبارات للنواة
refactor: تبسيط منطق AIGovernor
perf: تحسين أداء المخزن المؤقت
chore: تحديث المكتبات
```

### Pull Request:
```markdown
## الوصف
يضيف هذا PR وكيل جديد للتحليل.

## النوع
- [x] Feature
- [ ] Bug Fix
- [ ] Refactoring
- [ ] Documentation

## التغييرات
- إضافة AnalyzerAgent.kt
- تحديث AgentRegistry
- إضافة اختبارات

## الاختبار
- [x] Unit tests
- [x] Integration tests
- [ ] Manual testing

## Checklist
- [x] الكود اتبع معايير المشروع
- [x] تمت إضافة/تحديث الاختبارات
- [x] تمت تحديث التوثيق
- [x] لا توجد رسائل خطأ
```

---

## 7️⃣ أوامر مفيدة

### البناء والاختبار:
```bash
# بناء المشروع
./gradlew clean build

# اختبارات الوحدة
./gradlew test

# اختبارات الأجهزة
./gradlew connectedAndroidTest

# تقرير التغطية
./gradlew jacocoTestReport

# تحليل الكود
./gradlew detekt
./gradlew ktlintCheck

# Lint
./gradlew lint
```

### التطوير:
```bash
# تشغيل التطبيق
./gradlew installDebug

# تنظيف الكود
./gradlew ktlintFormat

# بناء APK للإطلاق
./gradlew assembleRelease

# بناء Android App Bundle
./gradlew bundleRelease
```

---

## 8️⃣ أفضل الممارسات

### ✅ افعل:
```kotlin
// استخدم sealed classes للحالات
sealed class State {
    object Loading : State()
    data class Success(val data: String) : State()
}

// استخدم extension functions
fun String.isValidCommand(): Boolean = this.isNotEmpty()

// استخدم scope functions بحكمة
val entity = CommandEntity().apply {
    command = "hello"
    timestamp = System.currentTimeMillis()
}
```

### ❌ لا تفعل:
```kotlin
// لا تستخدم null
val result: String? = null

// لا تتجاهل الأخطاء
try {
    kernelCore.process(cmd)
} catch (e: Exception) {
    // لا تفعل شيء
}

// لا تستخدم Global State
var globalState = ""

// لا تخلط بين الأنواع
fun execute(cmd: Any) { }
```

---

## 9️⃣ استكشاف الأخطاء الشائعة

### Memory Leak
```kotlin
// ❌ خطأ
viewModel.scope.launch {
    // يمكن تسرب الذاكرة
}

// ✅ صحيح
viewModelScope.launch {
    // آمن - ينظف تلقائياً
}
```

### Race Condition
```kotlin
// ❌ خطر
var counter = 0
launch { counter++ }
launch { counter++ }

// ✅ آمن
val counter = AtomicInteger(0)
launch { counter.incrementAndGet() }
launch { counter.incrementAndGet() }
```

---

## 🔟 أدوات مساعدة

### Android Studio Plugins:
- Kotlin Linter
- Detekt
- Code Coverage
- Test Generator

### Gradle Tasks:
```bash
# عرض جميع المهام
./gradlew tasks

# مهام مخصصة
./gradlew buildDebug
./gradlew testDebug
./gradlew lint
```

---

**آخر تحديث:** 2026-06-11
