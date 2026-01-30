package com.hakanemik.ortakakil.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hakanemik.ortakakil.R

@Composable
fun OnboardingScreen2(onNext: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(start = 24.dp, top = 32.dp, end = 24.dp, bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Geri butonu
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            TextButton(onClick = onBack) {
                Text("Geri", color = colorResource(id = R.color.text_muted))
            }
        }

        Image(
            painter = painterResource(id = R.drawable.chip,), // Özellikleri temsili ikon
            contentDescription = "Özellikler",
            modifier = Modifier.size(200.dp)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Yapay Zeka Destekli Kararlar",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.white),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Yapay zekamız sorularına en doğru ve tarafsız cevapları bulmana yardımcı olur.",
                fontSize = 16.sp,
                color = colorResource(id = R.color.text_muted),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.primary_purple))
        ) {
            Text("Devam Et", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colorResource(id = R.color.white))
        }
    }
}