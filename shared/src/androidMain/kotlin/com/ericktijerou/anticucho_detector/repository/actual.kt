package com.ericktijerou.anticucho_detector.repository

import co.touchlab.kermit.LogcatLogger
import co.touchlab.kermit.Logger
import com.ericktijerou.anticucho_detector.db.AnticuchoDetectorDatabase
import com.ericktijerou.anticucho_detector.di.AnticuchoDetectorDatabaseWrapper
import com.squareup.sqldelight.android.AndroidSqliteDriver
import org.koin.dsl.module

actual fun platformModule() = module {
    single {
        val driver =
            AndroidSqliteDriver(AnticuchoDetectorDatabase.Schema, get(), "anticuchodetector.db")

        AnticuchoDetectorDatabaseWrapper(AnticuchoDetectorDatabase(driver))
    }
    single<Logger>{ LogcatLogger() }
}
