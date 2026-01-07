package com.hakanemik.ortakakil.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding 
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hakanemik.ortakakil.R
import com.hakanemik.ortakakil.entity.CommentResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    onDismiss: () -> Unit,
    comments: List<CommentResponse>,
    onSendComment: (String) -> Unit,
    isHistoryPage: Boolean = false
) {
    var commentText by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colorResource(id = R.color.surface_dark),
        scrimColor = Color.Black.copy(alpha = 0.5f),
        windowInsets = WindowInsets.ime // Klavye açıldığında bottom sheet'in yukarı kaymasını sağlar
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.85f) // Biraz daha arttırdık
                .padding(horizontal = 20.dp)
                .navigationBarsPadding() // Sanal tuşlar için padding
        ) {
            Text(
                text = "Yorumlar (${comments.size})",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Yorum Listesi
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(
                    count = comments.size,
                    key = { index -> comments[index].id }
                ) {it->
                    val item= comments[it]
                    CommentItem(item)
                    Divider(color = colorResource(id = R.color.divider), thickness = 0.5.dp)
                }
            }

            if (!isHistoryPage){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .imePadding(), // Klavye açıldığında yukarı kayar
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        placeholder = { Text("Yorumunuzu yazın...", color = colorResource(id = R.color.text_muted)) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = colorResource(id = R.color.primary_purple),
                            focusedBorderColor = colorResource(id = R.color.primary_purple),
                            unfocusedBorderColor = colorResource(id = R.color.border_default)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(
                        onClick = {
                            if (commentText.isNotBlank()) {
                                onSendComment(commentText)
                                commentText = ""
                            }
                        },
                        modifier = Modifier.background(colorResource(id = R.color.primary_purple), CircleShape)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Gönder", tint = Color.White)
                    }
                }
            }

        }
    }
}
@Composable
fun CommentItem(comment: CommentResponse) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = if (comment.userProfilePicture.isNullOrEmpty()) R.drawable.user else comment.userProfilePicture,
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.user),
            fallback = painterResource(R.drawable.user)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = comment.userFullName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = colorResource(id = R.color.text_primary)
            )
            Text(
                text = comment.content,
                fontSize = 14.sp,
                color = colorResource(id = R.color.text_secondary),
                lineHeight = 20.sp
            )
            Text(
                text = comment.createdDate, // createdDate'den dönüştürülebilir
                fontSize = 11.sp,
                color = colorResource(id = R.color.text_muted),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}