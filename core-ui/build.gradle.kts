plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.erendogan6.planmyworkout.coreui"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
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

    buildFeatures {
        viewBinding = true
    }

    lint {
        abortOnError = false
    }
}

dependencies {
    // Core module
    implementation(project(":core"))

    // AndroidX Core Libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    // Lottie Animation
    implementation(libs.lottie)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    annotationProcessor(libs.hilt.compiler)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
