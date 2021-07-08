package com.ericktijerou.anticucho_detector.repository

import co.touchlab.kermit.Logger
import co.touchlab.kermit.NSLogLogger
import com.ericktijerou.anticucho_detector.db.AnticuchoDetectorDatabase
import com.ericktijerou.anticucho_detector.di.AnticuchoDetectorDatabaseWrapper
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.dsl.module

actual fun platformModule() = module {
    single {
        val driver = NativeSqliteDriver(AnticuchoDetectorDatabase.Schema, "anticuchodetector.db")
        AnticuchoDetectorDatabaseWrapper(AnticuchoDetectorDatabase(driver))
    }
    single<Logger>{ NSLogLogger() }
}