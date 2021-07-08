package com.ericktijerou.anticucho_detector.android

import android.app.Application
import co.touchlab.kermit.Kermit
import com.ericktijerou.anticucho_detector.android.di.appModule
import com.ericktijerou.anticucho_detector.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AnticuchoDetectorApp : Application(), KoinComponent {
    private val logger: Kermit by inject()

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@AnticuchoDetectorApp)
            modules(appModule)
        }

        logger.d { "AnticuchoDetectorApp" }
    }
}