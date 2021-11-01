plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(project(":domain"))

    implementation(Libs.androidxCore)
    implementation(Libs.androidxAppCompat)
    implementation(Libs.googleMaterial)
    implementation(Libs.firebaseFirestore)
    implementation(Libs.firebaseAuth)
    implementation(Libs.coroutines)
    implementation(Libs.coroutinesPlayServices)

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