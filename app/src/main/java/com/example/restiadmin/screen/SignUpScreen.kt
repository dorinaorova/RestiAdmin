@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.restiadmin.screen

import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.restiadmin.R
import com.example.restiadmin.data.User
import com.example.restiadmin.navigation.Screen
import com.example.restiadmin.viewmodel.SignUpViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

private var name  = mutableStateOf("")
private var email = mutableStateOf("")
private var phone = mutableStateOf("")
private var password = mutableStateOf("")
private var password2 = mutableStateOf("")
private var date = mutableStateOf("")
private var vm = SignUpViewModel()
private var enabled = mutableStateOf(name.value.isNotEmpty() && email.value.isNotEmpty() && phone.value.isNotEmpty() && password.value.isNotEmpty())


@Composable
fun SignUpScreen(navController: NavController){
    Box(modifier = Modifier.fillMaxSize()){
        val scroll = rememberScrollState(0)
        Header()
        Datas(scroll, navController)
    }
}
@Composable
private fun Header(){
    Box(modifier = Modifier
        .fillMaxSize()) {
        Image(
            painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Regisztráció",
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.headlineLarge,
                color = colorResource(id = R.color.primary_text),
                modifier = Modifier.padding(top = 30.dp, bottom = 60.dp)
            )
        }
    }
}

@Composable
private fun Datas(scroll : ScrollState, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )
        Column(
            modifier = Modifier
                .verticalScroll(scroll)
                .fillMaxSize()
                .background(
                    Color.White,
                    shape = RoundedCornerShape(size = 30.dp)
                )
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            DataField(
                "Teljes név",
                Icons.Rounded.AccountCircle,
                0,
                ImeAction.Next,
                VisualTransformation.None
            )
            DataField("Email", Icons.Rounded.Email, 1, ImeAction.Next, VisualTransformation.None)
            DataField(
                "Telefonszám",
                Icons.Rounded.Phone,
                2,
                ImeAction.Next,
                VisualTransformation.None
            )
            // TODO DatePicker()
            DataField(
                "Jelszó",
                Icons.Rounded.Lock,
                3,
                ImeAction.Next,
                PasswordVisualTransformation()
            )
            DataField(
                "Jelszó ismét",
                Icons.Rounded.Lock,
                4,
                ImeAction.Done,
                PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(50.dp))
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                val context = LocalContext.current

                Button(
                    onClick = {
//                        val df = SimpleDateFormat("yyyy.MM.dd")
//                        val user= User(0,name.value, email.value, phone.value, password.value,df.parse(date.value).time)
//                        vm.signUp(user,navController,context)
                        //navController.navigate(route = Screen.ProfileScreen.route)
                        Log.d("USER", email.value+" - "+password.value)
                        vm.signUp(User(0, name.value, email.value, phone.value, password.value, 0, "", "", ""), navController, context)
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_primary)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(bottom = 10.dp),
                    enabled = enabled.value //TODO

                ) {
                    Text(
                        text = "Sign Up",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

    }
}

@Composable
private fun DataField(text: String, icon: ImageVector, valueNum: Int, imeAction: ImeAction, visualTransformation: VisualTransformation){
    var value by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    enabled.value = name.value.isNotEmpty() && email.value.isNotEmpty() && password.value.isNotEmpty() && password.value == password2.value

    Text(text=text,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(start = 20.dp, top = 10.dp))

    BasicTextField(
        value = value,
        onValueChange = {value = it

            when (valueNum) {
                0 -> {
                    name.value = value
                }
                1 -> {
                    email.value=value
                }
                2 -> {
                    phone.value=value
                }
                3 -> {
                    password.value=value
                }
                else ->{
                    password2.value= value
                }
            }
        },
        textStyle = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.secondary_text)
        ),
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        visualTransformation = visualTransformation,
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            },
            onDone = {
                focusManager.clearFocus()
            }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(size = 16.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = colorResource(id = R.color.divider),
                        shape = RoundedCornerShape(size = 16.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colorResource(id = R.color.secondary_text),
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.width(width = 8.dp))
                innerTextField()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    MaterialTheme {
        SignUpScreen(navController = rememberNavController())
    }
}