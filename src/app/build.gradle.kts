plugins {
    alias(libs.plugins.android.application)
}

import org.gradle.external.javadoc.JavadocMemberLevel
import com.android.build.api.variant.hasJava
// Removed: import com.android.build.gradle.api.ApplicationVariant (deprecated)

// Define your application's root package here
val appPackage = "com.example.demoapp"

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

// --- JAVADOC GENERATION LOGIC: Using modern androidComponents API ---
androidComponents {
    onVariants(selector().withBuildType("release")) { variant ->
        // Check if the variant has Java sources before proceeding
        if (!variant.hasJava) return@onVariants

        // Generate Javadoc task only for the 'release' build type
        
        val capitalized = variant.name.replaceFirstChar { it.uppercaseChar() }

        tasks.register<Javadoc>("generate${capitalized}Javadoc") {
            group = "documentation"
            description = "Generate ${variant.name} Javadoc"
            
            // FIX 1: Get source files from the release variant's Java compilation using .asFileTree
            // Use .get() to resolve the lazy property
            source = files(variant.sources.java!!.all.get().asFileTree)
            destinationDir = file("$rootDir/doc/javadoc/")
            exclude("**/BuildConfig.java")
            
            doFirst {
                // Determine the compile SDK version for the android.jar path
                // FIX 2: Get compileSdkVersion safely
                val compileSdk = project.property("android.compileSdkVersion") as String 
                val androidJar = "${android.sdkDirectory}/platforms/$compileSdk/android.jar"
                
                // FIX 3: Add dependencies and the Android platform jar to the Javadoc classpath
                // Use the compileClasspath directly and add the platform JAR
                classpath = variant.compileClasspath + files(androidJar)
    
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
// --- END JAVADOC GENERATION LOGIC ---

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
