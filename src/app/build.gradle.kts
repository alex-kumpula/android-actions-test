plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.dokka") version "1.9.20"
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    // Use absolute path from project root
    outputDirectory = file("${project.rootProject.projectDir}/doc/javadoc")
    
    dokkaSourceSets {
        configureEach {
            includeNonPublic.set(true)
            skipDeprecated.set(true)
            jdkVersion.set(11)
            
            perPackageOption {
                matchingRegex.set("com\\.example\\.demoapp.*")
                suppress.set(false)
            }
            perPackageOption {
                matchingRegex.set(".*")
                suppress.set(true)
            }
        }
    }
}

android {
    namespace = "com.example.demoapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.demoapp"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // dokkaPlugin("org.jetbrains.dokka:android-documentation-plugin:2.1.0")
}