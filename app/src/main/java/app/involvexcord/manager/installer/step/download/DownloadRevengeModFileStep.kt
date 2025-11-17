package app.involvexcord.manager.installer.step.download

import app.involvexcord.manager.R
import app.involvexcord.manager.utils.Constants

class DownloadRevengeModFileStep : DownloadGitHubFileStep(
    nameRes = R.string.step_download_revenge_mod,
    owner = Constants.REVENGE_BUNDLE_OWNER,
    repo = Constants.REVENGE_BUNDLE_REPO,
    filePath = "mod/revenge.zip" // Placeholder, adjust as needed
)
