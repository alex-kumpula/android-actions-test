plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.dokka") version "1.9.20"
}

// outputDirectory = file("${project.rootProject.projectDir}/doc/javadoc")

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    outputDirectory.set(file("${rootProject.projectDir}/doc/javadoc"))

    dokkaSourceSets {
        named("main") {
            displayName.set("Android App")
            includeNonPublic.set(true)
            reportUndocumented.set(true)
            skipDeprecated.set(false)

            // Explicitly tell Dokka where your source code is
            sourceRoots.from(file("src/main/java"))
            sourceRoots.from(file("src/main/kotlin"))

            // Add Android SDK stubs so Android symbols resolve
            perPackageOption {
                matchingRegex.set(".*android.*")
                suppress.set(true)
            }

            // If you want to include comments from external docs (optional)
            externalDocumentationLink {
                url.set(URL("https://developer.android.com/reference/"))
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