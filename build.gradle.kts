// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Classpaths.androidGradle)
        classpath(Classpaths.kotlinGradlePlugin)
        classpath(Classpaths.googleServices)
        classpath(Classpaths.navigation)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

//task clean(type: Delete) {
//    delete rootProject.buildDir
//}