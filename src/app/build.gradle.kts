plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.dokka") version "1.9.20"
}

dokkaHtml {
    outputDirectory.set(file("src/doc/javadoc"))
    
    moduleName.set("DemoApp")
    moduleVersion.set(android.defaultConfig.versionName)
    
    dokkaSourceSets {
        named("main") {
            includeNonPublic.set(false)
            skipDeprecated.set(true)
            reportUndocumented.set(false)
            jdkVersion.set(11)
            
            // Only include your package
            perPackageOption {
                matchingRegex.set("^(?!com\\.example\\.demoapp).*")
                suppress.set(true)
            }
            
            // External documentation links
            externalDocumentationLink {
                url.set(uri("https://developer.android.com/reference/").toURL())
            }
            
            // Source links
            sourceLink {
                localDirectory.set(file("src/main/java"))
                remoteUrl.set(uri("https://github.com/alex-kumpula/android-actions-test/blob/main/src/main/java").toURL())
                remoteLineSuffix.set("#L")
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
    dokkaPlugin("org.jetbrains.dokka:android-documentation-plugin:2.1.0")
}