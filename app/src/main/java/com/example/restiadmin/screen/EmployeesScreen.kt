package com.example.restiadmin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.restiadmin.R
import com.example.restiadmin.data.MenuItem
import com.example.restiadmin.data.User
import com.example.restiadmin.screen.navbar.NavBar
import com.example.restiadmin.ui.theme.RestiAdminTheme
import com.example.restiadmin.viewmodel.EmployeesViewModel


private val vm = EmployeesViewModel()
@Composable
fun EmployeesScreen(navController: NavController){
    LaunchedEffect(Unit, block ={

    } )

    val openDialog = remember { mutableStateOf(false) }

    when{
        openDialog.value -> {
            NewEmployeeDialog(
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
                    Header("Alkalmazottak")
                    EmployeeList()
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { openDialog.value=true },
                containerColor = colorResource(id = R.color.dark_primary),
            ) {
                Icon(
                    Icons.Default.Add, contentDescription = "Add",
                    tint = colorResource(id = R.color.light_primary),)
            }
        },
        bottomBar ={
            NavBar(navController)
        }
    )
}

@Composable
private fun EmployeeList(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.light_primary))
    ) {
        LazyColumn {
            items(vm.employees){ item ->
                Employee(item)
            }
        }
    }
}

@Composable
fun Employee(item: User){
    Box (modifier = Modifier
        .padding(vertical = 12.dp, horizontal = 8.dp)
        .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
        .fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()
        , horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                modifier = Modifier.padding(8.dp).wrapContentWidth()
            ) {
                Text(
                    text = item.name,
                    fontWeight= FontWeight.Bold,
                    modifier = Modifier.padding(5.dp),
                    fontSize = 16.sp
                )
                Row(
                    modifier = Modifier.wrapContentWidth(),
                ) {
                    Text(
                        text = item.email,
                        Modifier.padding(end=6.dp)
                    )
                    Text(
                        text = item.phone,
                    )
                }
            }
            TextButton(onClick = { vm.deleteEmployee(item) }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_delete_forever_24),
                    contentDescription = null,
                    tint = colorResource(id = R.color.dark_primary))
            }
        }
    }
}

@Composable
private fun NewEmployeeDialog(onDismissRequest: () -> Unit){
    var expanded by remember { mutableStateOf(false) }
    //val items= vm.users

    val items= listOf(
        User(0L,"Name1","","","",0L, "", "", "bearer"),
        User(0L,"Name2","","","",0L, "", "", "bearer")
    )
    var newEmployee by remember {mutableStateOf<User>(User(0L,"","","","",0L, "", "", "bearer"))}

    Dialog(onDismissRequest = { onDismissRequest() }){
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(8.dp)
                .background(colorResource(id = R.color.light_primary)),
            shape = RoundedCornerShape(16.dp),
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()){
                Text(text = "Új alkalmazott",
                    modifier = Modifier.padding(vertical=4.dp),
                    fontSize = 18.sp)

                Text(
                    text= newEmployee.name + " - "+newEmployee.email,
                    color = colorResource(R.color.secondary_text),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .border(
                            width = 2.dp,
                            color = colorResource(id = R.color.divider),
                            shape = RoundedCornerShape(size = 16.dp)
                        )
                        .padding(8.dp)
                        .clickable(onClick = { expanded = true })
                )

                DropdownMenu(expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.padding(6.dp)
                                        .fillMaxWidth()) {

                    items.forEachIndexed { _, i ->
                        DropdownMenuItem(
                            text={Text(text=i.name + " - "+i.email)},
                            onClick = {
                                newEmployee = i
                                expanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Button(onClick = { /*vm.save(newEmployee)*/ onDismissRequest()},
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
fun EmployeesPreviewScreen() {
    RestiAdminTheme {
        EmployeesScreen(navController = rememberNavController())
    }
}