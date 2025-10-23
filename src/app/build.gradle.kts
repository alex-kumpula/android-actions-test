// Javadoc task for Android sources
import org.gradle.external.javadoc.JavadocMemberLevel


tasks.register<Jar>("generateReleaseJavadoc") {
    group = "documentation"
    description = "Generates Javadoc for release sources."
    val javadoc = tasks.register<Javadoc>("javadocReleaseSources") {
        source = fileTree("src/main/java")
        classpath += files(android.bootClasspath)
        options.encoding = "UTF-8"
        options.memberLevel = JavadocMemberLevel.PUBLIC
        options.isAuthor = true
        options.isVersion = true
        options.links("https://docs.oracle.com/en/java/javase/11/docs/api/")
    }
    from(javadoc.get().destinationDir)
    dependsOn(javadoc)
    archiveClassifier.set("javadoc")
    destinationDirectory.set(file("build/docs/javadoc"))
}

plugins {
    alias(libs.plugins.android.application)
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