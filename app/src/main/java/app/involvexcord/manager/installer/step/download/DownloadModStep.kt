package app.involvexcord.manager.installer.step.download

import androidx.compose.runtime.Stable
import app.involvexcord.manager.R
import app.involvexcord.manager.installer.step.download.base.DownloadStep
import java.io.File

/**
 * Downloads the Revenge XPosed module
 *
 * https://github.com/revenge-mod/revenge-xposed
 */
@Stable
class DownloadModStep(
    workingDir: File
): DownloadStep() {

    override val nameRes = R.string.step_dl_mod

    override val downloadFullUrl: String = "https://github.com/revenge-mod/revenge-xposed/releases/latest/download/app-release.apk"
    override val destination = preferenceManager.moduleLocation
    override val workingCopy = workingDir.resolve("xposed.apk")

}
