package com.hakanemik.ortakakil.ui.page

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.hakanemik.ortakakil.entity.AnswerItem
import com.hakanemik.ortakakil.entity.AnswerUiEvent
import com.hakanemik.ortakakil.entity.Enum.SnackbarType
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.ui.utils.AnswerShareSheet
import com.hakanemik.ortakakil.viewmodel.AnswerPageViewModel
import io.noties.markwon.Markwon

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AnswerPage(
    navController: NavController,
    onShowSnackbar: (String, SnackbarType) -> Unit,
    answerItem: AnswerItem,
    viewModel: AnswerPageViewModel = hiltViewModel()
) {
    LaunchedEffect(answerItem) {
        val category = if (answerItem.category == "") "genel" else answerItem.category
        viewModel.loadAnswer(answerItem.question, category)
    }

    val deviceSize = currentDeviceSizeHelper()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val markwon = remember { Markwon.create(context) }


    val styledText = remember(uiState.answer) {
        markwon.toMarkdown(uiState.answer).toString()
    }
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AnswerUiEvent.ShareSuccess -> {
                    showSheet = false
                    onShowSnackbar("Sorunuz paylaşıldı", SnackbarType.SUCCESS)
                }
                is AnswerUiEvent.ShareError -> {
                    onShowSnackbar("Paylaşılırken bir hata oluştu", SnackbarType.ERROR)
                }

                else ->{}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_dark))
            .padding(horizontal = 20.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. SORU BÖLÜMÜ
            item {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)) {
                    Text(
                        text = "Sorduğun Soru",
                        fontSize = 13.sp,
                        color = colorResource(id = R.color.primary_purple),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = answerItem.question ?: "Soru yükleniyor...",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 34.sp
                    )
                }
            }

            // 2. AI CEVAP KARTI (Glassmorphism Effect)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.surface_dark).copy(alpha = 0.6f)
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Text(
                            "Ortak Akıl'ın Önerisi",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        if (uiState.isLoading) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    color = colorResource(id = R.color.primary_purple),
                                    strokeWidth = 3.dp,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Veriler analiz ediliyor...",
                                    color = colorResource(id = R.color.text_muted),
                                    fontSize = 14.sp
                                )
                            }
                        } else {
                            Text(
                                text = styledText,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp,
                                lineHeight = 26.sp, // Okunabilirlik için artırıldı
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        // 3. ALT AKSİYON ALANI
        Column(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Şık bir ayırıcı çizgi
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)) {
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.2f), Color.Transparent)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (uiState.isShared) {
                // Paylaşılmış durumu göster
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.success).copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(colorResource(id = R.color.success), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Yayında",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.success),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Bu karar Keşfet sayfasında paylaşıldı.",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.text_muted),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            } else {
                // Paylaşım butonu
                Button(
                    onClick = { showSheet = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.primary_purple)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.world),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Keşfet'te Paylaş",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Bu kararı diğer insanlarla paylaşarak fikir alabilirsiniz.",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.text_muted),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
    }

    if (showSheet) {
        AnswerShareSheet(
            onDismiss = { showSheet = false },
            value = uiState.shareNote,
            onValueChange = viewModel::onValueChange,
            onClick = viewModel::onClick
        )
    }
}