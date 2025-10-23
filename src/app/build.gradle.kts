plugins {
    alias(libs.plugins.android.application)
}

import org.gradle.external.javadoc.JavadocMemberLevel
import com.android.build.gradle.api.ApplicationVariant

// Generate Javadoc for all variants (Kotlin DSL)
android.applicationVariants.all {
    val variant = this
    val capitalized = variant.name.replaceFirstChar { it.uppercaseChar() }
    tasks.register<Javadoc>("generate${capitalized}Javadoc") {
        group = "documentation"
        description = "Generate ${variant.name} Javadoc"
        source = variant.javaCompileProvider.get().source
        destinationDir = file("$rootDir/doc/javadoc/")
        exclude("**/BuildConfig.java")
        doFirst {
            val androidJar = "${android.sdkDirectory}/platforms/${android.compileSdkVersion}/android.jar"
            classpath = files(variant.javaCompileProvider.get().classpath) + files(androidJar)
            (options as org.gradle.external.javadoc.StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
            options.encoding = "UTF-8"
            options.memberLevel = JavadocMemberLevel.PUBLIC
            options.addStringOption("subpackages", "com.example.demoapp")
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