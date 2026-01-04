package com.hakanemik.ortakakil.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hakanemik.ortakakil.entity.BottomBarState
import com.hakanemik.ortakakil.entity.Enum.SnackbarType
import com.hakanemik.ortakakil.entity.TopBarState
import com.hakanemik.ortakakil.helper.TimeUtils
import com.hakanemik.ortakakil.repo.OrtakAkilDaoRepository
import com.hakanemik.ortakakil.repo.TokenManager
import com.hakanemik.ortakakil.repo.UserRepository
import com.hakanemik.ortakakil.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val userRepository: UserRepository,
    private val repository: OrtakAkilDaoRepository
) : ViewModel() {

    // One-shot UI Events (Navigation + Snackbar)
    sealed interface UiEffect {
        data class NavigateTo(val route: String, val popUpToRoute: String? = null, val inclusive: Boolean = false) : UiEffect
        data object GoBack : UiEffect
        data class ShowSnackbar(val message: String, val type: SnackbarType) : UiEffect
    }

    private val _uiEvent = Channel<UiEffect>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination.asStateFlow()

    private val _topBarState = MutableStateFlow(TopBarState())
    val topBarState: StateFlow<TopBarState> = _topBarState.asStateFlow()

    private val _bottomBarState = MutableStateFlow(BottomBarState())
    val bottomBarState: StateFlow<BottomBarState> = _bottomBarState.asStateFlow()

    // Cached user info for TopBar
    private var cachedUserName: String? = null
    private var cachedUserPicture: String? = null

    init {
        checkInitialNavigation()
        observeUserData()
    }

    private fun checkInitialNavigation() {
        viewModelScope.launch {
            delay(2000L)

            val refreshToken = tokenManager.getRefreshToken()
            val rememberMe = userRepository.getRememberMe()
            val userId = userRepository.getUserId()

            // Güvenli kontrol: Login'e yönlendir
            if (!rememberMe || userId == null || refreshToken == null) {
                _startDestination.update { Screen.Login.route }
                return@launch
            }

            if (tokenManager.isRefreshExpired()) {
                _startDestination.update { Screen.Login.route }
                return@launch
            }

            if (!tokenManager.isAccessExpired()) {
                _startDestination.update { Screen.Home.route }
                return@launch
            }

            try {
                val res = userRepository.refreshWithRefreshToken(refreshToken)
                res.data?.let { d ->
                    val accessExpMs = TimeUtils.isoToMillis(d.tokenExpiry)
                    val refreshExpMs = TimeUtils.isoToMillis(d.refreshExpiry)
                    tokenManager.saveTokens(d.accessToken, d.refreshToken, accessExpMs, refreshExpMs)

                    _startDestination.update { Screen.Home.route }
                    return@launch
                }
                _startDestination.update { Screen.Login.route }
            } catch (_: Exception) {
                _startDestination.update { Screen.Login.route }
            }
        }
        }


    private fun observeUserData() {
        viewModelScope.launch {
            combine(
                userRepository.getUserNameFlow(),
                userRepository.getUserPictureFlow()
            ) { name, pic ->
                Pair(name, pic)
            }.collectLatest { (name, pic) ->
                cachedUserName = name
                cachedUserPicture = pic

                // If currently showing HomePage TopBar, update it live
                if (_topBarState.value.isHomePage) {
                    _topBarState.update {
                        it.copy(
                            userName = name ?: "Misafir",
                            userPictureUrl = pic
                        )
                    }
                }
            }
        }
    }

    fun setTopBar(
        title: String,
        isHomePage: Boolean = false,
        leftIcon: Int? = null,
        rightIcon: Int? = null,
        onLeftClick: () -> Unit = {},
        onRightClick: () -> Unit = {},
        isVisible: Boolean = true
    ) {
        // Yeni bir state nesnesi oluşturmak burada mantıklıdır çünkü tüm barı baştan kuruyoruz
        _topBarState.update {
            TopBarState(
                isHomePage = isHomePage,
                title = title,
                leftIcon = leftIcon,
                rightIcon = rightIcon,
                onLeftIconClick = onLeftClick,
                onRightIconClick = onRightClick,

                isVisible = isVisible,
                userName = if (isHomePage) cachedUserName ?: "Misafir" else null,
                userPictureUrl = if (isHomePage) cachedUserPicture else null
            )
        }
    }

    fun setBottomBar(isVisible: Boolean) {
        _bottomBarState.update { it.copy(isVisible = isVisible) }
    }

    fun hideTopBar() {
        _topBarState.update { it.copy(isVisible = false) }
    }

    fun showTopBar() {
        _topBarState.update { it.copy(isVisible = true) }
    }

    fun hideBottomBar() {
        _bottomBarState.update { it.copy(isVisible = false) }
    }

    fun showBottomBar() {
        _bottomBarState.update { it.copy(isVisible = true) }
    }

    fun answerPageBackNavigate() {
        viewModelScope.launch {
            _uiEvent.send(UiEffect.GoBack)
        }
    }

    fun logout() {
        val refreshToken = tokenManager.getRefreshToken()
        viewModelScope.launch {
            try {
                repository.logout(refreshToken)
            } finally {
                _uiEvent.send(
                    UiEffect.NavigateTo(
                        route = Screen.Login.route,
                        inclusive = true
                    )
                )
                userRepository.logout()
            }
        }
    }

    fun showSnackbar(message: String, type: SnackbarType = SnackbarType.INFO) {
        viewModelScope.launch {
            _uiEvent.send(UiEffect.ShowSnackbar(message, type))
        }
    }
}

