package com.hakanemik.ortakakil.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.hakanemik.ortakakil.entity.BottomBarState
import com.hakanemik.ortakakil.entity.TopBarState
import com.hakanemik.ortakakil.helper.TimeUtils
import com.hakanemik.ortakakil.repo.TokenManager
import com.hakanemik.ortakakil.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination.asStateFlow()

    private val _topBarState = MutableStateFlow(TopBarState())
    val topBarState: StateFlow<TopBarState> = _topBarState.asStateFlow()

    private val _bottomBarState = MutableStateFlow(BottomBarState())
    val bottomBarState: StateFlow<BottomBarState> = _bottomBarState.asStateFlow()
    init {
        checkInitialNavigation()
    }


    private fun checkInitialNavigation() {
        viewModelScope.launch {

            delay(2000L)

            val accessToken = tokenManager.getToken()
            val refreshToken = tokenManager.getRefreshToken()
            val rememberMe = userRepository.getRememberMe()
            val userId = userRepository.getUserId()

            if (!rememberMe || userId == null || refreshToken == null) {
                _startDestination.value = "login_page"; return@launch
            }

            if (tokenManager.isRefreshExpired()) {
                _startDestination.value = "login_page"; return@launch
            }

            if (!tokenManager.isAccessExpired()) {
                _startDestination.value = "home_page"; return@launch
            }

            try {
                val ref = tokenManager.getRefreshToken()!!
                val res = userRepository.refreshWithRefreshToken(ref) // authApi
                res.data?.let { d ->
                    val accessExp = d.tokenExpiry
                    val refreshExp = d.refreshExpiry
                    val accessExpMs =TimeUtils.isoToMillis(accessExp)
                    val refreshExpMs = TimeUtils.isoToMillis(refreshExp)
                    tokenManager.saveTokens(d.accessToken, d.refreshToken, accessExpMs , refreshExpMs)
                    _startDestination.value = "home_page"; return@launch
                }
                _startDestination.value = "login_page"
            }catch (_: Exception){
                _startDestination.value = "login_page"
            }

        }
    }
    fun setTopBar(
        title: String,
        leftIcon: Int? = null,
        rightIcon: Int? = null,
        onLeftClick: () -> Unit = {},
        onRightClick: () -> Unit = {},
        isVisible: Boolean = true
    ) {
        _topBarState.value = TopBarState(
            title = title,
            leftIcon = leftIcon,
            rightIcon = rightIcon,
            onLeftIconClick = onLeftClick,
            onRightIconClick = onRightClick,
            isVisible = isVisible
        )
    }

    fun setBottomBar(isVisible: Boolean) {
        _bottomBarState.value = BottomBarState(isVisible = isVisible)
    }

    fun hideTopBar() {
        _topBarState.value = _topBarState.value.copy(isVisible = false)
    }

    fun showTopBar() {
        _topBarState.value = _topBarState.value.copy(isVisible = true)
    }

    fun hideBottomBar() {
        _bottomBarState.value = BottomBarState(isVisible = false)
    }

    fun showBottomBar() {
        _bottomBarState.value = BottomBarState(isVisible = true)
    }

    fun answerPageBackNavigate(navController: NavController){
        navController.popBackStack()
    }
   suspend fun logout() {
        tokenManager.clearTokens()
        userRepository.logout()
        _startDestination.value = "login_page"
    }
}