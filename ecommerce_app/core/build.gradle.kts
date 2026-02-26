plugins {
    alias(libs.plugins.kotlin.serialize.plugin)
    alias(libs.plugins.ksp.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

}

android {
    namespace = "com.example.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

dependencies {
    // Ktor dependencies
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.loggin)
    implementation(libs.ktor.auth)
    implementation(libs.ktor.cio)


    //koin
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.nav)
    implementation(libs.koin.compose)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.koin.test)



    //room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.ktx)

    //secureDataBase
    implementation(libs.sqlcipher)
    implementation(libs.sql.light)

    // Kotlinx Serialization JSON
    implementation(libs.kotlinx.serialization.json)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)


    implementation(project(":common"))

}