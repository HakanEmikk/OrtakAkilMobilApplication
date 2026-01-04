package com.hakanemik.ortakakil.ui.page

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.CategoryItem
import com.hakanemik.ortakakil.entity.Enum.SnackbarType
import com.hakanemik.ortakakil.entity.HomeUiState
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.helper.responsive
import com.hakanemik.ortakakil.helper.responsiveSp
import com.hakanemik.ortakakil.viewmodel.HomePageViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun HomePage(
    navController: NavController,
    onShowSnackbar: (String, SnackbarType) -> Unit,
    viewModel: HomePageViewModel = hiltViewModel()
) {
    val deviceSize = currentDeviceSizeHelper()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Box {

            when (deviceSize) {
                DeviceSize.Compact -> CompactLayout(deviceSize, uiState, viewModel, navController)
                DeviceSize.Medium -> MediumLayout(deviceSize, uiState, viewModel, navController)
                DeviceSize.Expanded -> ExpandedLayout(deviceSize, uiState, viewModel, navController)
            }
            HandleUIState(viewModel,uiState.isClicked,navController,uiState.question)
        }

    }


@Composable
fun CompactLayout(
    deviceSize: DeviceSize,
    uiState: HomeUiState,
    homeViewModel: HomePageViewModel,
    navController: NavController
) {
    val categories =   listOf(
        CategoryItem("Alışveriş", R.drawable.shopping_cart, colorResource(id = R.color.white)),
        CategoryItem("Kariyer", R.drawable.work, colorResource(id = R.color.white)),
        CategoryItem("İlişkiler", R.drawable.handshake, colorResource(id = R.color.white)),
        CategoryItem("Yaşam", R.drawable.emoji_people, colorResource(id = R.color.white))
    )

    val populerQuestion = remember {  listOf(
        "Hangi telefonu alsam daha iyi?",
        "İş değiştirmeli miyim?",
        "Bu kıyafet bana yakışır mı?"
    )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.background_dark),
                        colorResource(id = R.color.background_dark).copy(alpha = 0.90f),
                        colorResource(id = R.color.surface_dark).copy(alpha = 0.5f)
                    )
                )
            )
            .padding(horizontal = 24.dp,)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Bugün hangi konuda karar vermekte zorlanıyorsun?",
            color = colorResource(id = R.color.text_primary),
            fontSize = 15f.responsiveSp(20f, 22f, 24f, deviceSize),
            maxLines = 2,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp.responsive(32.dp, 36.dp, 28.dp, deviceSize)))

        // Search Box - Modern gradient border effect
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp.responsive(64.dp, 70.dp, 60.dp, deviceSize))
        ) {
            // Gradient border effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                colorResource(id = R.color.gradient_start),
                                colorResource(id = R.color.gradient_end)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
            )

            TextField(
                value = uiState.question,
                onValueChange = homeViewModel::onQuestionChange,
                placeholder = {
                    Text(
                        text = "Sorunu buraya yaz...",
                        color = colorResource(id = R.color.text_muted),
                        fontSize = 14.sp
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(RoundedCornerShape(15.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.surface_dark),
                    unfocusedContainerColor = colorResource(id = R.color.surface_dark),
                    disabledContainerColor = colorResource(id = R.color.surface_dark),
                    cursorColor = colorResource(id = R.color.primary_purple),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = colorResource(id = R.color.text_primary),
                    unfocusedTextColor = colorResource(id = R.color.text_primary),
                ),
                trailingIcon = {
                    FilledIconButton(
                        onClick = homeViewModel::aiQuestion,
                        modifier = Modifier
                            .size(48.dp.responsive(48.dp, 54.dp, 48.dp, deviceSize))
                            .padding(end = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = colorResource(id = R.color.primary_purple),
                            contentColor = colorResource(id = R.color.white),
                        ),
                    ) {
                        Icon(
                            contentDescription = "Send",
                            painter = painterResource(R.drawable.pencil),
                            tint = colorResource(id = R.color.white),
                            modifier = Modifier.size(22.dp.responsive(22.dp, 26.dp, 22.dp, deviceSize))
                        )
                    }
                },
                shape = RoundedCornerShape(15.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp.responsive(32.dp, 36.dp, 28.dp, deviceSize)))

        // Categories Section
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Kategoriler",
                    color = colorResource(id = R.color.text_primary),
                    fontSize = 18f.responsiveSp(18f, 20f, 22f, deviceSize),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Tümünü Gör",
                    color = colorResource(id = R.color.primary_purple),
                    fontSize = 12f.responsiveSp(12f, 14f, 16f, deviceSize),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 180.dp)
            ) {
                items(categories) { item ->
                    ModernCategoryButton(
                        item = item,
                        isSelected = uiState.selected == item.title,
                        onClick = { homeViewModel.onCategorySelected(item.title) },
                        deviceSize = deviceSize,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp.responsive(32.dp, 36.dp, 28.dp, deviceSize)))

        // Popular Questions Section
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Popüler Sorular",
                    color = colorResource(id = R.color.text_primary),
                    fontSize = 18f.responsiveSp(18f, 20f, 22f, deviceSize),
                    fontWeight = FontWeight.Bold
                )
