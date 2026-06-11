# 🏗️ معمارية النظام الشاملة

## نظرة عامة
**AI-Android-Dev-System** هو نظام تطوير ذاتي التطور يعمل على Android مع نواة ذكية وشبكة وكلاء متعددة الاختصاصات.

---

## 1️⃣ المستوى الأول: الطبقات الرئيسية

```
┌─────────────────────────────────────┐
│     Presentation Layer (UI)         │  Jetpack Compose
├─────────────────────────────────────┤
│     Business Logic Layer            │  Core + Agents + Bus
├─────────────────────────────────────┤
│     Data/Repository Layer           │  Database + APIs
├─────────────────────────────────────┤
│     System Services Layer           │  Services + Loaders
└─────────────────────────────────────┘
```

---

## 2️⃣ الطبقة الأولى: العرض (Presentation)

### Jetpack Compose
```kotlin
// ✅ استخدام Compose بدلاً من XML
// ✅ MVVM مع StateFlow للحالة
// ✅ Navigation Compose للتنقل
```

### الشاشات الرئيسية:
1. **MainConsoleScreen** - لوحة التحكم الرئيسية
2. **AgentDashboardScreen** - لوحة الوكلاء
3. **SystemLogsScreen** - سجل النظام
4. **SettingsScreen** - الإعدادات

---

## 2️⃣ الطبقة الثانية: منطق الأعمال

### الأعمدة الأساسية:

#### A. KernelCore (النواة الذكية)
```
المسؤوليات:
├─ تحليل الأوامر الطبيعية
├─ تقسيم المهام المعقدة
├─ تنسيق عمل الوكلاء
└─ إدارة الموارد
```

#### B. AIGovernor (نظام الحوكمة)
```
المسؤوليات:
├─ التحقق من الأوامر قبل التنفيذ
├─ فرض سياسات الأمان
├─ منع التصرفات الخطيرة
└─ تسجيل جميع العمليات (Audit Logging)
```

#### C. TaskOrchestrator (منسق المهام)
```
المسؤوليات:
├─ تحويل الأوامر إلى مهام
├─ توزيع المهام على الوكلاء
├─ تتبع تقدم المهام
└─ معالجة الأخطاء والإعادة
```

#### D. MessageBus (حافلة الرسائل)
```
النمط: Pub/Sub Reactive
├─ EventPublisher: نشر الأحداث
├─ EventSubscriber: الاشتراك بالأحداث
├─ EventHandler: معالجة الأحداث
└─ Replay Strategy: إعادة تشغيل
```

### نظام الوكلاء:
```
BaseAgent (الواجهة الأساسية)
├── CoderAgent (البرمجة)
├── AnalyzerAgent (التحليل)
├── ExecutorAgent (التنفيذ)
└── MonitorAgent (المراقبة)
```

---

## 3️⃣ الطبقة الثالثة: البيانات

### Room Database
```kotlin
// Tables:
├── commands    // سجل الأوامر
├── logs        // سجل النظام
├── agents      // حالة الوكلاء
└── cache       // الذاكرة المؤقتة
```

### Remote APIs
```
├── OllamaClient     // نماذج LLM
├── BackendClient    // الخادم الخلفي
└── SSHTunnelClient  // الأنفاق الآمنة
```

### Cache Management
```
├── Memory Cache     // ذاكرة الوصول السريع
├── Disk Cache       // قرص التخزين
└── LRU Strategy     // استراتيجية الحذف
```

---

## 4️⃣ الطبقة الرابعة: الخدمات

### WatchdogService
```
المسؤوليات:
├─ مراقبة استقرار التطبيق
├─ كشف الأعطال والأخطاء
├─ تنفيذ Rollback تلقائي
└─ إعادة التشغيل الذاتي
```

### DynamicDexLoader
```
المميزات:
├─ Hot Patching بدون إعادة تثبيت
├─ حقن الميزات الجديدة
├─ عزل الأخطاء
└─ Rollback آلي
```

### BackendService
```
المهام:
├─ تكامل FastAPI
├─ إدارة العمليات
├─ معالجة المهام المعقدة
└─ مزامنة البيانات
```

### NetworkService
```
المميزات:
├─ إدارة الاتصالات
├─ SSH Tunneling
├─ Fallback Strategy
└─ Connection Pool
```

---

## 5️⃣ تدفق البيانات الكامل

