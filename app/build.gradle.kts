plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.android.calendarapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.calendarapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "OAUTH_CLIENT_ID", "\"mdmWy7o7PTQcmb5yqLWL\"")
            buildConfigField("String", "OAUTH_CLIENT_SECRET", "\"2SAy2HAIOo\"")
            buildConfigField("String", "OAUTH_CLIENT_NAME", "\"Simple Calander\"")

            isDebuggable = false
        }

        debug {
            buildConfigField("String", "OAUTH_CLIENT_ID", "\"mdmWy7o7PTQcmb5yqLWL\"")
            buildConfigField("String", "OAUTH_CLIENT_SECRET", "\"2SAy2HAIOo\"")
            buildConfigField("String", "OAUTH_CLIENT_NAME", "\"Simple Calander\"")

            isDebuggable = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // hilt
    implementation(libs.com.google.dagger.hilt)
    kapt(libs.com.google.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.compose)

    // liveData
    implementation(libs.androidx.compose.runtime.livedata)

    // room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // naver login
    implementation(libs.com.navercorp.nid.oauth)

    // encryptedSharedPreferences
    implementation(libs.androidx.security.crypto)

    // tink
    implementation(libs.com.google.crypto.tink)

    // viewPager
    implementation(libs.com.google.accompanist)

    // retrofit
    implementation(libs.com.squareup.retrofit)
    implementation(libs.com.squareup.retrofit.converter)

    // runtime-compose
    implementation(libs.androidx.lifecycle.runtime.compose)
}