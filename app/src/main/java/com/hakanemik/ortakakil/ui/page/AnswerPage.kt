package com.hakanemik.ortakakil.ui.page

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.hakanemik.ortakakil.entity.AnswerUiEvent
import com.hakanemik.ortakakil.entity.Enum.SnackbarType
import com.hakanemik.ortakakil.helper.currentDeviceSizeHelper
import com.hakanemik.ortakakil.helper.responsive
import com.hakanemik.ortakakil.ui.utils.AnswerShareSheet
import com.hakanemik.ortakakil.viewmodel.AnswerPageViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AnswerPage(
    navController: NavController,
    onShowSnackbar: (String, SnackbarType) -> Unit,
    question: String?,
    viewModel: AnswerPageViewModel = hiltViewModel()
) {
    LaunchedEffect(question) {
        question?.let {
            viewModel.loadAnswer(it,"Genel")
        }
    }
    val deviceSize = currentDeviceSizeHelper()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showSheet by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is AnswerUiEvent.ShareSuccess -> {
                    showSheet = false
                    onShowSnackbar("Sorunuz paylaşıldı", SnackbarType.SUCCESS)
                }
                is AnswerUiEvent.ShareError -> {
                    onShowSnackbar("Paylaşılırken bir hata oluştu",SnackbarType.ERROR)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.background_dark),
                        colorResource(id = R.color.background_dark).copy(alpha = 0.95f),
                        colorResource(id = R.color.surface_dark).copy(alpha = 0.4f)
                    )
                )
            )
            .padding(horizontal = 16.dp)
            .padding(top = 20.dp, bottom = 35.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Sorunuz:",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.text_secondary),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = question ?: "Soru yüklenıyor...",
                        color = colorResource(id = R.color.text_primary),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 32.sp
                    )
                }
            }

            // Cevap Bölümü
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.surface_light)
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorResource(id = R.color.purple_overlay_20),
                                colorResource(id = R.color.purple_overlay_10)
                            )
                        )
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Ortak Akıl'ın Önerisi",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorResource(id = R.color.text_primary)
                            )

                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (uiState.isLoading) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    color = colorResource(id = R.color.primary_purple),
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    "Cevap hazırlanıyor...",
                                    color = colorResource(id = R.color.text_secondary),
                                    fontSize = 14.sp
                                )
                            }
                        } else {
                            Text(
                                text = uiState.answer,
                                color = colorResource(id = R.color.text_primary),
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
        Column {
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
            ) {
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White,
                            Color.Transparent
                        )
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
            Spacer(modifier = Modifier.height(8.dp.responsive(8.dp, 10.dp, 8.dp, deviceSize)))
            Button(
                onClick = { showSheet= true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.primary_purple)
            ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.world),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = colorResource(id = R.color.text_primary)
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    "Topluluğun Fikrini Al",
                    color = colorResource(id = R.color.text_primary),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text("Bu soruyu diğer kullanıcılara göndererek farklı bakış açıları kazanın",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.text_primary).copy(alpha = 0.8f)
            )
        }
    }
    if (showSheet){
        AnswerShareSheet(
            onDismiss = { showSheet = false },
            value = uiState.shareNote,
            onValueChange = viewModel::onValueChange,
            onClick = viewModel::onClick
        )
    }
}


