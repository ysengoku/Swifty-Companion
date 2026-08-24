package dev.ysengoku.swiftycompanion.data.model

import com.google.gson.annotations.SerializedName

data class User (
    val login: String,
    val image: UserImage?,
    val displayname: String?,
    val campus: List<Campus>,
    @SerializedName("campus_users") val campusUsers: List<CampusUser>,
    val titles: List<Title>,
    @SerializedName("titles_users") val titlesUsers: List<TitleUser>,
    @SerializedName("cursus_users") val cursusUsers: List<CursusUser>,
    @SerializedName("projects_users") val projectsUsers: List<ProjectUser>
)

data class UserImage(
    val link: String?,
    val versions: ImageVersions?
)

data class ImageVersions(
    val large: String?,
    val medium: String?,
    val small: String?,
    val micro: String?,
)

data class Campus(
    val id: Int,
    val name: String,
    val country: String,
)

data class CampusUser(
    @SerializedName("campus_id") val campusId: Int,
    @SerializedName("is_primary") val isPrimary: Boolean
)

data class Title(
    val id: Int,
    val name: String
)

data class TitleUser(
    @SerializedName("title_id") val titleId: Int,
    val selected: Boolean
)

data class CursusUser(
    @SerializedName("cursus_id") val cursusId: Int,
    val grade: String?,
    val level: Float,
    val cursus: Cursus,
    val skills: List<Skill>
)

data class Cursus(
    val id: Int,
    val name: String
)

data class Skill(
    val id: Int,
    val name: String,
    val level: Float
)

data class ProjectUser(
    val id: Int,
    @SerializedName("final_mark") val finalMark: Int?,
    val status: ProjectStatus?,
    @SerializedName("validated?") val validated: Boolean?,
    val project: Project,
    // API returns an array, but a project belongs to exactly one cursus
    @SerializedName("cursus_ids") val cursusIds: List<Int>
)

enum class ProjectStatus {
    @SerializedName("in_progress") IN_PROGRESS,
    @SerializedName("searching_a_group") SEARCHING_A_GROUP,
    @SerializedName("finished") FINISHED
}

data class Project(
    val id: Int,
    val name: String
)
