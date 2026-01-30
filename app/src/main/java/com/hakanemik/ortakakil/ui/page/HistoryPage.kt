package com.hakanemik.ortakakil.ui.page

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.gson.Gson
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.HistoryResponse
import com.hakanemik.ortakakil.ui.navigation.Screen
import com.hakanemik.ortakakil.ui.utils.DateUtils.calculateTimeAgo
import com.hakanemik.ortakakil.ui.utils.CommentsBottomSheet
import com.hakanemik.ortakakil.ui.utils.AnswerShareSheet
import com.hakanemik.ortakakil.entity.AnswerUiEvent
import com.hakanemik.ortakakil.entity.Enum.SnackbarType
import com.hakanemik.ortakakil.viewmodel.HistoryPageViewModel
import io.noties.markwon.Markwon


@Composable
fun HistoryPage(
    navController: NavController,
    onShowSnackbar: (String, SnackbarType) -> Unit,
    viewModel: HistoryPageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val comments by viewModel.selectedComments.collectAsStateWithLifecycle()
    var showComments by remember { mutableStateOf(false) }
    var showShareSheet by remember { mutableStateOf(false) }
    var selectedDecisionId by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AnswerUiEvent.ShareSuccess -> {
                    showShareSheet = false
                    onShowSnackbar("Başarıyla paylaşıldı", SnackbarType.SUCCESS)
                }
                is AnswerUiEvent.ShareError -> {
                    onShowSnackbar("Paylaşılırken bir hata oluştu", SnackbarType.ERROR)
                }
                is AnswerUiEvent.UnshareSuccess -> {
                    onShowSnackbar("Keşfetten kaldırıldı", SnackbarType.SUCCESS)
                }
                is AnswerUiEvent.UnshareError -> {
                    onShowSnackbar("Kaldırılırken bir hata oluştu", SnackbarType.ERROR)
                }

                AnswerUiEvent.ReportError -> {
                    onShowSnackbar("Bildirme işlemi başarısız", SnackbarType.ERROR)
                }
                AnswerUiEvent.ReportSuccess -> {
                    onShowSnackbar("İçerik bildirildi", SnackbarType.SUCCESS)
                }
                else -> {}
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refreshHistory()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_dark))
            .padding(horizontal = 16.dp)
    ) {
        if (!uiState.isLoading && uiState.list.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Geçmişiniz henüz boş görünüyor. Yeni bir şeyler sormaya ne dersiniz?",
                    color = colorResource(id = R.color.text_muted),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
            items(
                count = uiState.list.size,
                key = { index -> uiState.list[index].decisionId }
            ) { index ->
                val item = uiState.list[index]

                if (index >= uiState.list.size - 2 && !uiState.isLoading && uiState.list.isNotEmpty()) {
                    LaunchedEffect(Unit) {
                        viewModel.loadMore()
                    }
                }

                HistoryCard(
                    item = item,
                    onShareClick = {
                        selectedDecisionId = item.decisionId
                        showShareSheet = true
                    },
                    onUnshareClick = {
                        viewModel.unshareHistoryItem(item.decisionId)
                    },
                    onCardClick = {
                        val gson = Gson()
                        val json = gson.toJson(item)
                        val encodedJson = Uri.encode(json)
                        navController.navigate(Screen.HistoryDetail.createRoute(encodedJson))
                    },
                    onCommentClick = {
                        viewModel.getComments(item.decisionId)
                        showComments = true
                    }
                )
            }
        }
        }
    }
    
    if (showComments) {
        CommentsBottomSheet(
            onDismiss = { showComments = false },
            comments = comments,
            onSendComment = {},
            isHistoryPage = true
        )
    }

    if (showShareSheet) {
        AnswerShareSheet(
            onDismiss = { showShareSheet = false },
            value = uiState.shareNote,
            onValueChange = viewModel::onShareNoteChange,
            onClick = { viewModel.shareHistoryItem(selectedDecisionId) }
        )
    }
}

@Composable
fun HistoryCard(
    item: HistoryResponse, // Changed from HistoryItem
    onShareClick: () -> Unit,
    onUnshareClick: () -> Unit,
    onCardClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    val context = LocalContext.current
    val markwon = remember { Markwon.create(context) }


    val styledText = remember(item.answer) {
        markwon.toMarkdown(item.answer).toString()
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.surface_dark)),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorResource(id = R.color.gradient_start),
                                colorResource(id = R.color.gradient_end)
                            )
                        )
                    )
            )

            // --- İçerik ---
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Kategori ve Tarih
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = colorResource(id = R.color.primary_purple),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = calculateTimeAgo(item.createdDate), // Util function usage if available, else raw date
                        style = MaterialTheme.typography.labelSmall,
                        color = colorResource(id = R.color.text_muted)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Soru Başlığı
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (item.isPublic){
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Kullanıcı Notu: ${item.shareNote}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.primary_purple), // Soruyu senin mor renginle vurgulayalım
                        modifier = Modifier
                            .background(
                                color = colorResource(id = R.color.purple_overlay_20),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // AI Cevabı (Özet)
                Text(
                    text = "AI: $styledText", // Changed from aiAnswerPreview
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.text_secondary),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- Alt Aksiyon Alanı (Dinamik) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (item.isPublic) {
                        // DURUM 1: Zaten Paylaşılmış -> İstatistikleri Göster
                        Row(verticalAlignment = Alignment.CenterVertically ,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth() ) {
                            StatusBadge(text = "Yayında", color = colorResource(R.color.success)) // assuming success is tertiary

                            Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Favorite, null, tint = colorResource(R.color.white), modifier = Modifier.size(14.dp))
                                Text(" ${item.likeCount}", color = colorResource(R.color.white), fontSize = 12.sp) }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { onCommentClick() }
                            ) {
                                Icon(painterResource(id = R.drawable.comment), null, tint = colorResource(R.color.white), modifier = Modifier.size(14.dp))
                                Text(" ${item.commentCount}", color = colorResource(R.color.white), fontSize = 12.sp)
                            }

                            Button(
                                onClick = { onUnshareClick() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.error),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("Keşfet'ten Kaldır", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    } else {
                        // DURUM 2: Paylaşılmamış -> Sadece durumu göster
                         StatusBadge(text = "Sadece Sen", colorResource(R.color.white))
                         
                        Button(
                            onClick = { onShareClick() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.primary_purple),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.world),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Keşfet'te Paylaş", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
            }

        }
    }
}
}
@Composable
fun StatusBadge(text: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 11.sp,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}