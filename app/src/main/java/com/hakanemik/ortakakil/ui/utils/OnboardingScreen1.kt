package com.hakanemik.ortakakil.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hakanemik.ortakakil.R

@Composable
fun OnboardingScreen1(onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.ortak_akil_logo), // Kendi logon
            contentDescription = "Ortak Akıl Logo",
            modifier = Modifier.size(180.dp).clip(
                RoundedCornerShape(36.dp)
        )
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Ortak Akıl'a Hoş Geldin!",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colorResource(id = R.color.white),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Birlikte daha iyi kararlar al, topluluğun gücünü keşfet.",
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
            Text("Başlayalım", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colorResource(id = R.color.white))
        }
    }
}