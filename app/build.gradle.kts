plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlin-android")
}

androidExtensions {
    features = setOf("parcelize")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")

    defaultConfig {
        applicationId = "com.logicgames.life"
        minSdkVersion(23)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.1")
    val fileTree = fileTree(file("libs")) {
        include("*.jar")
    }
    implementation(fileTree)
    implementation(project(":game"))

    implementation(Dependencies.Kotlin.StandardLibrary)
    implementation(Dependencies.Kotlin.CoroutinesAndroid)
    implementation(Dependencies.AndroidX.AppCompat)
    implementation(Dependencies.AndroidX.CoreKtx)
    implementation(Dependencies.AndroidX.ActivityKtx)
    implementation(Dependencies.AndroidX.ConstraintLayout)
    implementation(Dependencies.AndroidX.CoordinatorLayout)
    implementation(Dependencies.AndroidX.Lifecycle.ViewModelKtx)
    implementation(Dependencies.AndroidX.Lifecycle.LiveDataKtx)
    implementation(Dependencies.MaterialDesign)
    implementation(Dependencies.ColorPicker)
    implementation(Dependencies.FB)

    testImplementation(Dependencies.Test.JUnit)
    androidTestImplementation(Dependencies.Test.Android.JUnit)
    androidTestImplementation(Dependencies.Test.Android.Espresso)
}
