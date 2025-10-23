plugins {
    alias(libs.plugins.android.application)
}

// Javadoc task for Android sources (Kotlin DSL)
import org.gradle.external.javadoc.JavadocMemberLevel

tasks.register<Javadoc>("generateJavadoc") {
    group = "documentation"
    description = "Generates Javadoc for main sources."
    source = fileTree("src/main/java")
    classpath += files(android.bootClasspath)
    options.encoding = "UTF-8"
    options.memberLevel = JavadocMemberLevel.PUBLIC
}

import com.android.build.gradle.api.ApplicationVariant
import org.gradle.external.javadoc.JavadocMemberLevel

android.applicationVariants.all {
    val variant = this
    val capitalized = variant.name.replaceFirstChar { it.uppercaseChar() }
    tasks.register<Javadoc>("generate${capitalized}Javadoc") {
        group = "documentation"
        description = "Generate ${variant.name} Javadoc"
        // Use Java sources only
        source = variant.javaCompileProvider.get().source
        destinationDir = file("$rootDir/doc/javadoc/")
        failOnError = false
        exclude("**/BuildConfig.java")

        doFirst {
            val androidJar = "${android.sdkDirectory}/platforms/${android.compileSdkVersion}/android.jar"
            classpath = files(variant.javaCompileProvider.get().classpath) + files(androidJar)
            (options as org.gradle.external.javadoc.StandardJavadocDocletOptions).addStringOption("-show-members", "package")
            options.encoding = "UTF-8"
            options.memberLevel = JavadocMemberLevel.PUBLIC
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