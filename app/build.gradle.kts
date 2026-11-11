plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.konekin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.konekin"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // Base URL of the REST API used by ApiClient.
        // 10.0.2.2 is the emulator's address for the computer running backend/ (npm start).
        // Original public API: https://dummy.restapiexample.com/api/v1/
        buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:3000/api/v1/\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.6.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
}
