package dev.ysengoku.swiftycompanion.ui.detail

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import java.io.IOException
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import dev.ysengoku.swiftycompanion.data.IntraApi
import dev.ysengoku.swiftycompanion.data.model.Campus
import dev.ysengoku.swiftycompanion.data.model.CampusUser
import dev.ysengoku.swiftycompanion.data.model.CursusUser
import dev.ysengoku.swiftycompanion.data.model.ProjectUser
import dev.ysengoku.swiftycompanion.data.model.ProjectStatus
import dev.ysengoku.swiftycompanion.data.model.Skill
import dev.ysengoku.swiftycompanion.data.model.Title
import dev.ysengoku.swiftycompanion.data.model.TitleUser
import dev.ysengoku.swiftycompanion.data.model.User
import dev.ysengoku.swiftycompanion.data.repository.UserRepository

sealed interface LoadState {
    object Loading : LoadState
    data class Success(val detail: DetailUiModel) : LoadState
    data class Error(val message: String) : LoadState
}

data class DetailUiState (
    val loadState: LoadState = LoadState.Loading,
    val selectedCursusId: Int? = null
)

class DetailViewModel (
    val login: String,
    val userRepository: UserRepository
    ) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()
    
    init {
        fetchUser()
    }
    
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val login: String = requireNotNull(savedStateHandle["login"])
                val userRepository = UserRepository(IntraApi.service)
                DetailViewModel(login, userRepository)
            }
        }
    }
    
    private fun fetchUser() {
        viewModelScope.launch {
            val result = userRepository.fetchUser(login).fold(
                onSuccess = {
                    _uiState.value = DetailUiState(
                        LoadState.Success(toDetailUiModel(it)),
                        resolveSelectedCursusId(it.cursusUsers)
                    )
                },
                onFailure = {
                    _uiState.value = DetailUiState(
                        LoadState.Error(mapErrorMessage(it))
                    )
                }
            )
        }
    }
    
    private fun toDetailUiModel(user: User): DetailUiModel {
        return DetailUiModel(
            user.login,
            user.image?.link,
            user.displayname,
            resolveCampus(user.campus, user.campusUsers),
            resolveTitle(user.titles, user.titlesUsers),
            mapCursus(user.cursusUsers),
            mapProjects(user.projectsUsers)
        )
    }
    
    private fun resolveCampus(
        campus: List<Campus>,
        campusUsers: List<CampusUser>
    ): CampusUi {
        val primary = requireNotNull(campusUsers.singleOrNull { it.isPrimary })
        val campusData = requireNotNull(campus.singleOrNull { primary.campusId == it.id })
        return CampusUi(campusData.name, campusData.country)
    }
    
    private fun resolveTitle(
        titles: List<Title>,
        titlesUsers: List<TitleUser>
    ): String? {
        val selected = titlesUsers.singleOrNull { it.selected }
        val title = titles.singleOrNull { selected?.titleId == it.id }
        return title?.name
    }
    
    private fun mapCursus(cursusUsers: List<CursusUser>): List<CursusUi> {
        return cursusUsers.map {
            CursusUi(
                it.cursusId,
                it.cursus.name,
                it.grade,
                it.level.roundToInt(),
                ((it.level % 1) * 100).roundToInt(),
                mapSkill(it.skills)
            )
        }
    }
    
    private fun mapSkill(skills: List<Skill>): List<SkillUi> {
        return skills.map {
            SkillUi(
                it.name,
                it.level.roundToInt(),
                ((it.level % 1) * 100).roundToInt()
            )
        }
    }
    
    private fun mapProjects(projectsUsers: List<ProjectUser>): List<ProjectUi> {
        return projectsUsers.filter { it.status == ProjectStatus.FINISHED }.map {
            ProjectUi(
                it.cursusIds[0],
                it.project.name,
                it.finalMark ?: 0,
                it.validated ?: false
            )
        }
    }
    
    private fun resolveSelectedCursusId(cursusUsers: List<CursusUser>): Int {
        val fortyTwoCursus = cursusUsers.find { it.cursus.name == "42cursus" }
        return fortyTwoCursus?.cursus?.id ?: requireNotNull(cursusUsers.maxByOrNull { it.beginAt }).cursus.id
    }

    private fun mapErrorMessage(e: Throwable): String {
        when {
            e is HttpException && e.code() == 404 -> return "Login \"$login\" does not exist."
            e is HttpException && e.code() == 500 -> return "The server is temporarily unavailable. Please try again later."
            e is HttpException -> return "Something went wrong. Please try again."
            e is IOException -> return "Couldn't connect. Check your internet connection and try again."
            else -> return e.message ?: "An unexpected error has occurred"
        }
    }
}
