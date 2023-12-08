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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.restiadmin.R
import com.example.restiadmin.data.StateEnum
import com.example.restiadmin.data.requestmodel.ReservationRequest
import com.example.restiadmin.screen.navbar.NavBar
import com.example.restiadmin.ui.theme.RestiAdminTheme
import com.example.restiadmin.viewmodel.ReservationViewModel
import java.text.SimpleDateFormat
import java.util.Date


private val vm = ReservationViewModel()
val openDialog =mutableStateOf(false)
val state = mutableStateOf(StateEnum.PENDING)
val id = mutableLongStateOf(0L)

@Composable
fun ReservationScreen(navController: NavController){
    val context = LocalContext.current
    LaunchedEffect(Unit, block ={
        vm.fetchData(context)
    } )



    when{
        openDialog.value -> {
            NoteDialog(
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
                    Header("Asztal foglalások")
                    ReservationList()
                }
            }
        },
        bottomBar ={
            NavBar(navController)
        }
    )
}

@Composable
private fun NoteDialog(onDismissRequest: () -> Unit){
    val context = LocalContext.current
    val note= remember { mutableStateOf("") }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(8.dp)
                .background(colorResource(id = R.color.light_primary)),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Megjegyzés",
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontSize = 18.sp
                )
                BasicTextField(
                    value = note.value,
                    onValueChange = { note.value = it },
                    maxLines = 1,
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.secondary_text)
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
                Button(onClick = {
                    //vm.update(id,context, null, state)
                    vm.update(id.longValue,context, note.value, state.value)
                    onDismissRequest()},
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_primary)),
                ) {
                    Text(text="Mentés")
                }
            }
        }}
}


@Composable
private fun ReservationList(){
    val color = colorResource(id = R.color.dark_primary)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.light_primary))
    ) {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.6f)) {
            LazyColumn {
                items(vm.current) { item ->
                    ReservationItem(item, false)
                }
            }
        }
        if(vm.current.isNotEmpty()) {
            Box (modifier = Modifier.fillMaxWidth().fillMaxHeight(0.1f)){
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
            items(vm.past){ item ->
                ReservationItem(item, true)
            }
        }
    }
}

@Composable
private fun ReservationItem(request: ReservationRequest, past: Boolean){
    val item = request
    val context = LocalContext.current
    Box (modifier = Modifier
        .padding(vertical = 12.dp, horizontal = 8.dp)
        .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
        .fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Column(modifier = Modifier.padding(8.dp)){
                    Text(text = item.reservation!!.user!!.name+" - "+item.reservation.people)
                    Text(text = "${convertDate(item.reservation.date)} ${createTimeStr(item.reservation.time)}",
                        modifier = Modifier.padding(8.dp,0.dp,0.dp,0.dp))
            }
                Column(
                    modifier = Modifier
                        .padding(3.dp, 8.dp)
                        .wrapContentWidth()
                ) {
                    if (!past && item.state===StateEnum.PENDING) {
                        TextButton(
                            onClick = {
                                openDialog.value=true
                                state.value= StateEnum.ACCEPTED
                                id.longValue= item.id

                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_add_task_24),
                                contentDescription = null,
                                tint = colorResource(id = R.color.dark_primary),
                            )
                        }
                        TextButton(onClick = {
                            openDialog.value=true
                            state.value= StateEnum.DECLINED
                            id.longValue= item.id
                        }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_delete_forever_24),
                                contentDescription = null,
                                tint = colorResource(id = R.color.dark_primary),
                            )
                        }
                    }
                    else if(past || item.state !== StateEnum.PENDING) {
                        val icon = when (item.state) {
                            StateEnum.ACCEPTED -> ImageVector.vectorResource(R.drawable.baseline_check_circle_outline_24)
                            StateEnum.DECLINED -> ImageVector.vectorResource(R.drawable.baseline_remove_circle_outline_24)
                            else -> {
                                ImageVector.vectorResource(R.drawable.baseline_question_mark_24) //PENDING
                            }
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = colorResource(id = R.color.dark_primary),
                        )
                    }

                }
            }
    }
}

private fun createTimeStr(time: Int): String{
    val hours = if(time/100 <10) "0${time/100}" else "${time/100}"
    val minutes = if(time%100 <10) "0${time%100}" else "${time%100}"
    return "$hours:$minutes"
}

private fun convertDate(time: Long): String{
    val date = Date(time)
    val format = SimpleDateFormat("yyyy.MM.dd.")
    return format.format(date)
}

@Preview(showBackground = true)
@Composable
fun ReservationPreviewScreen() {
    RestiAdminTheme {
        ReservationScreen(navController = rememberNavController())
    }
}