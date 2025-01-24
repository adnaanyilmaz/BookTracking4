plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
    id("androidx.navigation.safeargs")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.booktracking4"
    compileSdk = 34
    buildFeatures { viewBinding = true }

    defaultConfig {
        applicationId = "com.example.booktracking4"
        minSdk = 24
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //picasso
    implementation("com.squareup.picasso:picasso:2.71828")


    // Hilt
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.converter.gson)

    //Room
    implementation ("androidx.room:room-runtime:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    ksp ("androidx.room:room-compiler:2.6.1")

    // Firebase - Firebase Auth - Firestore
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation ("com.google.android.gms:play-services-auth:21.2.0")


    // Coil (Picassodan daha iyi çalışıyor bence. Bunu daha pratik kullanmakiçin bir extension yazdım)
    implementation(libs.coil)

// Firebase Firestore
    implementation(libs.firebase.firestore)
}

