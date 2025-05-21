plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    kotlin("kapt")
   // id("com.google.gms.google-services")



}

android {
    namespace = "com.example.medicall"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.medicall"
        minSdk = 24
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation ("androidx.navigation:navigation-compose:2.4.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation ("com.google.android.gms:play-services-auth:20.5.0")
    implementation ("androidx.core:core-ktx:1.9.0")
    implementation ("com.jakewharton.threetenabp:threetenabp:1.4.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.6.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.6.0")
    implementation ("com.google.code.gson:gson:2.8.5")
    // Coil
    implementation("io.coil-kt:coil-compose:2.4.0")
    // navigation
    implementation ("androidx.navigation:navigation-compose:2.8.3")
    //MockWebserver
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")
    //
    implementation("com.squareup.moshi:moshi:1.15.0")

    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.0") // For @JsonClass

    val room_version = "2.6.0"
    implementation ("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation ("androidx.room:room-ktx:$room_version")
    testImplementation ("androidx.room:room-testing:$room_version")
    ksp("com.google.dagger:hilt-compiler:2.50")
    implementation ("androidx.compose.material:material-icons-extended:1.0.5")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}