//                Icon(
//                    painter = painterResource(id = R.drawable.pencil),
//                    contentDescription = "trending",
//                    tint = colorResource(id = R.color.warning),
//                    modifier = Modifier.size(20.dp)
//                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 250.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(populerQuestion) { item ->
                    ModernQuestionCard(
                        question = item,
                        deviceSize = deviceSize,
                        onClick = { homeViewModel.onQuestionChange(item) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun MediumLayout(
    deviceSize: DeviceSize,
    uiState: HomeUiState,
    homeViewModel: HomePageViewModel,
    navController: NavController
) {

}
@Composable
fun ExpandedLayout(
    deviceSize: DeviceSize,
    uiState: HomeUiState,
    homeViewModel: HomePageViewModel,
    navController: NavController
) {

}
@Composable
fun ModernCategoryButton(
    deviceSize: DeviceSize,
    item: CategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = tween(durationMillis = 200)
    )

    Box(
        modifier = Modifier
            .aspectRatio(2.8f)
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
    ) {
        // Gradient background for selected
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                colorResource(id = R.color.gradient_start),
                                colorResource(id = R.color.gradient_end)
                            )
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isSelected) 2.dp else 0.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(
                    if (isSelected)
                        colorResource(id = R.color.surface_dark)
                    else
                        colorResource(id = R.color.surface_light)
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = item.title,
                    tint = if (isSelected)
                        colorResource(id = R.color.primary_purple)
                    else
                        colorResource(id = R.color.text_secondary),
                    modifier = Modifier.size(28.dp.responsive(28.dp, 30.dp, 28.dp, deviceSize))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.title,
                    color = if (isSelected)
                        colorResource(id = R.color.text_primary)
                    else
                        colorResource(id = R.color.text_secondary),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun ModernQuestionCard(
    question: String,
    deviceSize: DeviceSize,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.surface_light)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp.responsive(16.dp, 18.dp, 16.dp, deviceSize)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorResource(id = R.color.purple_overlay_10)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.question),
                    contentDescription = "question",
                    tint = colorResource(id = R.color.primary_purple),
                    modifier = Modifier.size(20.dp.responsive(20.dp, 24.dp, 20.dp, deviceSize))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                question,
                color = colorResource(id = R.color.text_primary),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                maxLines = 2,
                modifier = Modifier.weight(1f)
            )

//            Icon(
//                painter = painterResource(id = R.drawable.settings),
//                contentDescription = "arrow",
//                tint = colorResource(id = R.color.text_muted),
//                modifier = Modifier.size(16.dp)
//            )
        }
    }
}



@Composable
private fun HandleUIState(
    viewModel: HomePageViewModel,
    isClicked: Boolean,
    navController: NavController,
    question: String
) {
    LaunchedEffect(isClicked) {
        if (isClicked) {
            navController.navigate("answer_page/${question}")
            viewModel.uiClean()

        }
    }
}