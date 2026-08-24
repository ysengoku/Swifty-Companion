package dev.ysengoku.swiftycompanion.ui.detail

data class DetailUiModel (
    val login: String,
    val image: String?,
    val displayname: String?,
    // Only the primary campus (campus_users.is_primary == true) is shown
    val campus: CampusUi,
    // Only the currently selected title (titles_users.selected == true) is shown
    val title: String?,
    val cursus: List<CursusUi>,
    val projects: List<ProjectUi>,
)

data class CampusUi (
    val name: String,
    val country: String
)

data class CursusUi (
    val id: Int,
    val name: String,
    val grade: String?,
    val level: Int,
    val percentage: Int,
    val skills: List<SkillUi>
)

data class SkillUi (
    val name: String,
    val level: Int,
    val percentage: Int,
)

data class ProjectUi (
    // A project belongs to exactly one cursus, see ProjectUser.cursusIds
    val cursusId: Int,
    val name: String,
    // Only FINISHED projects are mapped, so these are never null in practice
    val finalMark: Int,
    val validated: Boolean,
)
