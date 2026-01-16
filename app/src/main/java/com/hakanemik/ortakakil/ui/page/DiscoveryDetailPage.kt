package com.hakanemik.ortakakil.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.DiscoveryResponse
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.hakanemik.ortakakil.viewmodel.DiscoveryDetailViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hakanemik.ortakakil.ui.utils.CommentsBottomSheet
import com.hakanemik.ortakakil.ui.utils.DateUtils.calculateTimeAgo
import com.hakanemik.ortakakil.ui.utils.ReportBottomSheet
import io.noties.markwon.Markwon

@Composable
fun DiscoveryDetailPage(
    item: DiscoveryResponse,
    viewModel: DiscoveryDetailViewModel = hiltViewModel()
) {
    var isLiked by remember { mutableStateOf(item.isLikedByMe) }
    var likeCount by remember { mutableIntStateOf(item.likeCount) }
    var commentCount by remember { mutableIntStateOf(item.commentCount) }
    var showComments by remember { mutableStateOf(false) }
    var showReportSheet by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val markwon = remember { Markwon.create(context) }


    val styledText = remember(item.answer) {
        markwon.toMarkdown(item.answer).toString()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_dark))
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // User Info Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = if (item.userPhotoUrl.isNullOrEmpty()) R.drawable.user else item.userPhotoUrl,
                contentDescription = null,
                modifier = Modifier.size(50.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.user),
                fallback = painterResource(R.drawable.user)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = item.userFullName,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.category.uppercase(),
                        style = typography.labelSmall,
                        color = colorResource(id = R.color.primary_purple),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(3.dp).background(colorResource(id = R.color.text_muted), CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = calculateTimeAgo(item.createdDate),
                        color = colorResource(id = R.color.text_muted),
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Box( modifier = Modifier
            .fillMaxWidth()) {
            Column {
                // AI Question Section
                Text(
                    text = "Soru:",
                    color = colorResource(id = R.color.text_muted),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp,
                    lineHeight = 24.sp
                )
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        // User's Share Note
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.surface_dark), RoundedCornerShape(12.dp))
                .border(1.dp, colorResource(id = R.color.divider), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Column {
                 Text(
                    text = "Kullanıcı Notu",
                    color = colorResource(id = R.color.primary_purple),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.shareNote,
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // AI Answer Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.purple_overlay_10), RoundedCornerShape(12.dp))
                .border(1.dp, colorResource(id = R.color.primary_purple), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
             Column {
                 Text(
                    text = "Ortak Akıl'ın Önerisi",
                    color = colorResource(id = R.color.primary_purple),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = styledText,
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Interaction Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
             Row(verticalAlignment = Alignment.CenterVertically,
                 modifier = Modifier.clickable {
                     isLiked = !isLiked
                     likeCount = if(isLiked) likeCount + 1 else likeCount - 1
                     viewModel.toggleLike(item.decisionId)
                 }
             ) {
                Icon(
                    Icons.Default.Favorite,
                    null,
                    tint = if(isLiked) Color.Red else Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "$likeCount Beğeni", color = Color.White)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    viewModel.getComments(item.decisionId)
                    showComments = true
                }
            ) {
                Icon(
                    painterResource(id = R.drawable.comment),
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "$commentCount Yorum", color = Color.White)
            }
        }

//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(
//                    Icons.Default.Share,
//                    null,
//                    tint = Color.White,
//                    modifier = Modifier.size(24.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(text = "Paylaş", color = Color.White)
//            }
        }

    if (showComments) {
        CommentsBottomSheet(
            onDismiss = { showComments = false },
            comments = uiState.selectedComments,
            onSendComment = { text ->
                viewModel.addComment(text, item.decisionId)
                commentCount++
            }
        )
    }

    if (showReportSheet) {
        ReportBottomSheet(
            onDismiss = { showReportSheet = false },
            onReasonSelected = { reason ->
                viewModel.reportContent(item.decisionId, reason.reason)
                showReportSheet = false
            }
        )
    }
    }
