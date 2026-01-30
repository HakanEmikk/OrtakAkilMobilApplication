package com.hakanemik.ortakakil.ui.utils

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hakanemik.ortakakil.R
import androidx.core.net.toUri

@Composable
fun OnboardingScreen3(
    onLoginClick: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onRegisterClick: () -> Unit,
    onBack: () -> Unit,
    isLoading: Boolean = false
) {
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

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.join), // Özellikleri temsili ikon
                contentDescription = "Özellikler",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Hemen Katıl",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Katıl ve akıllı kararlar almaya başla!",
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
                if (isLoading) {
                    Spacer(modifier = Modifier.width(12.dp))
                    CircularProgressIndicator(color = colorResource(R.color.primary_purple), strokeWidth = 3.dp, modifier = Modifier.size(24.dp))
                }
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

            Spacer(modifier = Modifier.height(16.dp))

            // Terms and Privacy Policy
            TermsAndPrivacyText()
        }
    }
}

@Composable
fun TermsAndPrivacyText() {
    val context = LocalContext.current
    
    // Placeholder URLs - Gerçek URL'lerinizi buraya ekleyebilirsiniz
    val termsUrl = ""
    val privacyUrl = ""
    
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Gray, fontSize = 12.sp)) {
            append("Devam ederek ")
        }
        
        pushStringAnnotation(tag = "TERMS", annotation = termsUrl)
        withStyle(style = SpanStyle(
            color = colorResource(id = R.color.primary_purple),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )) {
            append("Hizmet Şartları")
        }
        pop()
        
        withStyle(style = SpanStyle(color = Color.Gray, fontSize = 12.sp)) {
            append(" ve ")
        }
        
        pushStringAnnotation(tag = "PRIVACY", annotation = privacyUrl)
        withStyle(style = SpanStyle(
            color = colorResource(id = R.color.primary_purple),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )) {
            append("Gizlilik Politikası")
        }
        pop()
        
        withStyle(style = SpanStyle(color = Color.Gray, fontSize = 12.sp)) {
            append("'nı kabul etmiş olursunuz.")
        }
    }
    
    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    val intent = Intent(Intent.ACTION_VIEW, annotation.item.toUri())
                    context.startActivity(intent)
                }
            
            annotatedString.getStringAnnotations(tag = "PRIVACY", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    val intent = Intent(Intent.ACTION_VIEW, annotation.item.toUri())
                    context.startActivity(intent)
                }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        style = TextStyle(
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    )
}