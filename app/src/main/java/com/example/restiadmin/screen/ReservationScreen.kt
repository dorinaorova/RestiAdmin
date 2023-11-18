package com.example.restiadmin.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.restiadmin.R
import com.example.restiadmin.data.Reservation
import com.example.restiadmin.navController
import com.example.restiadmin.screen.navbar.NavBar
import com.example.restiadmin.ui.theme.RestiAdminTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.restiadmin.data.StateEnum
import com.example.restiadmin.data.User

import com.example.restiadmin.viewmodel.ReservationViewModel
import java.text.SimpleDateFormat
import java.util.Date


private val vm = ReservationViewModel()

@Composable
fun ReservationScreen(navController: NavController){
    LaunchedEffect(Unit, block ={

    } )

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
private fun ReservationList(){
    val color = colorResource(id = R.color.dark_primary)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.light_primary))
    ) {
        LazyColumn {
            items(vm.current){ item ->
                ReservationItem(item, false)
            }
        }
        if(vm.current.isNotEmpty()) {
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(top = 20.dp)
            ) {
                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                )
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
private fun ReservationItem(item: Reservation, past: Boolean){
    Box (modifier = Modifier
        .padding(vertical = 12.dp, horizontal = 8.dp)
        .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
        .fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Column(modifier = Modifier.padding(8.dp)){
                    Text(text = item.user!!.name+" - "+item.people)
                    Text(text = "${convertDate(item.date)} ${createTimeStr(item.time)}",
                        modifier = Modifier.padding(8.dp,0.dp,0.dp,0.dp))
            }
                Column(
                    modifier = Modifier
                        .padding(3.dp, 8.dp)
                        .wrapContentWidth()
                ) {
                    if (!past && item.state==StateEnum.PENDING) {
                        TextButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_add_task_24),
                                contentDescription = null,
                                tint = colorResource(id = R.color.dark_primary),
                            )
                        }
                        TextButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_delete_forever_24),
                                contentDescription = null,
                                tint = colorResource(id = R.color.dark_primary),
                            )
                        }
                    }
                    else if(past || item.state != StateEnum.PENDING) {
                        val icon = when (item.state) {
                            StateEnum.ACCEPTED -> ImageVector.vectorResource(R.drawable.baseline_check_circle_outline_24)
                            StateEnum.DECLINED -> ImageVector.vectorResource(R.drawable.baseline_remove_circle_outline_24)
                            else -> {
                                ImageVector.vectorResource(R.drawable.baseline_question_mark_24)
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