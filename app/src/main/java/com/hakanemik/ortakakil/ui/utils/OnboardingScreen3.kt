package com.hakanemik.ortakakil.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hakanemik.ortakakil.R

@Composable
fun OnboardingScreen3(
    onLoginClick: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onRegisterClick: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Geri butonu
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            TextButton(onClick = onBack) {
                Text("Geri", color = colorResource(id = R.color.text_muted))
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.join,), // Özellikleri temsili ikon
                contentDescription = "Özellikler",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Hemen Katıl",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Topluluğa katılarak akıllı kararlar almaya başla!",
                fontSize = 16.sp,
                color = colorResource(id = R.color.text_muted),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            // Google ile Giriş Butonu (Öncelikli)
            Button(
                onClick = onGoogleSignIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White) // Google butonu genelde beyaz olur
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google), // Google logon
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("Google ile Devam Et", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // E-posta ile Kayıt Ol
            OutlinedButton(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                border = BorderStroke(2.dp, colorResource(id = R.color.primary_purple)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = colorResource(id = R.color.primary_purple)
                )
            ) {
                Text("E-posta ile Kayıt Ol", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Zaten Hesabın Var mı? Giriş Yap
            TextButton(onClick = onLoginClick) {
                Text(
                    text = "Zaten hesabın var mı? ",
                    color = colorResource(id = R.color.text_muted),
                    fontSize = 16.sp
                )
                Text(
                    text = "Giriş Yap",
                    color = colorResource(id = R.color.primary_purple),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}