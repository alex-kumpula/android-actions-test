plugins {
    alias(libs.plugins.android.application)
}

task("generateJavadoc", type: Javadoc) {
    description "Generates Javadoc for $variant.name."
    source = variant.javaCompile.source
    ext.androidJar = "${android.sdkDirectory}/platforms/${android.compileSdkVersion}/android.jar"
    classpath = files(variant.javaCompile.classpath.files) + files(ext.androidJar)
    options.links("http://docs.oracle.com/javase/11/docs/api/");
    options.links("http://d.android.com/reference/");
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