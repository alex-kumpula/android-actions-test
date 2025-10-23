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

            val opts = options as org.gradle.external.javadoc.StandardJavadocDocletOptions
            opts.addStringOption("Xdoclint:none", "-quiet")
            opts.encoding = "UTF-8"
            opts.memberLevel = JavadocMemberLevel.PUBLIC
            // opts.addStringOption("subpackages", "com.example.demoapp")
            opts.addStringOption("exclude", "android.*:androidx.*:kotlin.*")
            opts.addStringOption("-stacktrace")
            //opts.addStringOption("-debug")
        }
    }
}

tasks.register<Javadoc>("generateCleanJavadoc") {
    group = "documentation"
    description = "Generate clean Javadoc for com.example.demoapp"

    // Only include your package
    source = fileTree("src/main/java") {
        include("com/example/demoapp/**/*.java")
    }

    // Exclude unwanted generated files
    exclude("**/R.java", "**/BuildConfig.java")

    // Output directory
    destinationDir = file("$buildDir/docs/javadoc")

    // Classpath: include main source + Android compile classpath
    val androidJar = "${android.sdkDirectory}/platforms/${android.compileSdkVersion}/android.jar"
    classpath = files(androidJar) + files(android.sourceSets["main"].java.srcDirs)

    // Configure doclet options
    (options as StandardJavadocDocletOptions).apply {
        encoding = "UTF-8"
        memberLevel = JavadocMemberLevel.PUBLIC

        // Hide inherited methods and fields
        addBooleanOption("noinherited", true)

        // Hide hierarchy tree, navbar, index pages
        addBooleanOption("notree", true)
        addBooleanOption("noindex", true)
        addBooleanOption("nonavbar", true)

        // Silence doclint warnings
        addStringOption("Xdoclint:none", "-quiet")
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