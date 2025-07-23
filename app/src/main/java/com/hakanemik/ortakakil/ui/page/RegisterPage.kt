package com.hakanemik.ortakakil.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hakanemik.ortakakil.R


@Composable
fun RegisterPage(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.black))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        Image(
            painter = painterResource(id = R.drawable.ortak_akil_logo),
            contentDescription = "logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Yeni bir hesap oluştur",
            fontSize = 18.sp,
            color = colorResource(id = R.color.white)
        )

        Spacer(modifier = Modifier.height(25.dp))

        LoginTextFields(onValueChange = { name = it }, label = "İsim", value = name)
        Spacer(modifier = Modifier.height(15.dp))

        LoginTextFields(onValueChange = { surname = it }, label = "Soyisim", value = surname)
        Spacer(modifier = Modifier.height(15.dp))

        LoginTextFields(onValueChange = { email = it }, label = "E-mail", value = email)
        Spacer(modifier = Modifier.height(15.dp))

        LoginTextFields(onValueChange = { password = it }, label = "Şifre", value = password)
        Spacer(modifier = Modifier.height(15.dp))

        LoginTextFields(onValueChange = { confirmPassword = it }, label = "Şifre Tekrar", value = confirmPassword)

        Spacer(modifier = Modifier.height(30.dp))

        LoginButton(
            onClick = {},
            colorRes = R.color.purple_700,
            borderColor = R.color.purple_700,
            label = "Kayıt Ol"
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(
            onClick = {
                navController.navigate("login_page")
            },
            content = {
                Text(text = "Zaten hesabın var mı? Giriş Yap",
                    color = colorResource(id = R.color.white))
            }
        ) 
        Spacer(modifier = Modifier.height(50.dp))
        }


    }


