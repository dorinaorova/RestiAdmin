package com.example.restiadmin.screen


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.restiadmin.R
import com.example.restiadmin.data.MenuItem
import com.example.restiadmin.data.Restaurant
import com.example.restiadmin.data.TypeEnum
import com.example.restiadmin.navController
import com.example.restiadmin.navigation.Screen
import com.example.restiadmin.screen.navbar.NavBar
import com.example.restiadmin.ui.theme.RestiAdminTheme
import com.example.restiadmin.viewmodel.ProfileViewModel

private var vm = ProfileViewModel()
private var name  = mutableStateOf("")
private var email = mutableStateOf("")
private var phone = mutableStateOf("")
private var address = mutableStateOf("")
private var open = mutableStateOf("")
private var close = mutableStateOf("")
private var enabled = mutableStateOf(name.value.isNotEmpty() )

@Composable
fun ProfileScreen(navController: NavController){
    val context = LocalContext.current
    LaunchedEffect(Unit, block ={
        vm.fetchDatas(context)
    } )

    val openDialog = remember { mutableStateOf(false) }

    when{
        openDialog.value -> {
            NewRestaurantDialog(
                onDismissRequest = { openDialog.value = false },
            )
        }
    }

    Scaffold(
        content = {paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)){
                Column {
                    UserInfo()
                    if(vm.restaurantExist){
                        RestaurantInfo()
                    }else {
                        Row(modifier= Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center){
                            Button(
                                onClick = {openDialog.value = true},
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_primary)),
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Text(text = "Étterem regisztrálása")
                            }
                        }

                    }
                }
            }
        },
        bottomBar ={ NavBar(navController)}
    )
}

@Composable
private fun NewRestaurantDialog(onDismissRequest: () -> Unit){
    val context = LocalContext.current
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(8.dp)
                .background(colorResource(id = R.color.light_primary)),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Új étterem",
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                DataField("Név",0, ImeAction.Next, Icons.Rounded.AccountCircle)
                DataField("Email",1, ImeAction.Next, Icons.Rounded.MailOutline)
                DataField("Telefonszám",2, ImeAction.Next, Icons.Rounded.Phone)
                DataField("Cím",3, ImeAction.Next, ImageVector.vectorResource(R.drawable.baseline_business_24))
                DataField("Nyitás",4, ImeAction.Next, ImageVector.vectorResource(R.drawable.baseline_access_time_filled_24))
                DataField("Zárás",5, ImeAction.Done, ImageVector.vectorResource(R.drawable.baseline_access_time_24))

                Button(onClick = {
                    vm.save(Restaurant(0, name.value, email.value, phone.value, address.value, open.value.toInt(), close.value.toInt(), null, null, null, 0),context)
                    onDismissRequest()
                                 },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_primary)),
                ) {
                    Text(text="Mentés")
                }

            }
        }
    }
}

@Composable
private fun DataField(text: String,valueNum: Int, imeAction: ImeAction, icon: ImageVector){
    var value by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight=FontWeight.Bold,
            modifier = Modifier.padding(start = 20.dp, top = 10.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = {
                value = it
                enabled.value = name.value.isNotEmpty()
                when (valueNum) {
                    0 -> {
                        name.value = value
                    }

                    1 -> {
                        email.value = value
                    }

                    2 -> {
                        phone.value = value
                    }

                    3 -> {
                        address.value = value
                    }

                    4 -> {
                        open.value = value
                    }

                    else -> {
                        close.value = value
                    }
                }
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(id = R.color.secondary_text)
            ),
            keyboardOptions = KeyboardOptions(imeAction = imeAction),
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
                        .padding(horizontal = 30.dp, vertical =6.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
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
}

@Composable
fun UserInfo(){
    Row(modifier= Modifier
        .fillMaxWidth()
        .background(colorResource(id = R.color.primary))
        .padding(vertical = 8.dp)){
        Image(
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .clip(shape = CircleShape),
            painter = painterResource(id = R.drawable.train),
            contentDescription = "Profile picture"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically)
        {
            Column(modifier = Modifier.padding(start = 16.dp, top = 5.dp)) {
                Text(
                    text = vm.user.name,
                    style = TextStyle(
                        fontSize = 22.sp,
                        color = colorResource(id = R.color.primary_text)
                    ),
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 5.dp),
                    text = vm.user.email,
                    style = TextStyle(color = colorResource(id = R.color.primary_text)),
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 5.dp),
                    text = vm.user.phone?: "",
                    style = TextStyle(color = colorResource(id = R.color.primary_text))
                )
                if(vm.restaurantExist){
                    Text(
                        modifier = Modifier.padding(start = 8.dp, top = 5.dp),
                        text = vm.restaurant.name,
                        style = TextStyle(color = colorResource(id = R.color.primary_text))
                    )
                }
                else{
                    Text(text = AnnotatedString("Find restaurant!") )
                }
            }
        }
    }
}

