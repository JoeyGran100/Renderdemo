plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlinx-serialization")
    id ("com.google.dagger.hilt.android")
    id ("kotlin-kapt")
    id("androidx.room")
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.example.wingsdatingapp"
    compileSdk = 34
    room {
        schemaDirectory("$projectDir/schemas")
    }
    defaultConfig {
        applicationId = "com.example.wingsdatingapp"
        minSdk = 24
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
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
//    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Ktor Dependency
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.serialization)
    implementation (libs.ktor.client.websockets)
    implementation (libs.ktor.client.cio)
//    implementation("io.ktor:ktor-client-logging:2.3.11")
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
//    implementation ("io.ktor:ktor-client-json:2.3.11")
    implementation (libs.kotlinx.serialization.json)
    implementation (libs.ktor.client.logging.jvm)

    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    //Navigation
    implementation (libs.androidx.navigation.compose)
    implementation (libs.otpview)
    implementation (libs.material3)
    implementation (libs.androidx.material.icons.extended)
    implementation (libs.lottie.compose)

    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)

    kapt("androidx.room:room-compiler:2.6.1")

    implementation (libs.gson)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.socket.io.client)
    implementation (libs.coil.compose)
    implementation (libs.play.services.auth)
}
kapt {
    correctErrorTypes = true
}