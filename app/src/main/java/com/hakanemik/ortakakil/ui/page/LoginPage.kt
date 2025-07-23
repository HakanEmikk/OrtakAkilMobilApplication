package com.hakanemik.ortakakil.ui.page


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hakanemik.ortakakil.R

@Composable
fun LoginPage(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.black))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()), // Kaydırılabilirlik
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(120.dp))

        Image(
            painter = painterResource(id = R.drawable.ortak_akil_logo),
            contentDescription = "logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Akıllı kararlar ver, topluluğa katıl",
            fontSize = 15.sp,
            color = colorResource(id = R.color.white)
        )

        Spacer(modifier = Modifier.height(30.dp))

        LoginTextFields(onValueChange = { email = it }, label = "E-mail", value = email)

        Spacer(modifier = Modifier.height(21.dp))

        LoginTextFields(onValueChange = { password = it }, label = "Şifre", value = password)

        Spacer(modifier = Modifier.height(15.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(checkedColor = colorResource(id = R.color.purple_700))
                )
                Text(
                    text = "Beni Hatırla",
                    color = colorResource(id = R.color.white),
                    fontSize = 14.sp
                )
            }

            Text(
                text = "Şifremi Unuttum",
                fontSize = 14.sp,
                color = colorResource(id = R.color.white)
            )
        }

        Spacer(modifier = Modifier.height(35.dp))

        LoginButton(
            onClick = { /* Giriş */ },
            colorRes = R.color.purple_700,
            borderColor = R.color.purple_700,
            label = "Giriş Yap"
        )

        Spacer(modifier = Modifier.height(17.dp))

        LoginButton(
            onClick = {
                navController.navigate("register_page")
            },
            colorRes = R.color.black,
            borderColor =  R.color.dark_gray,
            label = "Kayıt Ol",

        )

        Spacer(modifier = Modifier.height(100.dp))
    }
}



@Composable
fun LoginButton(
    onClick: () -> Unit,
    colorRes: Int,
    borderColor: Int = colorRes,
    label: String,
    textColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(2.dp, color = colorResource(id = borderColor),
                shape = RoundedCornerShape(10.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = colorRes)),
        shape = RoundedCornerShape(10.dp)

    ) {
        Text(text = label, color = textColor, fontSize = 16.sp)
    }
}


@Composable
fun LoginTextFields(
    onValueChange: (String) -> Unit,
    label: String,
    value: String
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = Color.Gray) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(10.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.dark_gray), // Hafif koyu gri
            unfocusedContainerColor = colorResource(id = R.color.dark_gray),
            disabledContainerColor = colorResource(id = R.color.dark_gray),
            focusedIndicatorColor = Color.Transparent, // Alt çizgi yok
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedLabelColor = Color.LightGray,
            cursorColor = colorResource(id = R.color.white),
            focusedTextColor = colorResource(id = R.color.white),
            unfocusedTextColor =colorResource(id = R.color.white)
        ),
        shape = RoundedCornerShape(10.dp)
    )
}

