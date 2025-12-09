package com.hakanemik.ortakakil.entity

data class TopBarState(
    val title: String = "",
    val leftIcon: Int? = null,
    val rightIcon: Int? = null,
    val onLeftIconClick: () -> Unit = {},
    val onRightIconClick: () -> Unit = {},
    val isVisible: Boolean = true
)