@Composable
fun RestaurantInfo(){
        Column {
            Column{
                val scroll = rememberScrollState(0)
                Column(modifier = Modifier
                    .verticalScroll(scroll)
                    .fillMaxWidth()
                    .background(
                        colorResource(id = R.color.white),
                        shape = RoundedCornerShape(size = 30.dp)
                    )){
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Text(
                            text=vm.restaurant.name,
                            style= MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = CreateOpenString(),
                                modifier = Modifier.padding(horizontal = 30.dp)
                            )
                            Row(modifier = Modifier
                                .height(18.dp)
                                .padding(top = 8.dp)) {
                                if (vm.restaurant.types != null) {
                                    if (vm.restaurant.types!!.contains(TypeEnum.VEGAN)) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.baseline_filter_vintage_24),
                                            contentDescription = null,
                                            tint = colorResource(id = R.color.secondary_text),
                                            modifier = Modifier.padding(horizontal = 5.dp)
                                        )
                                    }
                                    if(vm.restaurant.types!!.contains(TypeEnum.PET)) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.baseline_pets_24),
                                            contentDescription = null,
                                            tint = colorResource(id = R.color.secondary_text),
                                            modifier = Modifier.padding(horizontal = 5.dp)
                                        )
                                    }
                                    if(vm.restaurant.types!!.contains(TypeEnum.CAFE)) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.baseline_local_cafe_24) ,
                                            contentDescription = null,
                                            tint = colorResource(id = R.color.secondary_text),
                                            modifier = Modifier.padding(horizontal = 5.dp)
                                        )
                                    }
                                    if(vm.restaurant.types!!.contains(TypeEnum.BAR)) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.baseline_local_bar_24),
                                            contentDescription = null,
                                            tint = colorResource(id = R.color.secondary_text),
                                            modifier = Modifier.padding(horizontal = 5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Row(
                                modifier = Modifier.padding(start = 30.dp,top = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.baseline_map_24),
                                    contentDescription = null
                                )
                                Text(
                                    text = vm.restaurant.address,
                                    modifier = Modifier.padding(start=10.dp)
                                )
                            }
                            Row(
                                modifier = Modifier.padding(start = 30.dp, top = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Rounded.Phone,
                                    contentDescription = null
                                )
                                Text(
                                    text = vm.restaurant.phone,
                                    modifier = Modifier.padding(start=10.dp)
                                )
                            }
                            Row(
                                modifier = Modifier.padding(start = 30.dp,top = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Rounded.Email,
                                    contentDescription = null
                                )
                                Text(
                                    text = vm.restaurant.email,
                                    modifier = Modifier.padding(start=10.dp)
                                )
                            }
                        }
                    }
                    if(vm.restaurant.menu_Food != null) {
                        MenuRow(vm.restaurant.menu_Food!!, "Étlap", navController)
                    }
                    if(vm.restaurant.menu_Drink!=null) {
                        MenuRow(vm.restaurant.menu_Drink!!, "Itallap", navController)
                    }
                }
            }
        }
}

@Composable
fun MenuRow(list: List<MenuItem>, title: String, navController: NavController){
    Canvas(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(top = 20.dp)) {
        drawLine(
            color = Color.Black,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
        )
    }
    Row(modifier = Modifier
        .padding(top = 35.dp, bottom = 10.dp, start = 20.dp, end = 40.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text=title,
            style= MaterialTheme.typography.headlineMedium,
            color = colorResource(id = R.color.secondary_text))
        val route = Screen.MenuScreen.route+"/"+title
        IconButton(onClick = {navController.navigate(route = route)},
            modifier = Modifier
                .size(10.dp)
                .background(
                    colorResource(id = R.color.light_primary), shape = CircleShape
                )) {
            Icon(
                Icons.Rounded.ArrowForward,
                contentDescription = null
            )
        }
    }
    LazyRow{
        items(CreateList(list)){
                item -> MenuListItem(item = item)
        }
    }
}
@Composable
private fun MenuListItem(item: MenuItem){
    Box(modifier = Modifier
        .height(150.dp)
        .width(200.dp)
        .padding(10.dp)
        .background(
            colorResource(id = R.color.light_primary),
            shape = RoundedCornerShape(size = 16.dp)
        )){
        Text(text = item.name,
            color = colorResource(id = R.color.secondary_text),
            style=MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private fun CreateList(menuBase: List<MenuItem>):List<MenuItem>{
    if(menuBase.size>5) {
        val menu = arrayListOf<MenuItem>()
        for (i in 0..5) {
            menu.add(menuBase[i])
        }
        return menu
    }else return menuBase
}

private fun CreateOpenString():String{
    val openMinutes = if(vm.restaurant.open%100 == 0) "00" else (vm.restaurant.open%100).toString()
    val openHours = if(vm.restaurant.open/100 < 10) "0${vm.restaurant.open/100}" else (vm.restaurant.open/100).toString()
    val closeMinutes = if(vm.restaurant.close%100 == 0) "00" else (vm.restaurant.close%100).toString()
    val closeHours = if(vm.restaurant.close/100 < 10) "0${vm.restaurant.close/100}" else (vm.restaurant.close/100).toString()
    val openStr= "$openHours:$openMinutes - $closeHours:$closeMinutes"
    return openStr
}

@Preview(showBackground = true)
@Composable
fun ProfilePreviewScreen() {
    RestiAdminTheme {
        ProfileScreen(navController = rememberNavController())
    }
}