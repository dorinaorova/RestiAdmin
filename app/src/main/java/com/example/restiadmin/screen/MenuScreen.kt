package com.example.restiadmin.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.restiadmin.R
import com.example.restiadmin.data.MenuItem
import com.example.restiadmin.navigation.Screen
import com.example.restiadmin.screen.navbar.NavBar
import com.example.restiadmin.ui.theme.RestiAdminTheme
import com.example.restiadmin.viewmodel.MenuViewModel

private var menuTitle =""
private var vm = MenuViewModel()

@Composable
fun MenuScreen(navController: NavController, title: String?){
    val context = LocalContext.current
    LaunchedEffect(Unit, block ={
        vm.getMenu(menuTitle.contentEquals("Étlap"), context)
        vm.getRequests(menuTitle.contentEquals("Étlap"), context)
    } )

    menuTitle = title!!

    val openDialog = remember { mutableStateOf(false) }

    when{
        openDialog.value -> {
            NewItemDialog(
                onDismissRequest = { openDialog.value = false },
            )
        }
    }

    Scaffold(
        content = {paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                Column{
                    Header(menuTitle)
                    MenuList(title, navController)
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { openDialog.value=true },
                    containerColor = colorResource(id = R.color.dark_primary),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add",
                    tint = colorResource(id = R.color.light_primary),)
            }
        },
        bottomBar ={
            NavBar(navController)
        }
    )


}



@Composable
fun ChangeMenu(title: String?, navController: NavController){

    var newTitle=""
    if(title=="Étlap"){
        newTitle="Itallap"
    }
    else{
        newTitle="Étlap"
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        ){
        Button(
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_primary)),
            modifier = Modifier
                .wrapContentWidth()
                .height(40.dp),
            onClick = {
                navController.navigate(route = Screen.MenuScreen.route+"/${newTitle}")
            }) {
            Text(text = newTitle)
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_arrow_forward_24),
                contentDescription = null,
                tint = colorResource(id = R.color.light_primary),
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
    }
}

@Composable
private fun MenuList(title: String?, navController: NavController){
    val context = LocalContext.current
    val color = colorResource(id = R.color.dark_primary)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.light_primary))
    ) {
        ChangeMenu(title, navController)
        if(vm.admin(context)) {
            Box(modifier = Modifier.fillMaxHeight(0.4f)) {
                LazyColumn {
                    items(vm.requests) { item ->
                        MenuListItem(item.item, true)
                    }
                }
            }
        }
        if(vm.requests.isNotEmpty() && vm.admin(context)) {
            Box(modifier= Modifier.fillMaxHeight(0.1f)) {
                Canvas(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(vertical = 20.dp)
                ) {
                    drawLine(
                        color = color,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                    )
                }
            }
        }
        LazyColumn {
            items(vm.menu){ item ->
                MenuListItem(item, false)
            }
        }
    }
}

@Composable
private fun MenuListItem(item: MenuItem, request: Boolean){
    var context = LocalContext.current
    Box (modifier = Modifier
        .padding(vertical = 12.dp, horizontal = 8.dp)
        .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
        .fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,) {
            Column(
                modifier = Modifier.padding(8.dp).wrapContentWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name+" - ",
                        modifier = Modifier.padding(10.dp),
                        fontSize = 24.sp
                    )
                    Text(
                        text = item.price.toString() + " Ft",
                        modifier = Modifier.padding(10.dp),
                    )
                }
                if (item.description != null) {
                    Text(
                        text = item.description.toString(),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
            if(request) {
                Column(
                    modifier = Modifier
                        .padding(3.dp, 0.dp)
                        .wrapContentWidth()
                ) {
                    TextButton(onClick = { vm.addMenuItem(item.id, context , true)}) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_add_task_24),
                            contentDescription = null,
                            tint = colorResource(id = R.color.dark_primary)
                        )
                    }
                    TextButton(onClick = { vm.deleteRequest(item.id, context, false )}) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_delete_forever_24),
                            contentDescription = null,
                            tint = colorResource(id = R.color.dark_primary)
                        )
                    }

                }
            }
        }
    }
}

@Composable
private fun NewItemDialog(onDismissRequest: () -> Unit){
    val context = LocalContext.current
    val nameForm = remember { mutableStateOf("") }
    val priceForm = remember { mutableStateOf("") }
    val descriptionForm = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Dialog(onDismissRequest = { onDismissRequest() }){
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(8.dp)
                .background(colorResource(id = R.color.light_primary)),
            shape = RoundedCornerShape(16.dp),
        ){
            Column{
                Text(text="Név",
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize=20.sp,
                        color = colorResource(id = R.color.dark_primary)
                    ))
                BasicTextField(
                    value = nameForm.value,
                    onValueChange = { nameForm.value = it },
                    maxLines = 1,
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.secondary_text)
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 30.dp, vertical = 10.dp)
                                .fillMaxWidth(0.8f)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(size = 16.dp)
                                )
                                .border(
                                    width = 2.dp,
                                    color = colorResource(id = R.color.divider),
                                    shape = RoundedCornerShape(size = 16.dp)
                                )
                                .padding(all = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            innerTextField()
                        }
                    }
                )
                //Ár input
                Text(text="Ár",
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize=20.sp,
                        color = colorResource(id = R.color.dark_primary)
                    ))
                BasicTextField(
                    value = priceForm.value,
                    onValueChange = { priceForm.value = it },
                    maxLines = 1,
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.secondary_text)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 30.dp, vertical = 10.dp)
                                .fillMaxWidth(0.8f)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(size = 16.dp)
                                )
                                .border(
                                    width = 2.dp,
                                    color = colorResource(id = R.color.divider),
                                    shape = RoundedCornerShape(size = 16.dp)
                                )
                                .padding(all = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) { innerTextField()
                        }
                    }
                )
                Text(text="Leírás",
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize=20.sp,
                        color = colorResource(id = R.color.dark_primary)
                    ))
                BasicTextField(
                    value = descriptionForm.value,
                    onValueChange = { descriptionForm.value = it },
                    maxLines = 1,
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.secondary_text)
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 30.dp, vertical = 10.dp)
                                .fillMaxWidth(0.8f)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(size = 16.dp)
                                )
                                .border(
                                    width = 2.dp,
                                    color = colorResource(id = R.color.divider),
                                    shape = RoundedCornerShape(size = 16.dp)
                                )
                                .padding(all = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) { innerTextField()
                        }
                    }
                )
            }
            Row(modifier = Modifier.fillMaxWidth()
                ,horizontalArrangement = Arrangement.Center){
                Button(onClick = { vm.save(menuTitle == "Étlap", MenuItem(0,nameForm.value, priceForm.value.toInt(),descriptionForm.value), context)
                        onDismissRequest()},
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_primary)),
                ) {
                    Text(text="Mentés")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuPreviewScreen() {
    RestiAdminTheme {
        MenuScreen(navController = rememberNavController(), "Étlap")
    }
}