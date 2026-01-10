package com.hakanemik.ortakakil.ui.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.HistoryResponse
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import com.hakanemik.ortakakil.viewmodel.HistoryDetailViewModel
import com.hakanemik.ortakakil.ui.utils.CommentsBottomSheet
import com.hakanemik.ortakakil.ui.utils.DateUtils.calculateTimeAgo
import com.hakanemik.ortakakil.ui.utils.AnswerShareSheet
import com.hakanemik.ortakakil.entity.AnswerUiEvent
import com.hakanemik.ortakakil.entity.Enum.SnackbarType

@Composable
fun HistoryDetailPage(
    navController: NavHostController,
    item: HistoryResponse,
    onShowSnackbar: (String, SnackbarType) -> Unit,
    viewModel: HistoryDetailViewModel = hiltViewModel()
) {
    val comments by viewModel.comments.collectAsState()
    val shareNote by viewModel.shareNote.collectAsState()
    
    LaunchedEffect(item.decisionId) {
        viewModel.getComments(item.decisionId)
    }

    var showComments by remember { mutableStateOf(false) }
    var showShareSheet by remember { mutableStateOf(false) }

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
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_dark))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp)
        ) {
            // 1. Soru Başlığı ve Tarih
            item {
                Text(
                    text = item.title ,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorResource(R.color.white),
                    lineHeight = 34.sp
                )
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    Text(
                        text = calculateTimeAgo(item.createdDate) ,
                        fontSize = 13.sp,
                        color = colorResource(id = R.color.text_muted),
                    )
                    StatusBadge(
                    text = if (item.isPublic ) "Yayında" else "Gizli",
                    color = if (item.isPublic ) colorResource(R.color.success) else colorResource(R.color.text_muted)
                )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (item.isPublic ){
                item {
                    Text(
                        text = "Senin Notun",
                        color = colorResource(id = R.color.primary_purple),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = item.shareNote,
                        color = colorResource(id = R.color.text_primary),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }


            // 3. AI Cevap Kartı (Görselde verdiğin uzun cevap yapısı)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.surface_dark)),
                    border = BorderStroke(1.dp, colorResource(id = R.color.primary_purple).copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Ortak Akıl'ın Önerisi",
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.text_primary)
                            )
                        }

                        Divider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = colorResource(id = R.color.divider)
                        )

                        // AI'nın uzun cevabı burada tam liste/metin olarak gösterilir
                        Text(
                            text = item.answer ,
                            color = colorResource(id = R.color.text_secondary),
                            fontSize = 15.sp,
                            lineHeight = 26.sp
                        )
                    }
                }
            }

            // 4. Paylaşım Aksiyonu (Eğer paylaşılmadıysa)
            if (!item.isPublic ) {
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { showShareSheet = true },
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
                }
            }
            else {
                item{
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically,

                            ) {
                            Icon(
                                Icons.Default.Favorite,
                                null,
                                tint = colorResource(R.color.white),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "${item.likeCount} Beğeni", color = colorResource(R.color.white))
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { showComments = true }
                        ) {
                            Icon(
                                painterResource(id = R.drawable.comment),
                                null,
                                tint = colorResource(R.color.white),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "${item.commentCount} Yorum", color = colorResource(R.color.white))
                        }
                    }
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
            value = shareNote,
            onValueChange = viewModel::onShareNoteChange,
            onClick = { viewModel.shareHistoryItem(item.decisionId) }
        )
    }
}