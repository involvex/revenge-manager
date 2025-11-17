package app.involvexcord.manager.installer.step.download

import app.involvexcord.manager.domain.manager.InstallManager
import app.involvexcord.manager.installer.step.Step
import app.involvexcord.manager.installer.step.StepGroup
import app.involvexcord.manager.installer.step.StepRunner
import org.koin.core.component.inject

abstract class DownloadGitHubFileStep(
    override val nameRes: Int,
    private val owner: String,
    private val repo: String,
    private val filePath: String
) : Step() {

    override val group = StepGroup.DL

    private val installManager: InstallManager by inject()

    override suspend fun run(runner: StepRunner) {
        runner.logger.i("Downloading $filePath from $owner/$repo")
        val downloadedFile = when (repo) {
            "involvex/revenge-bundle" -> installManager.downloadRevengeBundleFile(filePath)
            "involvex/revenge-manager" -> installManager.downloadRevengeManagerFile(filePath)
            else -> null
        }

        if (downloadedFile == null) {
            throw Exception("Failed to download $filePath from $owner/$repo")
        }
        runner.logger.i("Downloaded $filePath to ${downloadedFile.absolutePath}")
    }
}
