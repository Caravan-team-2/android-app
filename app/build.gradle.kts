import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.apollo)
}

android {
    namespace = "smartcaravans.constat.client"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "smartcaravans.constat.client"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget("21")
            languageVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.fromVersion("2.2")
            apiVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.fromVersion("2.2")
        }
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://ab90c2a4e077.ngrok-free.app/\""
            )
        }
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://ab90c2a4e077.ngrok-free.app/\""
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

apollo {
    service("service") {
        packageName.set("smartcaravans.constat.client")//your package name
        introspection {
            endpointUrl.set("https://ab90c2a4e077.ngrok-free.app/graphql")
            schemaFile.set(file("src/main/graphql/schema.graphqls")) //replace with the schema file location
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.material.kolor)
    implementation(libs.kmpalette.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.sh.reorderable)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //implementation(libs.material)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3.adaptive.navigation.suite)
    implementation(libs.androidx.material3.adaptive.navigation.suite.android)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.material.motion.compose.navigation)
    implementation(libs.waveslider)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.jaudiotagger)
    implementation(libs.timber)
    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
    implementation(libs.androidx.glance.material)
    implementation(libs.converter.gson)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.play.services.mlkit.barcode.scanning)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.serialization.json)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    ksp(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.gson)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.coil.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose.viewmodel.navigation)
    implementation(libs.androidx.collection)
    implementation(libs.androidx.collection.ktx)
    implementation(libs.disk.lru.cache)
    implementation(libs.bundles.ktor)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.androidx.animation.graphics)
    implementation(libs.mapbox.android)
    implementation(libs.mapbox.maps.compose)
    implementation(libs.play.services.location)
    implementation(libs.image.labeling)
    implementation("com.google.mlkit:text-recognition:16.0.0")
    implementation("com.google.mlkit:image-labeling:17.0.8")
    implementation("com.google.mlkit:camera:16.0.0-beta3")
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.bundles.ktor)
    testImplementation(libs.truth)
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("io.ktor:ktor-client-mock:2.3.4")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    implementation(libs.apollo.runtime)
    implementation(libs.zxing.core)
    implementation(libs.compose.qr.code)
    implementation(libs.signature.pad)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
}