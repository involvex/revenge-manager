package app.involvexcord.manager.utils

object Constants {
    const val REVENGE_BUNDLE_OWNER = "involvex"
    const val REVENGE_BUNDLE_REPO = "revenge-bundle"
    const val REVENGE_MANAGER_OWNER = "involvex"
    const val REVENGE_MANAGER_REPO = "revenge-manager"
    const val MOD_DIR = "mod"

    val DUMMY_VERSION = DiscordVersion(0, 0, DiscordVersion.Type.STABLE)

    val TEAM_MEMBERS = listOf(
        TeamMember("Involvex", "Developer", "involvex"),
        TeamMember("AnotherDev", "Contributor", "anotherdev")
    )
}

data class TeamMember(
    val name: String,
    val role: String,
    val username: String
)
