#!/bin/bash

echo "========================================================"
echo "🎯 بدء عملية الأتمتة وتحديث المستودع محلياً بالكامل..."
echo "========================================================"

# 1. إنشاء الهيكل البرمجي والمجلدات الفرعية
echo "📁 [1/5] فحص وبناء المجلدات الأساسية..."
mkdir -p app/src/main
mkdir -p backend

# 2. إنشاء وتحديث ملف الـ AndroidManifest.xml
echo "🤖 [2/5] تحديث ملف الأذpermissions وصلاحيات الأندرويد..."
cat << 'EOF' > app/src/main/AndroidManifest.xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.jhad1234.aiassistant">
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
    <application android:allowBackup="true" android:label="AI OS Hub" android:theme="@style/Theme.AppCompat.NoActionBar">
        <activity android:name=".MainActivity" android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <service android:name=".services.AIExecutionService" android:enabled="true" android:exported="false" android:foregroundServiceType="specialUse" />
    </application>
</manifest>
EOF

# 3. إنشاء ملفات خادم الـ Backend والإعدادات البيئية
echo "⚙️ [3/5] حقن ملفات FastAPI والإعدادات المحلية لبايثون..."
cat << 'EOF' > backend/config.py
import os
from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    HOST: str = os.getenv("HOST", "127.0.0.1")
    PORT: int = int(os.getenv("PORT", 8000))
    OLLAMA_BASE_URL: str = os.getenv("OLLAMA_BASE_URL", "http://127.0.0.1:11434")
    MODEL_NAME: str = os.getenv("MODEL_NAME", "llama3")
    SSH_TUNNEL_URL: str = os.getenv("SSH_TUNNEL_URL", "ssh.localhost.run")
    LOCAL_TUNNEL_SUBDOMAIN: str = os.getenv("LOCAL_TUNNEL_SUBDOMAIN", "ai-os-hub")

    class Config:
        env_file = ".env"

settings = Settings()
EOF

cat << 'EOF' > backend/.env.example
HOST=127.0.0.1
PORT=8000
OLLAMA_BASE_URL=http://127.0.0.1:11434
MODEL_NAME=llama3
LOCAL_TUNNEL_SUBDOMAIN=ai-os-jhad
EOF

# 4. إعادة صياغة ملف الـ README.md وتثبيت الروابط في القاع
echo "📝 [4/5] إعادة صياغة الـ README ونقل الروابط الحيوية لأسفل الصفحة..."
cat << 'EOF' > README.md
# AI Android Dev System & Self-Evolving OS

An autonomous, self-evolving AI Assistant and OS environment engineered to run locally on Android devices utilizing Termux, Python FastAPI, Node.js services, and local LLMs via Ollama.

## 🚀 Key Features
* **Local Intelligence:** Fully integrated with Ollama for offline, secure text generation and code processing.
* **Automated CI/CD Workflow:** Connected to GitHub Actions for automatic APK compiling and release deployments.
* **Network & Tunnel Management:** Built-in support for establishing persistent SSH tunnels using localtunnel and ssh.localhost.run.
* **Process Persistence:** Background automation handling through Termux using custom foreground Android services and process managers.

## 🛠️ Architecture & Requirements
* **Frontend:** Android Native application (Kotlin / Gradle framework).
* **Backend Runtime:** Python FastAPI & Node.js (v25.8.2+ executed inside Termux environment).
* **AI Engine:** Ollama running localized models (e.g., Llama3, Phi3).
* **Monitoring Tool:** Fing Agent setup for local network infrastructure monitoring.

## 📦 System Configuration & Deployment
1. Clone the repository to your environment.
2. Configure your environment variables by copying `.env.example` to `.env`.
3. Boot up the local FastAPI service and launch your Termux SSH tunnel.
4. Build the development environment using Gradle or let GitHub Actions compile the build automatically.

---

## 📥 Download Links & Installation Artifacts
For quick access to the latest deployed software versions, use the production builds provided below:

* **Latest Release Bundle:** [Download AI OS Interface Assets v1.1.11](https://github.com/jhad1234/AI-Android-Dev-System/releases/download/v1.1.11/ai-os-v1.0.apk)
* **Production APK Build:** [Direct APK Download Link](https://github.com/jhad1234/AI-Android-Dev-System/raw/main/artifacts/ai-os-v1.0.apk)
EOF

# 5. تنظيف المستودعات الفرعية المخفية وجدولة البيانات في Git
echo "🧹 [5/5] حظر تداخل المجلدات الفرعية وتسجيل التحديثات في مستودع Git المحلي..."
find . -mindepth 2 -name ".git" -type d -exec rm -rf {} + 2>/dev/null

git add app/src/main/AndroidManifest.xml backend/config.py backend/.env.example README.md

# تفقد التغييرات للـ Commit التلقائي واستهداف الفرع main
if git diff-index --quiet HEAD --; then
    echo "ℹ️ لا توجد تغييرات معلقة، المستودع متزامن محلياً بالفعل."
else
    git commit -m "Fix: Clean local repository architecture, fix permissions, and organize markdown layout"
    echo "💾 تم تسجيل الـ Commit محلياً بنجاح."
fi

# محاولة فحص فرع main لضمان المطابقة
git branch -m master main 2>/dev/null || git branch -m main 2>/dev/null

echo "--------------------------------------------------------"
echo "✨ تم التحديث والهيكلة بنجاح! السكربت جاهز الآن للـ Push فور استعادة الحساب."
echo "========================================================"
