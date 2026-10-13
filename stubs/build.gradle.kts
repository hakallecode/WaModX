plugins {
    id("com.android.library")
}

android {
    namespace = "X"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // BaseActivity extends AppCompatActivity and exposes androidx ActionBar,
    // so the stubs module needs appcompat on its own compile classpath.
    // compileOnly: stubs are never packaged, they only exist for compilation.
    compileOnly("androidx.appcompat:appcompat:1.7.0")
}
