package com.hakanemik.ortakakil.ui.page

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.google.gson.Gson
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.AnswerItem
import com.hakanemik.ortakakil.entity.CategoryItem
import com.hakanemik.ortakakil.entity.Enum.SnackbarType
import com.hakanemik.ortakakil.entity.HomeUiState
import com.hakanemik.ortakakil.helper.DeviceSize
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.helper.responsiveSp
import com.hakanemik.ortakakil.ui.navigation.Screen
import com.hakanemik.ortakakil.viewmodel.HomePageViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePage(
    navController: NavController,
    onShowSnackbar: (String, SnackbarType) -> Unit,
    viewModel: HomePageViewModel = hiltViewModel()
) {
    val deviceSize = currentDeviceSizeHelper()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_dark))
    ) {
        when (deviceSize) {
            DeviceSize.Compact -> CompactLayout(deviceSize, uiState, viewModel, navController)
            else -> CompactLayout(deviceSize, uiState, viewModel, navController) // Diğerleri için de şimdilik compact
        }

        val answerItem = AnswerItem(uiState.question, uiState.selected)
        HandleUIState(viewModel, uiState.isClicked, navController, answerItem)
    }
}

@Composable
fun CompactLayout(
    deviceSize: DeviceSize,
    uiState: HomeUiState,
    homeViewModel: HomePageViewModel,
    navController: NavController
) {
    val categories = listOf(
        CategoryItem("Alışveriş", R.drawable.shopping_cart, colorResource(id = R.color.white)),
        CategoryItem("Kariyer", R.drawable.work, colorResource(id = R.color.white)),
        CategoryItem("İlişkiler", R.drawable.handshake, colorResource(id = R.color.white)),
        CategoryItem("Yaşam", R.drawable.emoji_people, colorResource(id = R.color.white))
    )

    val popularQuestions = remember {
        listOf(
            "Hangi telefonu alsam daha iyi?",
            "İş değiştirmeli miyim?",
            "Bu kıyafet bana yakışır mı?"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {


        Spacer(modifier = Modifier.height(32.dp))

        // --- 2. BAŞLIK VE SORU KUTUSU (GLASSMORPHISM) ---
        Text(
            text = "Bugün neye karar veriyoruz?",
            color = Color.White,
            fontSize = 22f.responsiveSp(24f, 26f, 28f, deviceSize),
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(
                    color = colorResource(id = R.color.surface_dark).copy(alpha = 0.8f),
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Color.White.copy(alpha = 0.15f), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            TextField(
                value = uiState.question,
                onValueChange = homeViewModel::onQuestionChange,
                placeholder = {
                    Text("Kararsız kaldığın konuyu yaz...",
                        color = colorResource(id = R.color.text_muted),
                        )
                },
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(20.dp)),
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
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(48.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(colorResource(R.color.gradient_start), colorResource(R.color.gradient_end))
                                ),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { homeViewModel.aiQuestion() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(painterResource(R.drawable.pencil), null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- 3. KATEGORİLER ---
        SectionHeader("Kategoriler", deviceSize)
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height(180.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = false,
            contentPadding = PaddingValues(top = 8.dp),

        ) {
            items(categories) { item ->
                ModernCategoryButton(
                    item = item,
                    isSelected = uiState.selected == item.title,
                    onClick = { homeViewModel.onCategorySelected(item.title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- 4. POPÜLER SORULAR ---
        SectionHeader("İlham Al", deviceSize)
        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            popularQuestions.forEach { question ->
                ModernQuestionCard(question, deviceSize) { homeViewModel.onQuestionChange(question) }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun SectionHeader(title: String, deviceSize: DeviceSize) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Tümü",
            color = colorResource(id = R.color.primary_purple),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ModernCategoryButton(
    item: CategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(if (isSelected) 1.05f else 1f)

    Box(
        modifier = Modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) colorResource(R.color.purple_overlay_20)
                else colorResource(R.color.surface_light).copy(alpha = 0.5f)
            )
            .border(
                1.dp,
                if (isSelected) colorResource(R.color.primary_purple) else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                tint = if (isSelected) colorResource(R.color.primary_purple) else Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = item.title,
                color = if (isSelected) Color.White else colorResource(R.color.text_secondary),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ModernQuestionCard(question: String, deviceSize: DeviceSize, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.surface_dark).copy(alpha = 0.4f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.question),
                contentDescription = null,
                tint = colorResource(id = R.color.primary_purple),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(question, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
            Icon(painterResource(R.drawable.pencil), null, tint = colorResource(R.color.text_muted), modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun HandleUIState(
    viewModel: HomePageViewModel,
    isClicked: Boolean,
    navController: NavController,
    answerItem: AnswerItem
) {
    LaunchedEffect(isClicked) {
        if (isClicked) {
            val json = Uri.encode(Gson().toJson(answerItem))
            navController.navigate(Screen.Answer.createRoute(json))
            viewModel.uiClean()
        }
    }
}