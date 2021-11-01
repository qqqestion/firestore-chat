plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "ru.tashkent.messenger"
        minSdk = 21
        targetSdk = 31
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    kapt {
        generateStubs = true
    }
}

dependencies {

    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(Libs.androidxCore)
    implementation(Libs.androidxAppCompat)
    implementation(Libs.googleMaterial)
    implementation(Libs.androidxConstraintLayout)
    implementation(Libs.androidxSwipeRefreshLayout)
    implementation(Libs.firebaseFirestore)
    implementation(Libs.coroutines)

    implementation(Libs.navigationFragment)
    implementation(Libs.navigationUi)

    implementation(Libs.lifecycleLivedata)
    implementation(Libs.lifecycleViewModel)
    implementation(Libs.lifecycle)
    implementation(Libs.lifecycleCommon)

    implementation(Libs.squareLogcat)

//    kapt(Libs.daggerAndroidProcessor)
//    implementation(Libs.daggerAndroid)
//    implementation(Libs.daggerAndroidSupport)
    implementation(Libs.dagger)
    kapt(Libs.daggerCompiler)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.AndroidTest.androidJunit)
    androidTestImplementation(Libs.AndroidTest.espresso)
}