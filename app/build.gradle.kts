plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

// Load version properties
val versionPropertiesFile = rootProject.file("version.properties")
val versionProperties = java.util.Properties()
if (versionPropertiesFile.exists()) {
    versionProperties.load(java.io.FileInputStream(versionPropertiesFile))
}

// Generate semantic version
val versionMajor = versionProperties["VERSION_MAJOR"]?.toString()?.toIntOrNull() ?: 0
val versionMinor = versionProperties["VERSION_MINOR"]?.toString()?.toIntOrNull() ?: 1 
val versionPatch = versionProperties["VERSION_PATCH"]?.toString()?.toIntOrNull() ?: 0
val semanticVersionName = "$versionMajor.$versionMinor.$versionPatch"

// Generate version code from semantic version (formula: major * 10000 + minor * 100 + patch)
val generatedVersionCode = versionMajor * 10000 + versionMinor * 100 + versionPatch

android {
    namespace = "com.example.myapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 36
        versionCode = generatedVersionCode
        versionName = semanticVersionName

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
}