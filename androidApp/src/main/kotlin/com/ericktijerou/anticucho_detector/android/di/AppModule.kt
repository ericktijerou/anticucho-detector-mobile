package com.ericktijerou.anticucho_detector.android.di

import com.ericktijerou.anticucho_detector.android.ui.AnticuchoDetectorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { AnticuchoDetectorViewModel(get(), get()) }
}