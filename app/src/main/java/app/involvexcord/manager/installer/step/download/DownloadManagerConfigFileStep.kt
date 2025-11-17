package app.involvexcord.manager.installer.step.download

import app.involvexcord.manager.R
import app.involvexcord.manager.utils.Constants

class DownloadManagerConfigFileStep : DownloadGitHubFileStep(
    nameRes = R.string.step_download_manager_config,
    owner = Constants.REVENGE_MANAGER_OWNER,
    repo = Constants.REVENGE_MANAGER_REPO,
    filePath = "config/manager_config.json" // Placeholder, adjust as needed
)
