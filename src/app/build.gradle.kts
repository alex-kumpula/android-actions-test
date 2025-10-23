plugins {
    alias(libs.plugins.android.application)
}

import org.gradle.external.javadoc.JavadocMemberLevel
import com.android.build.gradle.api.ApplicationVariant

// Define your application's root package here
val appPackage = "com.example.demoapp"

// Generate Javadoc for all variants (Kotlin DSL)
android.applicationVariants.all {
    val variant = this
    val capitalized = variant.name.replaceFirstChar { it.uppercaseChar() }
    
    // Check if the target SDK is available for release variant
    // This is often needed in kts files for type safety
    if (variant.buildType.name == "release") {
        
        tasks.register<Javadoc>("generate${capitalized}Javadoc") {
            group = "documentation"
            description = "Generate ${variant.name} Javadoc"
            source = variant.javaCompileProvider.get().source
            destinationDir = file("$rootDir/doc/javadoc/")
            exclude("**/BuildConfig.java")
            
            doFirst {
                val androidJar = "${android.sdkDirectory}/platforms/${android.compileSdkVersion}/android.jar"
                classpath = files(variant.javaCompileProvider.get().classpath) + files(androidJar)
    
                val options = (options as org.gradle.external.javadoc.StandardJavadocDocletOptions)
                
                // CRITICAL FIX: Focus documentation only on the specified package and its subpackages.
                options.addStringOption("subpackages", appPackage)
                
                // Optional: Add titles for better documentation clarity
                options.docTitle = "Demo App API Documentation (${variant.name})"
                options.windowTitle = "Demo App Javadoc"
    
                options.addStringOption("Xdoclint:none", "-quiet")
                options.encoding = "UTF-8"
                options.memberLevel = JavadocMemberLevel.PUBLIC
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
}