```
┌──────────────────┐
│  User Input      │
└────────┬─────────┘
         ↓
┌──────────────────────┐
│ CommandProcessor     │  معالجة الإدخال
└────────┬─────────────┘
         ↓
┌──────────────────────┐
│ AIGovernor Validation│  التحقق الأمني
└────────┬─────────────┘
         ↓
┌──────────────────────┐
│ KernelCore Analysis  │  تحليل المهام
└────────┬─────────────┘
         ↓
┌──────────────────────┐
│ TaskOrchestrator     │  توزيع المهام
└────────┬─────────────┘
         ↓
┌────────────────────────────────────┐
│  Publish to MessageBus             │  نشر الأحداث
└────────┬─────────────────────────────┘
         ↓
┌────────────────────────────────────┐
│  Agents Execution (Parallel)       │  تنفيذ متوازي
│  ├─ CoderAgent                    │
│  ├─ AnalyzerAgent                 │
│  ├─ ExecutorAgent                 │
│  └─ MonitorAgent                  │
└────────┬─────────────────────────────┘
         ↓
┌──────────────────────┐
│ MessageBus Publish   │  نشر النتائج
└────────┬─────────────┘
         ↓
┌──────────────────────┐
│ Repository Save      │  حفظ البيانات
└────────┬─────────────┘
         ↓
┌──────────────────────┐
│ ViewModel Update     │  تحديث الحالة
└────────┬─────────────┘
         ↓
┌──────────────────────┐
│ UI Recomposition     │  تحديث الواجهة
└──────────────────────┘
```

---

## 🔐 طبقات الأمان

### 1. Input Validation
```kotlin
- التحقق من نوع الإدخال
- تطهير البيانات من الحقن
- التحقق من الحجم والصيغة
```

### 2. Policy Enforcement
```kotlin
- تطبيق سياسات الأمان
- التحقق من الأذونات
- تسجيل العمليات
```

### 3. Sandboxing
```kotlin
- عزل الوكلاء عن بعضها
- تحديد الموارد المتاحة
- منع الوصول غير المصرح
```

### 4. Audit Logging
```kotlin
- تسجيل جميع العمليات
- تتبع الأخطاء
- الكشف عن الأنشطة المريبة
```

---

## 📊 توزيع الموارد

| المكون | CPU | RAM | Storage |
|--------|-----|-----|---------|
| KernelCore | 5% | 20MB | - |
| MessageBus | 2% | 10MB | - |
| Agents (4x) | 30% | 100MB | 50MB |
| Services | 10% | 30MB | - |
| UI | 15% | 40MB | - |
| Cache | - | 50MB | 200MB |
| Database | 5% | 15MB | 500MB+ |
| Reserve | - | 100MB | - |

---

## 🔄 دورة حياة التطبيق

```
1. Launch
   ↓
2. Initialize Core Systems
   ├─ Kernel Core
   ├─ Message Bus
   ├─ Watchdog Service
   └─ Database Setup
   ↓
3. Load Agents
   ├─ CoderAgent
   ├─ AnalyzerAgent
   ├─ ExecutorAgent
   └─ MonitorAgent
   ↓
4. Connect Backend
   ├─ Ollama Connection
   ├─ FastAPI Integration
   └─ SSH Tunnel (if needed)
   ↓
5. Display UI & Ready
   ├─ Main Console
   ├─ Agent Dashboard
   └─ System Status
   ↓
6. Runtime Operations
   ├─ Process Commands
   ├─ Execute Agents
   ├─ Update Logs
   └─ Monitor Health
```

---

## 🧩 التكامل مع الخدمات الخارجية

### Ollama (نماذج LLM)
```
App ←HTTP/REST→ OllamaServer
                    ↓
                Local LLM Models
                (Llama3, Phi, etc)
```

### FastAPI Backend
```
App ←HTTP/WebSocket→ BackendServer
                        ↓
                Database + Queue
```

### SSH Tunneling
```
App ←SSH Tunnel→ Remote Server
        (localtunnel / ssh.localhost.run)
```

---

## 🎯 مؤشرات الأداء الرئيسية (KPIs)

| المؤشر | الهدف |
|--------|--------|
| Response Time | < 500ms |
| Memory Usage | < 300MB |
| CPU Usage | < 50% |
| Agent Success Rate | > 95% |
| Error Recovery | < 2s |
| Database Queries | < 100ms |

---

**آخر تحديث:** 2026-06-11
