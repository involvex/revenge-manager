package app.involvexcord.manager.domain.repository

import app.involvexcord.manager.network.service.RestService
import app.involvexcord.manager.network.utils.transform
import app.involvexcord.manager.utils.DiscordVersion

class RestRepository(
    private val service: RestService
) {

    suspend fun getLatestRelease(repo: String) = service.getLatestRelease(repo)

    suspend fun getLatestDiscordVersions() = service.getLatestDiscordVersions().transform {
        mapOf(
            DiscordVersion.Type.ALPHA to DiscordVersion.fromVersionCode(it.latest.alpha),
            DiscordVersion.Type.BETA to DiscordVersion.fromVersionCode(it.latest.beta),
            DiscordVersion.Type.STABLE to DiscordVersion.fromVersionCode(it.latest.stable)
        )
    }

    suspend fun getCommits(repo: String, page: Int = 1) = service.getCommits(repo, page)

}