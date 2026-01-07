package com.hakanemik.ortakakil.ui.page

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.DiscoveryResponse
import com.hakanemik.ortakakil.entity.Enum.SnackbarType
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.ui.navigation.Screen
import com.hakanemik.ortakakil.ui.utils.CommentsBottomSheet
import com.hakanemik.ortakakil.ui.utils.DateUtils.calculateTimeAgo
import com.hakanemik.ortakakil.viewmodel.DiscoveryPageViewModel


@Composable
fun DiscoveryPage(
    navController: NavHostController,
    onShowSnackbar: (String, SnackbarType) -> Unit,
    viewModel: DiscoveryPageViewModel = hiltViewModel()
) {
    val deviceSize = currentDeviceSizeHelper()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showComments by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.refreshFeed()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = colorResource(id = R.color.background_dark)
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
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
                DiscoveryCard(
                    item = item,
                    onClick = {
                        val gson = Gson()
                        val json = gson.toJson(item)
                        val encodedJson = Uri.encode(json)
                        navController.navigate(Screen.DiscoveryDetail.createRoute(encodedJson))
                    },
                    onLikeClick = {
                        viewModel.toggleLike(item.decisionId)
                    },
                    onCommentClick = {
                        viewModel.getComments(item.decisionId)
                        showComments = true
                    }
                )
            }
        }
    }
    if (showComments) {
        CommentsBottomSheet(
            onDismiss = { showComments = false },
            comments = uiState.selectedComments, 
            onSendComment = { text -> viewModel.addComment(text) }
        )
    }
}
@Composable
fun DiscoveryCard(
    item: DiscoveryResponse, // Parametreleri tek tek geçmek yerine DTO geçmek daha temizdir
    onClick: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }, // Kartın tamamı tıklanabilir
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.surface_dark) // Dark tema uyumu
        ),
        border = BorderStroke(1.dp, colorResource(id = R.color.divider))
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Sol taraftaki Gradient Şerit ve Profil Resmi
            Column(
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorResource(id = R.color.gradient_start),
                                colorResource(id = R.color.gradient_end)
                            )
                        )
                    )
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = if (item.userPhotoUrl.isNullOrEmpty()) R.drawable.user else item.userPhotoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.user),
                    fallback = painterResource(R.drawable.user)
                )
            }

            // Sağ taraftaki İçerik
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text(
                    text = item.userFullName,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.text_primary)
                )
                Text(
                    text = calculateTimeAgo(item.createdDate),
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.text_muted)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Soru: ${item.title}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.primary_purple), // Soruyu senin mor renginle vurgulayalım
                    modifier = Modifier
                        .background(
                            color = colorResource(id = R.color.purple_overlay_10),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Kullanıcının Paylaşım Notu
                Text(
                    text = item.shareNote,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = colorResource(id = R.color.text_primary),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // AI Cevabı (Kısıtlanmış hali)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = colorResource(id = R.color.surface_light),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Ortak Akıl: ${item.answer}",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.text_secondary),
                        maxLines = 3, // Uzun cevabı kesiyoruz
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Devamını oku...",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.primary_purple),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Alt Etkileşim Çubuğu
                Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Favorite,
                            null,
                            tint = if (item.isLikedByMe) colorResource(R.color.error) else Color.White,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { onLikeClick() }
                        )
                    Text(" ${item.likeCount}", color = Color.White, fontSize = 13.sp)

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        painterResource(R.drawable.comment),
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp).clickable { onCommentClick() }
                    )
                    Text(" ${item.commentCount}", color = Color.White, fontSize = 13.sp)
                }
            }
        }
    }
}
