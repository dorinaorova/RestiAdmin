package com.example.restiadmin.screen.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.restiadmin.R




@Composable
fun NavBar(navController: NavController){

    val backStackEntry = navController.currentBackStackEntryAsState()

    val bottomNavItems = listOf(
//        BottomNavItem(
//            name = "Restaurant",
//            route = "list_screen",
//            icon = Icons.Rounded.Search,
//        ),
        BottomNavItem(
            name="Menu",
            route = "menu_screen/Étlap",
            icon = ImageVector.vectorResource(R.drawable.baseline_menu_book_24)
        ),
        BottomNavItem(
            name="Reservations",
            route = "reservation_screen",
            icon = ImageVector.vectorResource(R.drawable.baseline_table_bar_24)
        ),
        BottomNavItem(
            name = "Employees",
            route = "employees_screen",
            icon = ImageVector.vectorResource(R.drawable.baseline_groups_24),
        ),
        BottomNavItem(
            name = "Profile",
            route = "profile_screen",
            icon = Icons.Rounded.Person,
        ),
    )

    NavigationBar(containerColor = colorResource(id = R.color.primary))
    {
        bottomNavItems.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route

            NavigationBarItem(
                selected = selected,
                onClick = { navController.navigate(item.route) { launchSingleTop = true }},
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "${item.name} Icon",
                        tint = colorResource(id = R.color.primary_text)
                    )
                }
            )
        }
    }

}

@Composable
@Preview(showBackground =  true)
fun NavBarPreview(){
    NavBar(navController = rememberNavController())
}