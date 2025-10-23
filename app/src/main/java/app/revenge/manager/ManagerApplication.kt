package app.involvexcord.manager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import app.involvexcord.manager.di.httpModule
import app.involvexcord.manager.di.managerModule
import app.involvexcord.manager.di.repositoryModule
import app.involvexcord.manager.di.viewModelModule
import app.involvexcord.manager.domain.manager.PreferenceManager
import app.involvexcord.manager.domain.manager.UpdateCheckerDuration
import app.involvexcord.manager.updatechecker.worker.UpdateWorker
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class ManagerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initNotificationChannels()

        startKoin {
            androidContext(this@ManagerApplication)
            modules(
                httpModule,
                managerModule,
                viewModelModule,
                repositoryModule
            )
        }

        val prefs: PreferenceManager = get()

        if (prefs.updateDuration != UpdateCheckerDuration.DISABLED) {
            val duration = prefs.updateDuration

            val (period, unit) = when (duration) {
                UpdateCheckerDuration.QUARTERLY -> 15L to TimeUnit.MINUTES
                UpdateCheckerDuration.HALF_HOUR -> 30L to TimeUnit.MINUTES
                UpdateCheckerDuration.HOURLY -> 1L to TimeUnit.HOURS
                UpdateCheckerDuration.BIHOURLY -> 2L to TimeUnit.HOURS
                UpdateCheckerDuration.TWICE_DAILY -> 12L to TimeUnit.HOURS
                UpdateCheckerDuration.DAILY -> 1L to TimeUnit.DAYS
                UpdateCheckerDuration.WEEKLY -> 7L to TimeUnit.DAYS
                else -> 0L to TimeUnit.SECONDS
            }

            if (period > 0L) {
                WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
                    "app.involvexcord.manager.UPDATE_CHECK",
                    ExistingPeriodicWorkPolicy.KEEP,
                    PeriodicWorkRequestBuilder<UpdateWorker>(period, unit).build()
                )
            }
        }
    }

    private fun initNotificationChannels() {
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val updates = NotificationChannel(
            "${BuildConfig.APPLICATION_ID}.notifications.UPDATE",
            "Discord updates",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        nm.createNotificationChannel(updates)
    }

}