package app.involvexcord.manager.di

import app.involvexcord.manager.domain.manager.DownloadManager
import app.involvexcord.manager.domain.manager.InstallManager
import app.involvexcord.manager.domain.manager.PreferenceManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val managerModule = module {
    singleOf(::DownloadManager)
    singleOf(::PreferenceManager)
    single { InstallManager(get(), get(), get()) }
}