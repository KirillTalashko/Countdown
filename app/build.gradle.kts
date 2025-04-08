plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.countdown"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.countdown"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    kapt {
        correctErrorTypes = true
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
// Базовые зависимости Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Для фонового сервиса таймера
    implementation(libs.androidx.lifecycle.service) // LifecycleService

    // Для управления фоновыми задачами
    implementation(libs.androidx.work.runtime.ktx) // WorkManager

    // Для уведомлений
    implementation(libs.androidx.core) // NotificationCompat

    // Для коммуникации между компонентами
    implementation(libs.androidx.localbroadcastmanager) // LocalBroadcastManager

    // Для запроса разрешений в Compose
    implementation(libs.accompanist.permissions)
    implementation("androidx.compose.animation:animation:1.4.0")

    // Для звука уведомлений
    implementation(libs.androidx.media)

    // Для EncryptedSharedPreferences
    implementation(libs.androidx.security.crypto)
    // Для работы с корутинами
    implementation(libs.kotlinx.coroutines.android)


    // Тестирование
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}
