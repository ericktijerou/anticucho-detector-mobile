object Versions {
    const val kotlin = "1.5.10"
    const val kotlinCoroutines = "1.5.1"
    const val ktor = "1.6.1"
    const val kotlinxSerialization = "1.1.0"
    const val koin = "3.1.2"
    const val sqlDelight = "1.5.0"
    const val kermit = "0.1.9"
    const val sqliteJdbcDriver = "3.30.1"
    const val slf4j = "1.7.30"
    const val compose = "1.0.0-rc01"
    const val nav_compose = "2.4.0-alpha04"
    const val accompanist = "0.13.0"
    const val junit = "4.13.2"
    const val activityCompose = "1.3.0-rc01"
    const val lifecycle = "2.4.0-alpha02"
    const val composeViewModel = "1.0.0-alpha01"
    const val material = "1.4.0"
    const val osmdroid = "6.1.10"
    const val cameraxVersion = "1.0.0"
    const val cameraView = "1.0.0-alpha26"
    const val robolectric = "4.5.1"
    const val androidTest = "1.4.0"
}

object AndroidSdk {
    const val min = 21
    const val compile = 30
    const val target = compile
}

object Deps {
    const val kermit = "co.touchlab:kermit:${Versions.kermit}"
}

object Test {
    const val junit = "junit:junit:${Versions.junit}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val androidCore = "androidx.test:core:${Versions.androidTest}"
    const val androidRunner = "androidx.test:runner:${Versions.androidTest}"
}

object Compose {
    const val ui = "androidx.compose.ui:ui:${Versions.compose}"
    const val uiGraphics = "androidx.compose.ui:ui-graphics:${Versions.compose}"
    const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val foundationLayout = "androidx.compose.foundation:foundation-layout:${Versions.compose}"
    const val material = "androidx.compose.material:material:${Versions.compose}"
    const val navigation = "androidx.navigation:navigation-compose:${Versions.nav_compose}"
    const val accompanistCoil = "com.google.accompanist:accompanist-coil:${Versions.accompanist}"
    const val accompanistInsets = "com.google.accompanist:accompanist-insets:${Versions.accompanist}"
    const val composeLifecycle = "androidx.compose.runtime:runtime-livedata:${Versions.compose}"
    const val runtime = "androidx.compose.runtime:runtime:${Versions.compose}"
    const val compiler = "androidx.compose.compiler:compiler:${Versions.compose}"
}

object Activity {
    const val compose = "androidx.activity:activity-compose:${Versions.activityCompose}"
}

object Android {
    const val material = "com.google.android.material:material:${Versions.material}"
    const val osmdroid = "org.osmdroid:osmdroid-android:${Versions.osmdroid}"
}

object Lifecycle {
    const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val process = "androidx.lifecycle:lifecycle-process:${Versions.lifecycle}"
    const val compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val viewModelCompose =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.composeViewModel}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
}

object CameraX {
    const val camera2 = "androidx.camera:camera-camera2:${Versions.cameraxVersion}"
    const val lifecycle = "androidx.camera:camera-lifecycle:${Versions.cameraxVersion}"
    const val cameraView = "androidx.camera:camera-view:${Versions.cameraView}"
}

object Koin {
    const val core = "io.insert-koin:koin-core:${Versions.koin}"
    const val test = "io.insert-koin:koin-test:${Versions.koin}"
    const val android = "io.insert-koin:koin-android:${Versions.koin}"
    const val compose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
}

object Ktor {
    const val clientCore = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val clientJson = "io.ktor:ktor-client-json:${Versions.ktor}"
    const val clientLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
    const val clientSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    const val clientAndroid = "io.ktor:ktor-client-android:${Versions.ktor}"
    const val clientApache = "io.ktor:ktor-client-apache:${Versions.ktor}"
    const val slf4j = "org.slf4j:slf4j-simple:${Versions.slf4j}"
    const val clientIos = "io.ktor:ktor-client-ios:${Versions.ktor}"
    const val clientCio = "io.ktor:ktor-client-cio:${Versions.ktor}"
    const val clientJs = "io.ktor:ktor-client-js:${Versions.ktor}"
}

object Serialization {
    const val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinxSerialization}"
}

object SqlDelight {
    const val runtime = "com.squareup.sqldelight:runtime:${Versions.sqlDelight}"
    const val coroutineExtensions = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"
    const val androidDriver = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
    const val nativeDriver = "com.squareup.sqldelight:native-driver:${Versions.sqlDelight}"
    const val nativeDriverMacos = "com.squareup.sqldelight:native-driver-macosx64:${Versions.sqlDelight}"
    const val jdbcDriver = "org.xerial:sqlite-jdbc:${Versions.sqliteJdbcDriver}"
    const val sqlliteDriver = "com.squareup.sqldelight:sqlite-driver:${Versions.sqlDelight}"
}