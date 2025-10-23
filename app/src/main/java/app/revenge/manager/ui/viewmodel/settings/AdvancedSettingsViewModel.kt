package app.involvexcord.manager.ui.viewmodel.settings

import android.content.Context
import android.os.Environment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import app.involvexcord.manager.BuildConfig
import app.involvexcord.manager.R
import app.involvexcord.manager.domain.manager.InstallMethod
import app.involvexcord.manager.domain.manager.PreferenceManager
import app.involvexcord.manager.domain.manager.UpdateCheckerDuration
import app.involvexcord.manager.installer.shizuku.ShizukuPermissions
import app.involvexcord.manager.updatechecker.worker.UpdateWorker
import app.involvexcord.manager.utils.showToast
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.TimeUnit

class AdvancedSettingsViewModel(
    private val context: Context,
    private val prefs: PreferenceManager,
) : ScreenModel {
    private val cacheDir = context.externalCacheDir ?: File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS).resolve(
        BuildConfig.MANAGER_NAME).also { it.mkdirs() }

    fun clearCache() {
        cacheDir.deleteRecursively()
        context.showToast(R.string.msg_cleared_cache)
    }

    fun updateCheckerDuration(updateCheckerDuration: UpdateCheckerDuration) {
        val wm = WorkManager.getInstance(context)
        when (updateCheckerDuration) {
            UpdateCheckerDuration.DISABLED -> wm.cancelUniqueWork("app.involvexcord.manager.UPDATE_CHECK")
            else -> {
                val (period, unit) = when (updateCheckerDuration) {
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
                    wm.enqueueUniquePeriodicWork(
                        "app.involvexcord.manager.UPDATE_CHECK",
                        ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                        PeriodicWorkRequestBuilder<UpdateWorker>(period, unit).build()
                    )
                }
            }
        }
    }

    fun setInstallMethod(method: InstallMethod) {
        when (method) {
            InstallMethod.SHIZUKU -> screenModelScope.launch {
                if (ShizukuPermissions.waitShizukuPermissions()) {
                    prefs.installMethod = InstallMethod.SHIZUKU
                } else {
                    context.showToast(R.string.msg_shizuku_denied)
                }
            }

            else -> prefs.installMethod = method
        }
    }

}