plugins { id("com.android.application"); id("org.jetbrains.kotlin.android"); id("com.google.devtools.ksp") }

android { namespace = "com.mohammad.scheduleddownloader"; compileSdk = 35
    defaultConfig { applicationId = "com.mohammad.scheduleddownloader"; minSdk = 26; targetSdk = 35; versionCode = 1; versionName = "1.0" }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(libs.androidx.activity); implementation(libs.androidx.core.ktx); implementation(libs.appcompat); implementation(libs.material)
    implementation(libs.recyclerview); implementation(libs.coordinatorlayout); implementation(libs.lifecycle.runtime)
    implementation(libs.room.runtime); implementation(libs.room.ktx); ksp(libs.room.compiler)
}
